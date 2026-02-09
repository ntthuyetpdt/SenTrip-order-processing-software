package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.entity.Employee;
import com.example.da_sentrip.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> finBySerch (String fullName, String address, String  mnv);
}
