package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TicketView {
    String getFullName();
    String getPhone();
    String getGmail();
    String getProductName();
    String getServiceType();
    String getType();
    LocalDateTime getCreateTime();
    String getStatus();
    BigDecimal getTotalAmount();
    LocalDateTime getPaidAt();
    String getPaymentCode();

}
