package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.reponse.LoginReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.LoginRequestDto;
import com.example.da_sentrip.model.dto.request.RegisterRequestDTO;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.security.JwtUtil;
import com.example.da_sentrip.service.UserService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public SuccessResponse<LoginReponseDTO> login(@RequestBody LoginRequestDto request) {
        LoginReponseDTO user = userService.login(request);
        return new SuccessResponse<>(
                200,
                "Login success",
                user
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request){
        try {
            User user =userService.register(request);
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("Account registration successful.")
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.builder()
                            .status("error")
                            .code(Constants.HTTP_STATUS.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .message(e.getMessage())
                            .build());
        }
    }


}
