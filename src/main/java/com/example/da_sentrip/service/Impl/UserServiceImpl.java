package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.reponse.LoginReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;
import com.example.da_sentrip.model.dto.request.LoginRequestDto;
import com.example.da_sentrip.model.dto.request.RegisterRequestDTO;
import com.example.da_sentrip.model.dto.request.UserRequestDTO;
import com.example.da_sentrip.model.entity.Role;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.model.enums.Status;
import com.example.da_sentrip.repository.RoleRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.repository.custom.MenuCustomRepository;
import com.example.da_sentrip.repository.custom.UserCustomRepository;
import com.example.da_sentrip.security.JwtUtil;
import com.example.da_sentrip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final UserCustomRepository userCustomRepository;
    private final MenuCustomRepository menuCustomRepository;

    @Override
    public LoginReponseDTO login(LoginRequestDto request) {
        User user = userRepository.findByGmail(request.getGmail())
                .orElseThrow(() -> new BadCredentialsException("Gmail is incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }

        if (user.getRole() == null) {
            throw new BadCredentialsException("User role is not assigned");
        }

        String token = jwtUtil.generateToken(user);
        return new LoginReponseDTO(token);
    }

    @Override
    public User register(RegisterRequestDTO request) {
        if (userRepository.findByGmail(request.getGmail()).isPresent()) {
            throw new BadCredentialsException("Gmail already exists");
        }

        Role customerRole = roleRepository.findById(4L).orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));
        User user = new User();
        user.setGmail(request.getGmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole(customerRole);
        return userRepository.save(user);
    }

    @Override
    public User add(UserRequestDTO request) {
        if (userRepository.findByGmail(request.getGmail()).isPresent()) {
            throw new BadCredentialsException("Gmail already exists");
        }

        User userAdd = new User();
        userAdd.setGmail(request.getGmail());
        userAdd.setPassword(passwordEncoder.encode("Ab123456@"));
        userAdd.setStatus(Status.ACTIVE);
        return userRepository.save(userAdd);
    }

    @Override
    public ProfileResponseDTO getProfile(String email) {
        return userCustomRepository.getProfileByEmail(email);
    }

    @Override
    public Map<String, Object> getMenu(String email, int page, int size) {
        return menuCustomRepository.getMenuByEmail(email, page, size);
    }
}