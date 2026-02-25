package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Product;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductReponseDTO {
    private String productName;
    private String serviceType;
    private BigDecimal price;
    private Integer refundable;
    private Integer status;
    private LocalDateTime createdAt;
    private String Type;
    private String img;
    public ProductReponseDTO (Product product){
        this.productName = product.getProductName();
        this.serviceType = product.getServiceType();
        this.price= product.getPrice();
        this.refundable=product.getRefundable();
        this.status = product.getStatus();
        this.createdAt= product.getCreatedAt();
        this.Type= product.getType();
        this.img= product.getImg();

    }
}
