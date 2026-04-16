package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.model.dto.request.UpdateEmployees;
import com.example.da_sentrip.model.entity.Employee;
import com.example.da_sentrip.model.entity.Role;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.EmployeeRepository;
import com.example.da_sentrip.repository.RoleRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.lang.module.ResolutionException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DataSourceRepository dataSourceRepository;
    private final UserRepository userRepository;
    private final MediaStorageService mediaStorageService;
    private final ModelMapper modelMapper;
    private final RoleRepository repository;
     private final PasswordEncoder passwordEncoder;

    @Override
    public void updateRole(UpdateEmployees request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));

        if (request.getMnv() != null) employee.setMnv(request.getMnv());
        if (request.getJoinDate() != null) employee.setJoinDate(request.getJoinDate());
        if (request.getRole() != null) {
            Long roleId = Long.valueOf(request.getRole());
            if (!roleId.equals(2L) && !roleId.equals(3L))
                throw new IllegalArgumentException("Roles are only allowed to be 2 or 3.");
            Role role = repository.findById(roleId)
                    .orElseThrow(() -> new BadCredentialsException("Role not found"));
            user.setRole(role);
        }

        userRepository.save(user);
        employeeRepository.save(employee);
    }

    @Override
    public void update(UpdateEmployees request, String gmail, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));

        if (request.getMnv() != null) employee.setMnv(request.getMnv());
        if (request.getJoinDate() != null) employee.setJoinDate(request.getJoinDate());
        if (request.getRole() != null) {
            Role role = repository.findByRoleName(request.getRole())
                    .orElseThrow(() -> new BadCredentialsException("Role not found"));
            user.setRole(role);
        }

        userRepository.save(user);
        employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));
        if (emp.getImg() != null && emp.getImg().matches("\\d+")) {
            Long imgId = Long.parseLong(emp.getImg());
            dataSourceRepository.findById(imgId).ifPresent(d -> mediaStorageService.deleteMedia(imgId));
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDTO> getdetailis(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResolutionException("ID not found"));
        return Collections.singletonList(new EmployeeDTO(emp));
    }

    @Override
    public List<EmployeeDTO> search(String fullName, String address, String mnv) {
        return employeeRepository.findBySearch(fullName, address, mnv)
                .stream().map(EmployeeDTO::new).toList();
    }

    @Override
    public List<EmployeeReponseDTO> getAll() {
        return employeeRepository.findAll().stream().map(emp -> {
            User user = emp.getUser();
            EmployeeReponseDTO dto = new EmployeeReponseDTO(emp, user);
            if (user != null && user.getRole() != null)
                dto.setRole(user.getRole().getRoleName());
            if (emp.getImg() != null && emp.getImg().matches("\\d+"))
                dataSourceRepository.findById(Long.valueOf(emp.getImg()))
                        .ifPresent(ds -> dto.setImg(ds.getImageUrl()));
            return dto;
        }).toList();
    }
    @Override
    public void updateProfile(String gmail, EmployeeRequestDTO request, MultipartFile img) {
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Employee emp = employeeRepository.findByUser_Gmail(gmail).orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getFullName() != null) emp.setFullName(request.getFullName());
        if (request.getPhone() != null) emp.setPhone(request.getPhone());
        if (request.getGender() != null) emp.setGender(request.getGender());
        if(request.getDateTime()!=null) emp.setDateTime(request.getDateTime());
        if (request.getAddress() != null) emp.setAddress(request.getAddress());
        if (request.getCccd()!=null) emp.setCccd(request.getCccd());
        if (request.getBankName() != null) emp.setBankName(request.getBankName());
        if (request.getAccountBank() != null) emp.setAccountBank(request.getAccountBank());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }

        if (img != null && !img.isEmpty()) {
            if (emp.getImg() != null && emp.getImg().matches("\\d+"))
                mediaStorageService.deleteMedia(Long.valueOf(emp.getImg()));
            emp.setImg(mediaStorageService.uploadMedia(img));
        }

        employeeRepository.save(emp);
    }

}