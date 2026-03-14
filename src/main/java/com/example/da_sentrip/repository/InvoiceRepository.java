package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.view.InvoiceDetailProjection;
import com.example.da_sentrip.model.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, Long> {
    @Query(value = "SELECT * FROM invoices WHERE order_id = :orderId", nativeQuery = true)
    Optional<Invoices> findByOrderId(@Param("orderId") String orderId);


    @Query(value = """
    SELECT
        i.ID AS invoiceId,
        i.INVOICE_CODE AS invoiceCode,
        i.MERCHANT_ID AS merchantId,
        i.AMOUNT AS amount,
        i.STATUS AS status,
        i.FILE_NAME AS fileName,
        i.GENRATED_AT AS generatedAt,
        i.file_url AS fileUrl,
        o.ORDER_CODE AS orderCode,
        o.ORDER_STATUS AS orderStatus,
        o.TOTAL_AMOUNT AS totalAmount,
        o.CREATED_AT AS orderCreatedAt,
        u.GMAIL AS gmail,
        c.FULL_NAME AS fullName
    FROM INVOICES i
    JOIN ORDERS o ON o.ID = i.ORDER_ID
    JOIN USERS u ON u.ID = o.USER_ID
    LEFT JOIN CUSTOMERS c ON c.USER_ID = u.ID
    """, nativeQuery = true)
    List<InvoiceDetailProjection> findAllInvoiceDetail();
}