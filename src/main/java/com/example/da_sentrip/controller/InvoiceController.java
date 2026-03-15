package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.InvoiceResponse;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.service.InvoiceService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN_VIEW_INVOICE')")
    public ResponseEntity<ResponseDTO> getAllInvoices() {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get all invoices success")
                .data(invoiceService.getAllInvoices())
                .build());
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN_INPUT_INVOICE')")
    public ResponseEntity<ResponseDTO> uploadInvoicePdf(
            @RequestPart MultipartFile file,
            @RequestParam String orderCode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.CREATED)
                .message("Upload invoice PDF success")
                .data(invoiceService.uploadInvoicePdf(file, orderCode))
                .build());
    }
    @GetMapping("/view")
    public ResponseEntity<?> getAllInvoices(Authentication authentication) {
        List<InvoiceResponse> data = invoiceService.getAllInvoice();
        return ResponseEntity.ok(Map.of(
                "message", "View success",
                "data", data
        ));
    }
    @PostMapping(" /submit-input")
    public ResponseEntity<?> submitInputInvoice(
            @RequestParam("orderCode") String orderCode,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        invoiceService.submitInputInvoice(orderCode, file);
        return ResponseEntity.ok(Map.of(
                "message", "Invoice has been issued successfully"
        ));
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchInvoices(
            @RequestParam(required = false) String invoiceCode,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Authentication authentication) {
        List<InvoiceResponse> data = invoiceService.searchInvoices(invoiceCode, orderCode, fromDate, toDate);
        return ResponseEntity.ok(Map.of(
                "message", "View success",
                "data", data
        ));
    }
}