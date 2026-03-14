package com.example.da_sentrip.controller;

import com.example.da_sentrip.service.UserService;

import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.UserRequestDTO;
import com.example.da_sentrip.security.JwtUtil;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ResponseDTO> add(@RequestBody UserRequestDTO request) {
        userService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.CREATED)
                .message("Create success").build());
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO> getProfile(Authentication authentication) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get profile success")
                .data(userService.getProfile(authentication.getName())).build());
    }

    @GetMapping("/menu")
    public ResponseEntity<ResponseDTO> getMenu(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get menu success")
                .data(userService.getMenu(authentication.getName(), page, size)).build());
    }
}