package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "CART_ITEMS")
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CART_ID")
    private BigDecimal cartId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product productId;

    @Column(name = "QUANTITY")
    private BigDecimal quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name="NSD")
    private LocalDate NSD;
}