package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.model.entity.Employee;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.EmployeeRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.lang.module.ResolutionException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DataSourceRepository dataSourceRepository;
    private final UserRepository userRepository;
    private final MediaStorageService mediaStorageService;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDTO create(EmployeeRequestDTO request, Long id) {
        userRepository.findById(id).orElseThrow(() -> new BadCredentialsException("ID not found"));
        return new EmployeeDTO(modelMapper.map(request, Employee.class));
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeRequestDTO request, MultipartFile img) {
        Employee emp = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        modelMapper.map(request, emp);
        if (img != null && !img.isEmpty()) {
            if (emp.getImg() != null && emp.getImg().matches("\\d+")) {
                mediaStorageService.deleteMedia(Long.valueOf(emp.getImg()));
            }
            String newDsId = mediaStorageService.uploadMedia(img);
            emp.setImg(newDsId);
        }
        employeeRepository.save(emp);
        return new EmployeeDTO(emp);
    }

    @Override
    public EmployeeDTO delete(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new BadCredentialsException("ID not found"));
        if (employee.getImg() != null && !employee.getImg().isBlank()) {
            Long imgId = Long.parseLong(employee.getImg());
            dataSourceRepository.findById(imgId).ifPresent(data -> mediaStorageService.deleteMedia(imgId));
        }
        return new EmployeeDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getdetailis(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResolutionException("ID not found"));
        return Collections.singletonList(new EmployeeDTO(employee));
    }

    @Override
    public List<EmployeeDTO> search(String fullName, String address, String mnv) {
        return employeeRepository.findBySearch(fullName, address, mnv).stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeReponseDTO> getAll() {
        return employeeRepository.findAll().stream().map(employee -> {
            User user = employee.getUser();
            EmployeeReponseDTO dto = new EmployeeReponseDTO(employee,user);
            if (user != null && user.getRole() != null) {
                dto.setRole(user.getRole().getRoleName());
            }
                    if (employee.getImg() != null && employee.getImg().matches("\\d+")) {
                        dataSourceRepository.findById(Long.valueOf(employee.getImg())).ifPresent(ds -> {
                        dto.setImg(ds.getImageUrl());
                        });
                    }
                    return dto;
                }).toList();
    }
}