package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.model.entity.DataSource;
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
import java.lang.module.ResolutionException;
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
        User user= userRepository.findById(id).orElseThrow(()->new BadCredentialsException("ID not found"));
        Employee emp = new Employee();
        emp.setMnv(request.getMnv());
        emp.setFullName(request.getFullName());
        emp.setPhone(request.getPhone());
        emp.setGender(request.getGender());
        emp.setDateTime(request.getDateTime());
        emp.setAddress(request.getAddress());
        emp.setJoinDate(request.getJoinDate());
        emp.setCccd(request.getCccd());
        return new EmployeeDTO(emp);
    }
    @Override
    public EmployeeDTO update(Long id, EmployeeRequestDTO request) {
        return employeeRepository.findById(id).map(emp -> {
                    modelMapper.map(request, emp);
                    return new EmployeeDTO(employeeRepository.save(emp));
                }).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public EmployeeDTO delete(Long id) {
        Employee employee=employeeRepository.findById(id).orElseThrow(() ->new BadCredentialsException("ID not found"));
        if (employee.getImg() != null && !employee.getImg().isBlank()){
            Long avatarIdb =Long.parseLong(employee.getImg());
            DataSource data=dataSourceRepository.findById(avatarIdb).orElseThrow(null);
            if (data !=null){
                mediaStorageService.deleteMedia(avatarIdb);
            }
        }
        return new EmployeeDTO(employee);

    }


    @Override
    public EmployeeDTO getdetailis(Long id) {
        Employee employee =employeeRepository.findById(id).orElseThrow(() ->new ResolutionException("ID not found"));
        EmployeeDTO dto = new EmployeeDTO(employee);
        if (employee.getImg() != null && employee.getImg().matches("\\d+")) {
            Long dataSourceId = Long.parseLong(employee.getImg());
            dataSourceRepository.findById(dataSourceId).ifPresent(ds -> {
                dto.setImg(ds.getImageUrl());
            });
        }
        return dto;
    }

    @Override
    public List<EmployeeDTO> search(String fullName, String address, String mnv) {
        List<Employee> employees = employeeRepository.finBySerch(fullName, address, mnv);
        return employees.stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }


}
