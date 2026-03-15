package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.service.InvoiceService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}