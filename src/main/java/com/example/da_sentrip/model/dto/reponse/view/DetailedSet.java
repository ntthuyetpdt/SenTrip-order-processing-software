package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DetailedSet {
    String getOrderCode();
    String getFullNameCustomer();
    String getCccd();
    String getPhone();
    String getAddress();
    String getGmail();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    BigDecimal getTotalAmount();
    String getOrderStatus();
    String getPaymentStatus();
    String getPaymentCode();
    BigDecimal getPaymentAmount();
    LocalDateTime getPaidAt();
    String getProductNames();
    String getQuantities();
}