package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.reponse.InvoiceResponse;
import com.example.da_sentrip.model.dto.reponse.view.InvoiceDetailProjection;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDetailProjection> getAllInvoices();
    String uploadInvoicePdf(MultipartFile file, String orderCode);
    List<InvoiceResponse> searchInvoices(String invoiceCode, String orderCode, LocalDateTime fromDate, LocalDateTime toDate);
    void submitInputInvoice(String orderCode, MultipartFile file);
    List<InvoiceResponse> getAllInvoice();
}