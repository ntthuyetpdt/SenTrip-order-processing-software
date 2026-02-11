package com.example.da_sentrip.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Setter
@Getter

public class EmployeeRequestDTO {
    private String mnv;
    private String fullName;
    private String phone;
    private String gender;
    private LocalDateTime dateTime;
    private String address;
    private LocalDateTime JoinDate;
    private String cccd;
    private String bankName;
    private String accountBank;

}
