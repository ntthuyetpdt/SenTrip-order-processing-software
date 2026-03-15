package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.service.ExportService;
import com.example.da_sentrip.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/export")
@AllArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/bill/{orderCode}")
    @PreAuthorize("hasAnyAuthority('ADMIN_IMPORT_BILL')")
    public ResponseEntity<ResponseDTO> exportInvoice(@PathVariable String orderCode) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Export invoice successfully")
                .data(exportService.exportInvoicePdf(orderCode))
                .build());
    }

    @GetMapping("/excel")
    @PreAuthorize("hasAnyAuthority('ADMIN_EXCEL')")
    public ResponseEntity<byte[]> exportOrdersExcel() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(exportService.exportOrdersExcel());
    }
}