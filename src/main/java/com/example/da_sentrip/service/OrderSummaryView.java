package com.example.da_sentrip.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderSummaryView {
    LocalDateTime getCreatedAt();
    String getOrderCode();
    String getOrderStatus();     // lấy từ o.ORDER_STATUS
    BigDecimal getTotalAmount();
    Long getUserId();            // u.ID as userId
    String getGmail();           // u.GMAIL as gmail
    String getProductNames();
}
