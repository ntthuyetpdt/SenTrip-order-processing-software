package com.example.da_sentrip.service;

import com.example.da_sentrip.model.entity.Employee;
import org.springframework.stereotype.Service;


@Service
public interface EmployeeService {
    Employee create ();
    Employee update ();
    Employee getdetailis();
    Employee srearch ();


}
