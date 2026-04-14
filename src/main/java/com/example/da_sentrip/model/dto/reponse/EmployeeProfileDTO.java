package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class EmployeeProfileDTO {
    private String gmail;
    private String role;
    private List<String> permissions;
    private String mnv;
    private String fullName;
    private String phone;
    private String gender;
    private LocalDate dateTime;
    private String address;
    private LocalDateTime joinDate;
    private String cccd;
    private String bankName;
    private String accountBank;
    private String img;
}