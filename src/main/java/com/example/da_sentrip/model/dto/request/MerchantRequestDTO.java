package com.example.da_sentrip.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantRequestDTO {
    private String merchantName;
    private String phone;
    private String cccd;
    private String bankName;
    private String bankAccount;
    private String address;
    private String img;
    private String businessLicense;
}
