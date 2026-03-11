package com.example.da_sentrip.controller;

import com.example.da_sentrip.service.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exsport")
@AllArgsConstructor
public class ExportController {
    private final ExportService exportService;


    @GetMapping("/Bill/{orderCode}")
    public ResponseEntity<byte[]> exportInvoice(@PathVariable String orderCode) {

        byte[] pdfBytes = exportService.exportInvoicePdf(orderCode);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + orderCode + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportOrdersExcel() {
        byte[] excelBytes = exportService.exportOrdersExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
