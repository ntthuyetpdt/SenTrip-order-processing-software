package com.example.da_sentrip.controller;

import com.example.da_sentrip.service.UserService;
import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.UserRequestDTO;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.security.JwtUtil;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> add(@RequestBody UserRequestDTO request){

            User user =userService.add( request);
        return ResponseEntity.ok("add user success");

    }
    @GetMapping("/profile")
    public SuccessResponse<ProfileResponseDTO> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getGmailFromToken(token);
        return new SuccessResponse<>(
                200,
                "Get profile success",
                userService.getProfile(email)
        );
    }

    @PostMapping("/cread")
    @GetMapping("/menu")
    public SuccessResponse<?> getMenu(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getGmailFromToken(token);
        return new SuccessResponse<>(
                200,
                "Get menu success",
                userService.getMenu(email, page, size)
        );
    }


}
