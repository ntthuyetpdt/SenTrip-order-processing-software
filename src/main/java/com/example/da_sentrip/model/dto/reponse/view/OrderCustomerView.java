package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderCustomerView {
    Long getOrderId();

    String getOrderCode();

    LocalDateTime getCreatedAt();

    String getFullName();

    String getPhone();

    String getAddress();

    String getProductName();

    String getType();

    String getAdditionalServices();

    Integer getQuantity();

    BigDecimal getPrice();
}
