package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, Long> {
    @Query(value = "SELECT * FROM invoices WHERE order_id = :orderId", nativeQuery = true)
    Optional<Invoices> findByOrderId(@Param("orderId") String orderId);
}