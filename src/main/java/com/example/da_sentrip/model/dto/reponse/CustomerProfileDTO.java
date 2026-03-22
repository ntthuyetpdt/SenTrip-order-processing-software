package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomerProfileDTO {
    private String gmail;
    private String role;
    private List<String> permissions;
    private String cccd;
    private String fullName;
    private LocalDateTime dateTime;
    private String phone;
    private String address;
    private String img;

}