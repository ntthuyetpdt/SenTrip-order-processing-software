package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Product;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductReponseDTO {
    private Long id;
    private String productName;
    private String serviceType;
    private String price;
    private Integer refundable;
    private Integer status;
    private LocalDateTime createdAt;
    private String Type;
    private String img;
    private String additionalService;
    private Long merchantId;

    public ProductReponseDTO (Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.serviceType = product.getServiceType();
        this.price= product.getPrice();
        this.refundable=product.getRefundable();
        this.status = product.getStatus();
        this.createdAt= product.getCreatedAt();
        this.Type= product.getType();
        this.img= product.getImg();
        this.additionalService =product.getAdditionalService();
        if (product.getMerchant() != null) {
            this.merchantId = product.getMerchant().getId();
        }
    }
}
