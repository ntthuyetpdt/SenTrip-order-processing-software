package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Employee;
import com.example.da_sentrip.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class EmployeeReponseDTO {
    private String mnv;
    private String fullName;
    private String Role;
    private String phone;
    private String gender;
    private String address;
    private LocalDateTime JoinDate;
    private String cccd;
    private String bankName;
    private String accountBank;
    private String img;
    public EmployeeReponseDTO(Employee employee , User user){
        this.mnv =employee.getMnv();
        this.fullName =employee.getFullName();
        this.Role = String.valueOf(user.getRole());
        this.phone =employee.getPhone();
        this.gender =employee.getGender();
        this.address =employee.getAddress();
        this.JoinDate =employee.getJoinDate();
        this.cccd=employee.getCccd();
        this.bankName =employee.getBankName();
        this.accountBank=employee.getAccountBank();
        this.img= employee.getImg();
    }

}
