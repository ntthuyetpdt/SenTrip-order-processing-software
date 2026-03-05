package com.example.da_sentrip.model.dto.reponse;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class OderdetailReponseDTO {

    private LocalDateTime createdAt;
    private String orderCode;
    private String orderStatus;
    private Double totalAmount;
    private Long userId;
    private String gmail;
    private String fullName;
    private String phone;
    private String cccd;
    private String paymentStatus;
    private String paymentCode;
    private LocalDateTime paidAt;
    private String productNames;
    private String Type;
    private String serviceType;
    private String additionalService;
    private String description;
    private String quantities;
    private String img;

}