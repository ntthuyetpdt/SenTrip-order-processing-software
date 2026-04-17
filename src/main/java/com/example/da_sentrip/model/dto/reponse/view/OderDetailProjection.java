package com.example.da_sentrip.model.dto.reponse.view;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface OderDetailProjection {
        LocalDateTime getCreatedAt();
        String getOrderCode();
        String getOrderStatus();
        Double getTotalAmount();
        LocalDate getNSD();
        String getType();
        Long getUserId();
        String getGmail();
        String getFullName();
        String getPhone();
        String getCccd();
        String getPaymentStatus();
        String getPaymentCode();
        LocalDateTime getPaidAt();
        String getProductNames();
        String getAdditionalService();
        String getServiceType();
        String getDescription();
        String getQuantities();
        String getImg();

}
