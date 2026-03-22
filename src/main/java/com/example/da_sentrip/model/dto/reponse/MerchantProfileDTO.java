package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class MerchantProfileDTO {
    private String gmail;
    private String role;
    private List<String> permissions;
    private String merchantCode;
    private String merchantName;
    private String phone;
    private String cccd;
    private String bankName;
    private String bankAccount;
    private String address;
    private String img;
    private String businessLicense;
}