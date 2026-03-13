package com.example.da_sentrip.controller;

import com.example.da_sentrip.service.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/exsport")
@AllArgsConstructor
public class ExportController {
    private final ExportService exportService;


    @PostMapping("/bill/{orderCode}")
    public ResponseEntity<?> exportInvoice(@PathVariable String orderCode) {
        String fileUrl = exportService.exportInvoicePdf(orderCode);

        return ResponseEntity.ok(Map.of(
                "message", "Export invoice successfully",
                "url", fileUrl
        ));
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportOrdersExcel() {
        byte[] excelBytes = exportService.exportOrdersExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
