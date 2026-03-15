package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InvoiceDTO {
    String getOrderCode();
    String getInvoiceCode();
    BigDecimal getAmount();
    String getStatus();
    String getFileName();
    LocalDateTime getGeneratedAt();
    String getFileUrl();
}