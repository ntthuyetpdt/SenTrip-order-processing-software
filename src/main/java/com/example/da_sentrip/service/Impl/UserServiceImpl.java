package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.LoginReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;
import com.example.da_sentrip.model.dto.request.LoginRequestDto;
import com.example.da_sentrip.model.dto.request.RegisterRequestDTO;
import com.example.da_sentrip.model.dto.request.UserRequestDTO;
import com.example.da_sentrip.model.entity.*;
import com.example.da_sentrip.model.enums.Status;
import com.example.da_sentrip.repository.*;
import com.example.da_sentrip.repository.custom.MenuCustomRepository;
import com.example.da_sentrip.repository.custom.UserCustomRepository;
import com.example.da_sentrip.security.JwtUtil;
import com.example.da_sentrip.service.MerchantService;
import com.example.da_sentrip.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public LoginReponseDTO login(LoginRequestDto request) {
        User user = userRepository.findByGmail(request.getGmail()).orElseThrow(() -> new BadCredentialsException("Gmail is incorrect"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }
        if (user.getRole() == null) {
            throw new BadCredentialsException("User role is not assigned");
        }
        String token = jwtUtil.generateToken(user);
        return new LoginReponseDTO(token);
    }
    @Transactional
    @Override
    public User register(RegisterRequestDTO request) {
        if (userRepository.findByGmail(request.getGmail()).isPresent()) {
            throw new BadCredentialsException("Gmail already exists");
        }

        Long roleId = request.getRole();
        if (roleId == null || (!roleId.equals(4L) && !roleId.equals(5L))) {
            throw new IllegalArgumentException("Chỉ cho phép đăng ký CUSTOMER(4) hoặc MERCHANT(5)");
        }

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        User user = new User();
        user.setGmail(request.getGmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole(role);
        User savedUser = userRepository.save(user);
        if (roleId.equals(4L)) {
            Customer customer = new Customer();
            customer.setUser(savedUser);
            customerRepository.save(customer);
        }
        if (roleId.equals(5L)) {
            Merchant merchant = new Merchant();
            merchant.setUser(savedUser);
            merchantRepository.save(merchant);
        }

        return savedUser;
    }

    @Transactional
    @Override
    public User add(UserRequestDTO request) {

        if (userRepository.findByGmail(request.getGmail()).isPresent()) {
            throw new BadCredentialsException("Gmail already exists");
        }
        Long roleId = Long.valueOf(request.getRole());
        if (!roleId.equals(2L) && !roleId.equals(3L)) {
            throw new IllegalArgumentException("Role chỉ được phép là 2 hoặc 3");
        }
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        User userAdd = new User();
        userAdd.setGmail(request.getGmail());
        userAdd.setPassword(passwordEncoder.encode("Ab123456@"));
        userAdd.setRole(role);
        userAdd.setStatus(Status.ACTIVE);
        User savedUser = userRepository.save(userAdd);
        if (roleId.equals(2L) || roleId.equals(3L)) {
            Employee employee = new Employee();
            employee.setUser(savedUser);
            employeeRepository.save(employee);
        }
        return savedUser;
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