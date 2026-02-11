package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Customer;
import com.example.da_sentrip.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = """
        SELECT * FROM Customers E WHERE (:fullName IS NULL OR E.fullName LIKE CONCAT('%', :fullName, '%'))
          AND (:address IS NULL OR E.address LIKE CONCAT('%', :address, '%'))
          AND (:phone IS NULL OR E.phone LIKE CONCAT('%', :mnv, '%'))
        """, nativeQuery = true)
    List<Customer> findBySearch(
            @Param("fullName") String fullName,
            @Param("address") String address,
            @Param("phone") String phone
    );
}
