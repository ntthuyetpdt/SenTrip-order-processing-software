package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.view.TicketView;
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

    @Query(value = """
            SELECT
        C.FULL_NAME AS fullName,
        C.PHONE AS phone,
        C.CCCD AS cccd,
        U.GMAIL AS gmail,
        GROUP_CONCAT(DISTINCT PR.PRODUCT_NAME ORDER BY PR.PRODUCT_NAME SEPARATOR ', ') AS productName,
        GROUP_CONCAT(OI.QUANTITY ORDER BY OI.ID SEPARATOR ', ') AS quantities,
        GROUP_CONCAT(DISTINCT CASE WHEN PR.IMG IS NOT NULL AND PR.IMG REGEXP '^[0-9]+$' THEN DS.MEDIA_URL ELSE PR.IMG  END  ORDER BY PR.PRODUCT_NAME SEPARATOR ', ' ) AS img,
        MAX(PR.SERVICE_TYPE) AS serviceType,
        MAX(PR.TYPE) AS type,
        O.CREATED_AT AS createTime,
        COALESCE(PAY.PAYMENT_STATUS, O.ORDER_STATUS) AS status,
        O.TOTAL_AMOUNT AS totalAmount,
        PAY.PAID_AT AS paidAt,
        PAY.PAYMENT_CODE AS paymentCode
    
    FROM ORDERS O
             JOIN CUSTOMERS C ON C.USER_ID = O.USER_ID
             JOIN USERS U ON U.ID = O.USER_ID
             LEFT JOIN ORDER_ITEMS OI ON OI.ORDER_ID = O.ID
             LEFT JOIN PRODUCTS PR ON PR.ID = OI.PRODUCT_ID
    
             LEFT JOIN DATA_SOUSES DS
                       ON (
                           PR.IMG IS NOT NULL
                               AND PR.IMG REGEXP '^[0-9]+$'
                               AND DS.ID = CAST(PR.IMG AS UNSIGNED)
                           )
    
             LEFT JOIN PAYMENTS PAY ON PAY.ID = (
        SELECT P2.ID
        FROM PAYMENTS P2
        WHERE P2.ORDER_ID = O.ID
        ORDER BY P2.PAID_AT DESC
        LIMIT 1
    )
    
    WHERE O.USER_ID = :userId
    GROUP BY
        O.ID,
        C.FULL_NAME, C.PHONE, C.CCCD, U.GMAIL,
        O.CREATED_AT,
        COALESCE(PAY.PAYMENT_STATUS, O.ORDER_STATUS),
        O.TOTAL_AMOUNT,
        PAY.PAID_AT,
        PAY.PAYMENT_CODE
    ORDER BY O.CREATED_AT DESC;
    """, nativeQuery = true)
    List<TicketView> findTicketsByUserId(@Param("userId") Long userId);
}
