package com.example.da_sentrip.model.dto;

import com.example.da_sentrip.model.entity.Customer;
import com.example.da_sentrip.model.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {
    private String MaKH;
    private String fullName;
    private LocalDateTime dateTime;
    private String phone;
    private String address;
    private String img;
    public CustomerDTO (Customer customer){
        this.MaKH = customer.getMaKH();
        this.fullName=customer.getFullName();
        this.dateTime = customer.getDateTime();
        this.phone =customer.getPhone();
        this.address = customer.getAddress();
        this.img=customer.getImg();
    }


}
