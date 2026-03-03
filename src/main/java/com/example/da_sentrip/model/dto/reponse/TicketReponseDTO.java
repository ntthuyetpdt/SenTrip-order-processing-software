package com.example.da_sentrip.model.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TicketReponseDTO {
    private String fullName;
    private String phone;
    private String gmail;
    private String cccd;
    private String productName;
    private String serviceType;
    private String type;
    private LocalDateTime createTime;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime paidAt;
    private String paymentCode;
    private String img;
    private String quantities;

    public TicketReponseDTO(String fullName, String phone, String gmail,String cccd,
                            String productName, String serviceType, String type,
                            LocalDateTime createTime, String status,
                            BigDecimal totalAmount, LocalDateTime paidAt,
                            String paymentCode,String img,String quantities) {
        this.fullName = fullName;
        this.phone = phone;
        this.gmail = gmail;
        this.cccd =cccd;
        this.productName = productName;
        this.serviceType = serviceType;
        this.type = type;
        this.createTime = createTime;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paidAt = paidAt;
        this.paymentCode = paymentCode;
        this.img =img;
        this.quantities =quantities;
    }


}
