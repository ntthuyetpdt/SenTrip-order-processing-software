package com.example.da_sentrip.model.dto.reponse;


import com.example.da_sentrip.model.entity.Customer;
import com.example.da_sentrip.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class CustomerReponseDTO {
    private String fullName;
    private String gmail;
    private String phone;
    private String address;
    private String img;
    public CustomerReponseDTO(Customer customer, User user){
        this.fullName = customer.getFullName();
        this.gmail = user.getGmail();
        this.phone= customer.getPhone();
        this.address = customer.getAddress();
        this.img=customer.getImg();

    }
}
