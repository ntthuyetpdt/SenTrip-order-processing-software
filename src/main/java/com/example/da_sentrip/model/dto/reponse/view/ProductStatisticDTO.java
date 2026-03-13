package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;

public interface ProductStatisticDTO {

    Long getProductId();

    String getProductName();

    String getAdditionalServices();

    Long getTotalCustomers();

    Long getTotalOrders();

    BigDecimal getTotalRevenue();
}