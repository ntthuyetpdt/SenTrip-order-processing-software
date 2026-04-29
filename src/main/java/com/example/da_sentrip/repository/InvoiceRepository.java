package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.view.InvoiceDTO;
import com.example.da_sentrip.model.dto.reponse.view.InvoiceDetailProjection;
import com.example.da_sentrip.model.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query(value = """
        SELECT
            o.ORDER_CODE AS orderCode,
            i.INVOICE_CODE AS invoiceCode,
            i.AMOUNT AS amount,
            i.STATUS AS status,
            i.FILE_NAME AS fileName,
            i.GENRATED_AT AS generatedAt,
            i.file_url AS fileUrl
        FROM ORDERS o
        LEFT JOIN INVOICES i ON i.ORDER_ID = o.ID
                WHERE  i.ID IS NOT  NULL
                ORDER BY i.GENRATED_AT DESC 
        """, nativeQuery = true)
    List<InvoiceDTO> findAllInvoices();


    @Query(value = """
        SELECT
            o.ORDER_CODE AS orderCode,
            i.INVOICE_CODE AS invoiceCode,
            i.AMOUNT AS amount,
            i.STATUS AS status,
            i.FILE_NAME AS fileName,
            i.GENRATED_AT AS generatedAt,
            i.file_url AS fileUrl
        FROM ORDERS o
        LEFT JOIN INVOICES i ON i.ORDER_ID = o.ID
        WHERE
            (:invoiceCode IS NULL OR i.INVOICE_CODE LIKE CONCAT('%', :invoiceCode, '%'))
            AND (:orderCode IS NULL OR o.ORDER_CODE LIKE CONCAT('%', :orderCode, '%'))
            AND (:fromDate IS NULL OR i.GENRATED_AT >= :fromDate)
            AND (:toDate IS NULL OR i.GENRATED_AT <= :toDate)
        """, nativeQuery = true)
    List<InvoiceDTO> findInvoices(
            @Param("invoiceCode") String invoiceCode,
            @Param("orderCode") String orderCode,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query(value = "SELECT * FROM invoices WHERE ORDER_ID IN (SELECT ID FROM orders WHERE ORDER_CODE = :orderCode) ORDER BY GENRATED_AT DESC LIMIT 1",
            nativeQuery = true)
    Optional<Invoices> findByOrderCode(@Param("orderCode") String orderCode);
}