package com.example.da_sentrip.model.dto.reponse.view;

public interface GetallOder {
    java.time.LocalDateTime getCreatedAt();
    String getOrderCode();
    String getOrderStatus();
    java.math.BigDecimal getTotalAmount();

    Long getUserId();
    String getGmail();

    String getProductNames();
    String getAdditionalService();
    String getQuantities();
    String getImg();
}
