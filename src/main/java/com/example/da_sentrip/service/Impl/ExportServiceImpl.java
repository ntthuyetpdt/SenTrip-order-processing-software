package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.CloudinaryPDF;
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
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final OrderRepository orderRepository;
    private final CloudinaryPDF cloudinaryPDF;
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public String exportInvoicePdf(String orderCode) {
        Optional<Invoices> existingInvoice = invoiceRepository.findByOrderId(orderCode);
        if (existingInvoice.isPresent()) {
            return existingInvoice.get().getFileUrl();
        }

        InvoiceProjection invoice = orderRepository.findInvoiceByOrderCode(orderCode);
        if (invoice == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng với orderCode = " + orderCode);
        }

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

//    private byte[] generateInvoicePdf(InvoiceProjection invoice) {
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//            Document document = new Document();
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
//            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//            document.add(new Paragraph("BILL TRAVEL", titleFont));
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("Order code: " + safe(invoice.getOrderCode()), normalFont));
//            document.add(new Paragraph("Customer: " + safe(invoice.getFullNameCustomer()), normalFont));
//            document.add(new Paragraph("Phone: " + safe(invoice.getPhone()), normalFont));
//            document.add(new Paragraph("Address: " + safe(invoice.getAddress()), normalFont));
//
//            if (invoice.getCreatedAt() != null) {
//                document.add(new Paragraph(
//                        "Created at: " + invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
//                        normalFont
//                ));
//            }
//            document.add(new Paragraph("Order status: " + safe(invoice.getOrderStatus()), normalFont));
//            document.add(new Paragraph("Payment status: " + safe(invoice.getPaymentStatus()), normalFont));
//            document.add(new Paragraph(" "));
//
//            PdfPTable table = new PdfPTable(2);
//            table.setWidthPercentage(100);
//            table.addCell("Product");
//            table.addCell("Quantity");
//
//            String[] products = invoice.getProductNames() != null
//                    ? invoice.getProductNames().split(",\\s*")
//                    : new String[0];
//
//            String[] quantities = invoice.getQuantities() != null
//                    ? invoice.getQuantities().split(",\\s*")
//                    : new String[0];
//
//            int max = Math.max(products.length, quantities.length);
//            for (int i = 0; i < max; i++) {
//                table.addCell(i < products.length ? products[i] : "");
//                table.addCell(i < quantities.length ? quantities[i] : "");
//            }
//
//            document.add(table);
//            document.add(new Paragraph(" "));
//
//            BigDecimal totalAmount = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
//            document.add(new Paragraph("Total amount: " + totalAmount, titleFont));
//
//            document.close();
//            return out.toByteArray();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Lỗi khi tạo file PDF", e);
//        }
//    }

    private byte[] generateInvoicePdf(InvoiceProjection invoice) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/NotoSans-VariableFont_wdth,wght.ttf");
            if (fontStream == null) {
                throw new RuntimeException("Khong tim thay font: fonts/NotoSans-Regular.ttf");
            }

            byte[] fontBytes = fontStream.readAllBytes();

            BaseFont baseFont = BaseFont.createFont(
                    "NotoSans-VariableFont_wdth,wght.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    fontBytes,
                    null
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
                document.add(new Paragraph(
                        "Ngày tạo: " + invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        normalFont
                ));
            }

            document.add(new Paragraph("Trạng thái đơn: " + safe(invoice.getOrderStatus()), normalFont));
            document.add(new Paragraph("Trạng thái Thanh toán: " + safe(invoice.getPaymentStatus()), normalFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 1.5f});

            PdfPCell header1 = new PdfPCell(new Phrase("Tên dịch vụ", normalFont));
            PdfPCell header2 = new PdfPCell(new Phrase("số lượng ", normalFont));
            table.addCell(header1);
            table.addCell(header2);

            String[] products = invoice.getProductNames() != null
                    ? invoice.getProductNames().split(",\\s*")
                    : new String[0];

            String[] quantities = invoice.getQuantities() != null
                    ? invoice.getQuantities().split(",\\s*")
                    : new String[0];

            int max = Math.max(products.length, quantities.length);
            for (int i = 0; i < max; i++) {
                table.addCell(new PdfPCell(new Phrase(i < products.length ? products[i] : "", normalFont)));
                table.addCell(new PdfPCell(new Phrase(i < quantities.length ? quantities[i] : "", normalFont)));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            BigDecimal totalAmount = invoice.getTotalAmount() != null
                    ? invoice.getTotalAmount()
                    : BigDecimal.ZERO;

            document.add(new Paragraph("Tổng: " + totalAmount, titleFont));

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
        if (paymentStatus == null || paymentStatus.isBlank()) {
            return "UNPAID";
        }

        String normalized = paymentStatus.trim().toUpperCase();

        return switch (normalized) {
            case "PAID", "COMPLETED", "SUCCESS" -> "GENERATED";
            case "UNPAID", "PENDING", "FAILED", "CANCELLED" -> normalized;
            default -> "GENERATED";
        };
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }


    @Override
    public byte[] exportOrdersExcel() {
        List<OrderAllReponseDTO> orders = getAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Orders");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Mã đặt hàng");
            header.createCell(1).setCellValue("Tên khách hàng");
            header.createCell(2).setCellValue("Thời gian tạo");
            header.createCell(3).setCellValue("Loại");
            header.createCell(4).setCellValue("Địa chỉ");
            header.createCell(5).setCellValue("Tổng số tiền");
            header.createCell(6).setCellValue("Trạng thái đơn hàng");
            header.createCell(7).setCellValue("Trạng thái Thanh toán");
            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (OrderAllReponseDTO order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getOrderCode());
                row.createCell(1).setCellValue(order.getFullNameCustomer());
                row.createCell(2).setCellValue(order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : "");
                row.createCell(3).setCellValue(order.getServiceType());
                row.createCell(4).setCellValue(order.getAddress());
                row.createCell(5).setCellValue(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0);
                row.createCell(6).setCellValue(order.getOrderStatus());
                row.createCell(7).setCellValue(order.getPaymentStatus());
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error when producing Excel file", e);
        }
    }

    private List<OrderAllReponseDTO> getAll() {
        return orderRepository.findAllOrderSummary()
                .stream()
                .map(view -> new OrderAllReponseDTO(
                        view.getOrderCode(),
                        view.getFullNameCustomer(),
                        view.getCreatedAt(),
                        view.getServiceType(),
                        view.getAdditionalService(),
                        view.getAddress(),
                        view.getTotalAmount(),
                        view.getOrderStatus(),
                        view.getPaymentStatus()
                ))
                .toList();
    }
}