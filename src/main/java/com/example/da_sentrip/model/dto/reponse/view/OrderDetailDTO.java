package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderDetailDTO {
    String getOrderCode();
    String getFullNameCustomer();
    String getPhoneNumber();
    String getProductName();
    Integer getQuantity();
    BigDecimal getPrice();
    LocalDateTime getCreatedAt();
}