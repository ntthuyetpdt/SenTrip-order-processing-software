package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        INSERT INTO PAYMENTS (PAYMENT_CODE, ORDER_ID, AMOUNT, PAYMENT_STATUS, BANK_NAME, BANK_ACCOUNT)
        VALUES (:paymentCode, :orderId, :amount, 'CONFIRM', :bankName, :bankAccount)
        """, nativeQuery = true)
    int insertPending(@Param("paymentCode") String paymentCode,
                      @Param("orderId") Long orderId,
                      @Param("amount") BigDecimal amount,
                      @Param("bankName") String bankName,
                      @Param("bankAccount") String bankAccount);

    @Query(value = """ 
        SELECT P.ID 
        FROM PAYMENTS P 
        WHERE P.PAYMENT_CODE = :paymentCode 
        LIMIT 1
        """, nativeQuery = true)
    Long findIdByPaymentCode(@Param("paymentCode") String paymentCode);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE PAYMENTS
        SET PAYMENT_STATUS = 'SUCCESS',
            PROCESSED_BY   = :employeeId,
            PAID_AT        = NOW()
        WHERE ID = :paymentId
        """, nativeQuery = true)
    int markSuccess(@Param("paymentId") Long paymentId,
                    @Param("employeeId") Long employeeId);

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
}