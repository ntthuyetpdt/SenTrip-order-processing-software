package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileResponseDTO {
    private String gmail;
    private String role;
    private  String name;
    private String img;
    private List<String> permissions;

}