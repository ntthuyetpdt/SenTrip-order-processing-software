package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Merchant;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MerchantReponseDTO {
    private String merchantName;
    private String phone;
    private String cccd;
    private String bankName;
    private String bankAccount;
    private String address;
    private String img;
    private String businessLicense;
    public MerchantReponseDTO(Merchant merchant){
        this.merchantName = merchant.getMerchantName();
        this.phone =merchant.getPhone();
        this.cccd=merchant.getCccd();
        this.bankName = merchant.getBankName();
        this.bankAccount=merchant.getBankAccount();
        this.address = merchant.getAddress();
        this.img =merchant.getImg();
        this.businessLicense = merchant.getBusinessLicense();

    }
}
