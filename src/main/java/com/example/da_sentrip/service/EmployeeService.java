package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.model.entity.Employee;
import org.springframework.stereotype.Service;


@Service
public interface EmployeeService {
    Employee create (Long id,EmployeeRequestDTO request);
    Employee update (Long id,EmployeeRequestDTO request);
    Employee getdetailis();
    Employee srearch ();


}
