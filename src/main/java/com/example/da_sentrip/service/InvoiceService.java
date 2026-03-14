package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.reponse.view.InvoiceDetailProjection;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDetailProjection> getAllInvoices();
    String uploadInvoicePdf(MultipartFile file, String orderCode);
}