package com.example.da_sentrip.model.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class OrderAllReponseDTO {
    private String orderCode;
    private String fullNameCustomer;
    private LocalDateTime createdAt;
    private String serviceType;
    private String additionalService;
    private String address;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentStatus;

    public OrderAllReponseDTO(String orderCode, String fullNameCustomer, LocalDateTime createdAt, String serviceType,String additionalService, String  address, BigDecimal totalAmount, String orderStatus, String paymentStatus) {
        this.orderCode=orderCode;
        this.fullNameCustomer=fullNameCustomer;
        this.createdAt=createdAt;
        this.serviceType=serviceType;
        this.additionalService=additionalService;
        this.address=address;
        this.totalAmount=totalAmount;
        this.orderStatus=orderStatus;
        this.paymentStatus=paymentStatus;
    }
}
