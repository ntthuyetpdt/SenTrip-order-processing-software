package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PRODUCTS")
public class Product  extends BaseCreatedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @ManyToOne
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private Merchant merchant;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "REFUNDABLE")
    private Integer refundable;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name="TYPE")
    private String Type;

    @Column (name = "ADDRESS")
    private String address;

    @Column (name ="IMG")
    private String img;

    @Column (name = "ADDITIONAL_SERVICES")
    private String additionalService;
}
