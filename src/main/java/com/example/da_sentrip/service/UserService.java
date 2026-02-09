package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.reponse.LoginReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;
import com.example.da_sentrip.model.dto.request.LoginRequestDto;
import com.example.da_sentrip.model.dto.request.RegisterRequestDTO;
import com.example.da_sentrip.model.dto.request.UserRequestDTO;
import com.example.da_sentrip.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {
    LoginReponseDTO login (LoginRequestDto request);
    User register (RegisterRequestDTO request);
    User add(UserRequestDTO request);
    ProfileResponseDTO getProfile(String email);
    Map<String, Object> getMenu(String email, int page, int size);
}
