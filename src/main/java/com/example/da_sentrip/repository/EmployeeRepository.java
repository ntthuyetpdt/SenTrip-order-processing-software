package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
        SELECT * FROM employees E WHERE (:fullName IS NULL OR E.full_name LIKE CONCAT('%', :fullName, '%'))
          AND (:address IS NULL OR E.address LIKE CONCAT('%', :address, '%'))
          AND (:mnv IS NULL OR E.mnv LIKE CONCAT('%', :mnv, '%'))
        """, nativeQuery = true)
    List<Employee> findBySearch(
            @Param("fullName") String fullName,
            @Param("address") String address,
            @Param("mnv") String mnv
    );
}