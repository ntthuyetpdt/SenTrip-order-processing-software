package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InvoiceDetailProjection {
    Long getInvoiceId();
    String getInvoiceCode();
    Long getMerchantId();
    BigDecimal getAmount();
    String getStatus();
    String getFileName();
    LocalDateTime getGeneratedAt();
    String getFileUrl();
    String getOrderCode();
    String getOrderStatus();
    BigDecimal getTotalAmount();
    LocalDateTime getOrderCreatedAt();
    String getGmail();
    String getFullName();
}
