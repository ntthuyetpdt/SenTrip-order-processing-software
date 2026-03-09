package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProductMechartReponseDTO {
    private String orderCode;
    private LocalDateTime createdAt;
    private String fullName;
    private String phone;
    private String productName;
    private String type;
    private String additionalServices;
    private String address;
    private Integer quantity;
    private BigDecimal price;


}
