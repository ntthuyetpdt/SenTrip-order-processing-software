package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReponseDTO {
    private Long id;
    private String productName;
    private String serviceType;
    private BigDecimal price;
    private Integer refundable;
    private Integer status;
    private String type;
    private String img;
    private String additionalService;
    private Long merchantId;
    private String address;

    public ProductReponseDTO (Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.serviceType = product.getServiceType();
        this.price= product.getPrice();
        this.refundable=product.getRefundable();
        this.status = product.getStatus();
        this.type= product.getType();
        this.img= product.getImg();
        this.address =product.getAddress();
        this.additionalService =product.getAdditionalService();
        if (product.getMerchant() != null) {
            this.merchantId = product.getMerchant().getId();
        }
    }
}
