package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderSummaryView {
    LocalDateTime getCreatedAt();
    String getOrderCode();
    String getOrderStatus();     // lấy từ o.ORDER_STATUS
    BigDecimal getTotalAmount();     // u.GMAIL as gmail
    String getProductNames();
    String getAdditionalService();
    String getImg();
    String getQuantities();
}
