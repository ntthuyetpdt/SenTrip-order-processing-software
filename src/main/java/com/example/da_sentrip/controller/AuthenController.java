package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.reponse.LoginReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.LoginRequestDto;
import com.example.da_sentrip.model.dto.request.RegisterRequestDTO;
import com.example.da_sentrip.service.UserService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Login success")
                .data(Map.of("token", userService.login(request).getToken()))
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.CREATED)
                .message("Account registration successful")
                .build());
    }
}
