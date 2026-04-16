package com.example.da_sentrip.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateRoleDTO {
    private String role;
    private String mnv;
    private LocalDateTime JoinDate;
}
