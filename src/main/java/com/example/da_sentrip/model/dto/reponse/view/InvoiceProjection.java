package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InvoiceProjection {
    Long getOrderId();
    String getOrderCode();
    Long getMerchantId();
    String getFullNameCustomer();
    String getPhone();
    String getAddress();
    LocalDateTime getCreatedAt();
    BigDecimal getTotalAmount();
    String getOrderStatus();
    String getPaymentStatus();
    String getProductNames();
    String getQuantities();
}
