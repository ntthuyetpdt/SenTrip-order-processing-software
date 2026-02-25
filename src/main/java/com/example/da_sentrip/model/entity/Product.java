package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "REFUNDABLE")
    private Integer refundable;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="TYPE")
    private String Type;

    @Column (name = "ADDRESS")
    private String address;

    @Column (name ="IMG")
    private String img;
}
