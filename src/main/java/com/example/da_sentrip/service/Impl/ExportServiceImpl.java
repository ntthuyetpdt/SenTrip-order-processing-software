package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.SupabasePDF;
import com.example.da_sentrip.model.dto.reponse.OrderAllReponseDTO;
import com.example.da_sentrip.model.dto.reponse.view.InvoiceProjection;
import com.example.da_sentrip.model.entity.Invoices;
import com.example.da_sentrip.repository.InvoiceRepository;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.service.ExportService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final OrderRepository orderRepository;
    private final SupabasePDF cloudinaryPDF;
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public String exportInvoicePdf(String orderCode) {
        Optional<Invoices> existing = invoiceRepository.findByOrderId(orderCode);
        if (existing.isPresent()) return existing.get().getFileUrl();

        InvoiceProjection invoice = orderRepository.findInvoiceByOrderCode(orderCode);
        if (invoice == null) throw new RuntimeException("Không tìm thấy đơn hàng với orderCode = " + orderCode);

        try {
            byte[] pdfBytes = generateInvoicePdf(invoice);
            String invoiceCode = "INV" + System.currentTimeMillis();
            String fileName = invoiceCode + ".pdf";
            String fileUrl = uploadPdfToCloudinary(pdfBytes, fileName);

            Invoices entity = new Invoices();
            entity.setInvoiceCode(invoiceCode);
            entity.setOderId(invoice.getOrderId());
            entity.setMerchantId(invoice.getMerchantId());
            entity.setAmount(invoice.getTotalAmount());
            entity.setStatus(resolveInvoiceStatus(invoice.getPaymentStatus()));
            entity.setPdfPart("CLOUDINARY");
            entity.setFileName(fileName);
            entity.setFileUrl(fileUrl);
            entity.setGeneratedAt(LocalDateTime.now());
            entity.setCreatedAt(LocalDateTime.now());
            invoiceRepository.save(entity);
            return fileUrl;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi export invoice PDF", e);
        }
    }

    private byte[] generateInvoicePdf(InvoiceProjection invoice) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            InputStream fontStream = getClass().getClassLoader()
                    .getResourceAsStream("fonts/NotoSans-VariableFont_wdth,wght.ttf");
            if (fontStream == null) throw new RuntimeException("Khong tim thay font");

            BaseFont baseFont = BaseFont.createFont(
                    "NotoSans-VariableFont_wdth,wght.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED, true, fontStream.readAllBytes(), null
            );

            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);

            document.add(new Paragraph("BILL TRAVEL", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Mã đơn hàng: " + safe(invoice.getOrderCode()), normalFont));
            document.add(new Paragraph("Họ và tên: " + safe(invoice.getFullNameCustomer()), normalFont));
            document.add(new Paragraph("Số điện thoại: " + safe(invoice.getPhone()), normalFont));
            document.add(new Paragraph("Địa chỉ: " + safe(invoice.getAddress()), normalFont));
            if (invoice.getCreatedAt() != null) {
                document.add(new Paragraph("Ngày tạo: " +
                        invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            }
            document.add(new Paragraph("Trạng thái đơn: " + safe(invoice.getOrderStatus()), normalFont));
            document.add(new Paragraph("Trạng thái Thanh toán: " + safe(invoice.getPaymentStatus()), normalFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 1.5f});
            table.addCell(new PdfPCell(new Phrase("Tên dịch vụ", normalFont)));
            table.addCell(new PdfPCell(new Phrase("Số lượng", normalFont)));

            String[] products = invoice.getProductNames() != null ? invoice.getProductNames().split(",\\s*") : new String[0];
            String[] quantities = invoice.getQuantities() != null ? invoice.getQuantities().split(",\\s*") : new String[0];

            for (int i = 0; i < Math.max(products.length, quantities.length); i++) {
                table.addCell(new PdfPCell(new Phrase(i < products.length ? products[i] : "", normalFont)));
                table.addCell(new PdfPCell(new Phrase(i < quantities.length ? quantities[i] : "", normalFont)));
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tổng: " +
                    (invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO), titleFont));
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file PDF", e);
        }
    }

    private String uploadPdfToCloudinary(byte[] fileBytes, String fileName) {
        try {
            return cloudinaryPDF.uploadPdf(fileBytes, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi upload PDF lên Cloudinary", e);
        }
    }

    private String resolveInvoiceStatus(String paymentStatus) {
        if (paymentStatus == null || paymentStatus.isBlank()) return "UNPAID";
        return switch (paymentStatus.trim().toUpperCase()) {
            case "PAID", "COMPLETED", "SUCCESS" -> "GENERATED";
            case "UNPAID", "PENDING", "FAILED", "CANCELLED" -> paymentStatus.trim().toUpperCase();
            default -> "GENERATED";
        };
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    @Override
    public byte[] exportOrdersExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Orders");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            String[] headers = {"Mã đặt hàng", "Tên khách hàng", "Thời gian tạo", "Loại",
                    "Địa chỉ", "Tổng số tiền", "Trạng thái đơn hàng", "Trạng thái Thanh toán"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);

            int rowIdx = 1;
            for (OrderAllReponseDTO o : getAll()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(o.getOrderCode());
                row.createCell(1).setCellValue(o.getFullNameCustomer());
                row.createCell(2).setCellValue(o.getCreatedAt() != null ? o.getCreatedAt().format(formatter) : "");
                row.createCell(3).setCellValue(o.getServiceType());
                row.createCell(4).setCellValue(o.getAddress());
                row.createCell(5).setCellValue(o.getTotalAmount() != null ? o.getTotalAmount().doubleValue() : 0);
                row.createCell(6).setCellValue(o.getOrderStatus());
                row.createCell(7).setCellValue(o.getPaymentStatus());
            }

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error when producing Excel file", e);
        }
    }

    private List<OrderAllReponseDTO> getAll() {
        return orderRepository.findAllOrderSummary().stream()
                .map(view -> new OrderAllReponseDTO(
                        view.getOrderCode(), view.getFullNameCustomer(), view.getCreatedAt(),
                        view.getServiceType(), view.getAdditionalService(), view.getAddress(),
                        view.getTotalAmount(), view.getOrderStatus(), view.getPaymentStatus()
                ))
                .toList();
    }
}