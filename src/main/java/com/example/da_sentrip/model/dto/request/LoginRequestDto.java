package com.example.da_sentrip.model.dto.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class LoginRequestDto {
    private String gmail;
    private String password;
}
