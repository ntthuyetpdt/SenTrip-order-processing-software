package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderDTO {
    private Long id;
    private String gmail;

    public UserOrderDTO(User user) {
        this.id = user.getId();
        this.gmail = user.getGmail();
    }
}