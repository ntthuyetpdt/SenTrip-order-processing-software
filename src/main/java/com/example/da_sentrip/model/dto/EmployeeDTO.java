package com.example.da_sentrip.model.dto;

import com.example.da_sentrip.model.entity.Employee;
import com.example.da_sentrip.model.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private String mnv;
    private String fullName;
    private String phone;
    private String gender;
    private LocalDateTime dateTime;
    private String address;
    private LocalDateTime JoinDate;
    private String cccd;
    private String bankName;
    private String AccountBank;
    private String img;
    public EmployeeDTO(Employee employee){
        this.mnv =employee.getMnv();
        this.fullName =employee.getFullName();
        this.phone =employee.getPhone();
        this.gender =employee.getGender();
        this.dateTime =employee.getDateTime();
        this.address =employee.getAddress();
        this.JoinDate =employee.getJoinDate();
        this.cccd=employee.getCccd();
        this.bankName =employee.getBankName();
        this.AccountBank =employee.getAccountBank();
        this.img= employee.getImg();
    }


}
