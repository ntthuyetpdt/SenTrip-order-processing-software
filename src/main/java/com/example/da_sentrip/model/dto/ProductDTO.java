package com.example.da_sentrip.model.dto;

import com.example.da_sentrip.model.entity.Product;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProductDTO {
    private String productName;
    private String serviceType;
    private String price;
    private Integer refundable;
    private Integer status;
    private LocalDateTime createdAt;
    private String Type;
    public ProductDTO (Product product){
        this.productName = product.getProductName();
        this.serviceType = product.getServiceType();
        this.price= product.getPrice();
        this.refundable=product.getRefundable();
        this.status = product.getStatus();
        this.createdAt= product.getCreatedAt();
        this.Type = product.getType();
    }

}
