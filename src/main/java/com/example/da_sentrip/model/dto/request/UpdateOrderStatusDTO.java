package com.example.da_sentrip.model.dto.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateOrderStatusDTO {
    private String orderCode;
    private String orderStatus;
}
