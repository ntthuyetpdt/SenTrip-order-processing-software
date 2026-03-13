package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO PAYMENTS (PAYMENT_CODE, ORDER_ID, AMOUNT, PAYMENT_STATUS)
    VALUES (:paymentCode, :orderId, :amount, 'WAITING_FOR_PAYMENT')
""", nativeQuery = true)
    void insertWaiting(@Param("paymentCode") String paymentCode, @Param("orderId") Long orderId, @Param("amount") BigDecimal amount);

    @Query(value = """
    SELECT p.ID
    FROM PAYMENTS p
    WHERE p.PAYMENT_CODE = :paymentCode
    """, nativeQuery = true)
    Optional<Long> findIdByPaymentCode(@Param("paymentCode") String paymentCode);

    @Modifying
    @Query(value = """
    UPDATE PAYMENTS
    SET PAYMENT_STATUS = 'SUCCESS',
    PAID_AT = NOW()
    WHERE ID = :paymentId
    """, nativeQuery = true)
    void markSuccess(@Param("paymentId") Long paymentId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    INSERT INTO PAYMENT_AUDIT_LOGS 
    (PAYMENT_ID, ACTION, OLD_STATUS, NEW_STATUS, OLD_AMOUNT, NEW_AMOUNT, PERFORMED_BY, PERFORMED_AT)
    VALUES 
    (:paymentId, :action, :oldStatus, :newStatus, :oldAmount, :newAmount, :performedBy, NOW())
    """, nativeQuery = true)
    int insertLog(@Param("paymentId") Long paymentId,
                  @Param("action") String action,
                  @Param("oldStatus") String oldStatus,
                  @Param("newStatus") String newStatus,
                  @Param("oldAmount") BigDecimal oldAmount,
                  @Param("newAmount") BigDecimal newAmount,
                  @Param("performedBy") Long performedBy);


    @Query(value = """
    SELECT p.ID AS id,
           p.PAYMENT_CODE AS paymentCode,
           p.ORDER_ID AS orderId,
           p.AMOUNT AS amount,
           p.PAYMENT_STATUS AS paymentStatus
    FROM PAYMENTS p
    WHERE p.PAYMENT_CODE = :paymentCode
    LIMIT 1
    FOR UPDATE
""", nativeQuery = true)
    Optional<PaymentSnapshot> lockByPaymentCode(@Param("paymentCode") String paymentCode);

    interface PaymentSnapshot {
        Long getId();
        String getPaymentCode();
        Long getOrderId();
        BigDecimal getAmount();
        String getPaymentStatus();
    }
}