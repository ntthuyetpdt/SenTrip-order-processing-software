package com.example.da_sentrip.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProductRequestDTO {
    private String productName;
    private String serviceType;
    private BigDecimal price;
    private Integer refundable;
    private Integer status;
    private LocalDateTime createdAt;
    private String Type;
    private  String address;
    private String img;
}
