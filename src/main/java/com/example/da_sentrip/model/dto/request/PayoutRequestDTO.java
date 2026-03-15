package com.example.da_sentrip.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutRequestDTO {
    private String orderCode;
    private Long merchantId;
}