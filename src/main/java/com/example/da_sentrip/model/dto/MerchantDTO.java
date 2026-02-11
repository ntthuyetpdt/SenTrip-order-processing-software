package com.example.da_sentrip.model.dto;


import com.example.da_sentrip.model.entity.Merchant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantDTO {
    private String merchantName;
    private String phone;
    private String cccd;
    private String bankName;
    private String bankAccount;
    private String address;
    private String businessLicense;
    public MerchantDTO(Merchant merchant){
        this.merchantName = merchant.getMerchantName();
        this.phone =merchant.getPhone();
        this.cccd=merchant.getCccd();
        this.bankName = merchant.getBankName();
        this.bankAccount=merchant.getBankAccount();
        this.address = merchant.getAddress();
        this.businessLicense = merchant.getBusinessLicense();

    }
}
