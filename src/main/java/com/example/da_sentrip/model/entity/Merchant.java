package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MERCHANTS")
public class Merchant extends  BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", unique = true)
    private User user;

    @Column(name = "MERCHANT_CODE")
    private String merchantCode;

    @Column(name = "MERCHANT_NAME")
    private String merchantName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name ="CCCD")
    private String cccd;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    @Column(name="TAX_CODE")
    private String TaxCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name ="IMG")
    private String img;

}
