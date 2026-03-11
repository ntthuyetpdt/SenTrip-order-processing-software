package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.reponse.OrderAllReponseDTO;
import com.example.da_sentrip.model.dto.reponse.view.InvoiceProjection;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.service.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final OrderRepository orderRepository;

    @Override
    public byte[] exportInvoicePdf(String orderCode) {
        InvoiceProjection invoice = orderRepository.findInvoiceByOrderId(orderCode);
        if (invoice == null) {
            throw new RuntimeException("No orders found with ID = " + orderCode);
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("BILL", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Oder code: " + invoice.getOrderCode(), normalFont));
            document.add(new Paragraph("Full Name: " + invoice.getFullNameCustomer(), normalFont));
            document.add(new Paragraph("Phone: " + invoice.getPhone(), normalFont));
            document.add(new Paragraph("Address: " + invoice.getAddress(), normalFont));
            document.add(new Paragraph(
                    "Create at: " + invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    normalFont
            ));
            document.add(new Paragraph("Order status: " + invoice.getOrderStatus(), normalFont));
            document.add(new Paragraph("Payment status " + invoice.getPaymentStatus(), normalFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell("product");
            table.addCell("quantity");

            String[] products = invoice.getProductNames() != null
                    ? invoice.getProductNames().split(",\\s*")
                    : new String[0];

            String[] quantities = invoice.getQuantities() != null
                    ? invoice.getQuantities().split(",\\s*")
                    : new String[0];

            int max = Math.max(products.length, quantities.length);
            for (int i = 0; i < max; i++) {
                table.addCell(i < products.length ? products[i] : "");
                table.addCell(i < quantities.length ? quantities[i] : "");
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tong tien: " + invoice.getTotalAmount(), titleFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error when exporting invoices to PDF", e);
        }
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
                row.createCell(2).setCellValue(
                        order.getCreatedAt() != null ? order.getCreatedAt().format(formatter) : ""
                );
                row.createCell(3).setCellValue(order.getServiceType());
                row.createCell(4).setCellValue(order.getAddress());
                row.createCell(5).setCellValue(
                        order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0
                );
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