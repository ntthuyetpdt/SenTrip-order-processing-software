package com.example.da_sentrip.repository.custom;


import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;

public interface UserCustomRepository {
    ProfileResponseDTO getProfileByEmail(String email);
}
