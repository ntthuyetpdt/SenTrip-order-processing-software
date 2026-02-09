package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.model.entity.Employee;
import com.example.da_sentrip.repository.EmployeeRepository;
import com.example.da_sentrip.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;


    @Override
    public Employee create(Long id,EmployeeRequestDTO request) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee emp = new Employee();
        emp.setMnv(request.getMnv());
        emp.setFullName(request.getFullName());
        emp.setPhone(request.getPhone());
        emp.setGender(request.getGender());
        emp.setDateTime(request.getDateTime());
        emp.setAddress(request.getAddress());
        emp.setJoinDate(request.getJoinDate());
        emp.setCccd(request.getCccd());
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Long id,EmployeeRequestDTO request) {
        Employee employee=employeeRepository.findById(id).orElseThrow(()-> new BadCredentialsException("user not found"));
        if (request.getFullName() != null && request.getFullName().isBlank())employee.setFullName(request.getFullName());

    }

    @Override
    public Employee getdetailis() {
        return null;
    }

    @Override
    public Employee srearch() {
        return null;
    }
}
