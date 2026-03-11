package com.example.da_sentrip.model.dto.reponse.view;

public interface InvoiceProjection {
    String getOrderCode();
    String getFullNameCustomer();
    String getPhone();
    String getAddress();
    java.time.LocalDateTime getCreatedAt();
    java.math.BigDecimal getTotalAmount();
    String getOrderStatus();
    String getPaymentStatus();
    String getProductNames();
    String getQuantities();
}
