package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.OderdetailReponseDTO;
import com.example.da_sentrip.model.dto.reponse.view.GetallOder;
import com.example.da_sentrip.model.dto.reponse.view.OderDetailProjection;
import com.example.da_sentrip.model.entity.Order;
import com.example.da_sentrip.model.dto.reponse.view.OrderSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM ORDERS WHERE ORDER_CODE = :orderCode", nativeQuery = true)
    Optional<Order> findByOrderCode(@Param("orderCode") String orderCode);

    @Query(value = """
            SELECT
            o.CREATED_AT AS createdAt,
            o.ORDER_CODE AS orderCode,
            o.ORDER_STATUS AS orderStatus,
            o.TOTAL_AMOUNT AS totalAmount,
            u.ID AS userId,
            u.GMAIL AS gmail,
            c.FULL_NAME AS fullName,
            c.PHONE AS phone,
            c.CCCD AS cccd,
            pay.PAYMENT_STATUS AS paymentStatus,
            pay.PAYMENT_CODE AS paymentCode,
            pay.PAID_AT AS paidAt,
            GROUP_CONCAT(p.PRODUCT_NAME SEPARATOR ', ') AS productNames,
            GROUP_CONCAT(p.ADDITIONAL_SERVICES SEPARATOR ', ') AS additionalService,
            GROUP_CONCAT(p.SERVICE_TYPE SEPARATOR ',') AS  serviceType,
            GROUP_CONCAT(p.TYPE SEPARATOR ',') AS Type,
            GROUP_CONCAT(oi.QUANTITY ORDER BY oi.ID SEPARATOR ', ') AS quantities,
            GROUP_CONCAT( DISTINCT CASE  WHEN p.IMG IS NOT NULL AND p.IMG REGEXP '^[0-9]+$'  THEN ds.MEDIA_URL ELSE p.IMG END SEPARATOR ', ' ) AS img
        FROM ORDERS o
                 JOIN USERS u ON u.ID = o.USER_ID
                 LEFT JOIN CUSTOMERS c ON c.USER_ID = u.ID   
                 JOIN ORDER_ITEMS oi ON oi.ORDER_ID = o.ID
                 JOIN PRODUCTS p ON p.ID = oi.PRODUCT_ID
                 LEFT JOIN PAYMENTS pay ON pay.ORDER_ID = o.ID
                 LEFT JOIN DATA_SOUSES ds ON (  p.IMG IS NOT NULL  AND p.IMG REGEXP '^[0-9]+$' AND ds.ID = CAST(p.IMG AS UNSIGNED)  )
        WHERE o.ORDER_CODE = :orderCode
        GROUP BY
            o.CREATED_AT, o.ORDER_CODE, o.ORDER_STATUS, o.TOTAL_AMOUNT,
            u.ID, u.GMAIL,
            c.FULL_NAME, c.PHONE, c.CCCD,
            pay.PAYMENT_STATUS, pay.PAYMENT_CODE, pay.PAID_AT
        
        ORDER BY o.CREATED_AT DESC;
        """, nativeQuery = true)
    List<OderDetailProjection> findOrderDetailByOrderCode(String orderCode);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY, PRICE)
        VALUES (:orderId, :productId, :quantity, :price)
        """, nativeQuery = true)
    void insertOrderItem(
            @Param("orderId") Long orderId,
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity,
            @Param("price") String price
    );

    @Modifying
    @Transactional
    @Query(value = """
        DELETE OI FROM ORDER_ITEMS OI
        JOIN ORDERS O ON O.ID = OI.ORDER_ID
        WHERE O.ORDER_CODE = :orderCode
        """, nativeQuery = true)
    void deleteOrderItemsByOrderCode(@Param("orderCode") String orderCode);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ORDERS WHERE ORDER_CODE = :orderCode", nativeQuery = true)
    void deleteOrderByOrderCode(@Param("orderCode") String orderCode);

    @Query(value = "SELECT * FROM ORDERS WHERE USER_ID = :userId", nativeQuery = true)
    List<Order> findAllByUserId(@Param("userId") Long userId);

    @Query(value = """
    SELECT
        o.CREATED_AT AS createdAt,
        o.ORDER_CODE AS orderCode,
        o.ORDER_STATUS AS orderStatus,
        o.TOTAL_AMOUNT AS totalAmount,
        u.ID AS userId,
        u.GMAIL AS gmail,
        GROUP_CONCAT(p.PRODUCT_NAME SEPARATOR ', ') AS productNames,
        GROUP_CONCAT(p.ADDITIONAL_SERVICES SEPARATOR ', ') AS additionalService,
        GROUP_CONCAT(oi.QUANTITY ORDER BY oi.ID SEPARATOR ', ') AS quantities,
        GROUP_CONCAT(DISTINCT CASE WHEN p.IMG IS NOT NULL AND p.IMG REGEXP '^[0-9]+$'
        THEN ds.MEDIA_URL ELSE p.IMG END SEPARATOR ', ') AS img
        FROM ORDERS o
        JOIN USERS u ON u.ID = o.USER_ID
        JOIN ORDER_ITEMS oi ON oi.ORDER_ID = o.ID
        JOIN PRODUCTS p ON p.ID = oi.PRODUCT_ID
        LEFT JOIN DATA_SOUSES ds
        ON (p.IMG IS NOT NULL AND p.IMG REGEXP '^[0-9]+$'
        AND ds.ID = CAST(p.IMG AS UNSIGNED))
        WHERE o.USER_ID = :userId
        GROUP BY o.CREATED_AT, o.ORDER_CODE, o.ORDER_STATUS, o.TOTAL_AMOUNT
        ORDER BY o.CREATED_AT DESC
""", nativeQuery = true)
    List<OrderSummaryView> findOrderSummaryByUser(@Param("userId") Long userId);

    interface OrderPaySnapshot{
        Long getId();
        BigDecimal getTotalAmount();
        String getOrderStatus();
    }
    @Query(value = """
        SELECT O.ID AS id,
               O.TOTAL_AMOUNT AS totalAmount,
               O.ORDER_STATUS AS orderStatus
        FROM ORDERS O
        WHERE O.ORDER_CODE = :orderCode
        FOR UPDATE
    """, nativeQuery = true)
    Optional<OrderPaySnapshot> lockByOrderCode(@Param("orderCode") String orderCode);

    @Query(value = """
    SELECT
        o.CREATED_AT AS createdAt,
        o.ORDER_CODE AS orderCode,
        o.ORDER_STATUS AS orderStatus,
        o.TOTAL_AMOUNT AS totalAmount,
        u.ID AS userId,
        u.GMAIL AS gmail,
        GROUP_CONCAT(p.PRODUCT_NAME ORDER BY oi.ID SEPARATOR ', ') AS productNames,
        GROUP_CONCAT(p.ADDITIONAL_SERVICES ORDER BY oi.ID SEPARATOR ', ') AS additionalService,
        GROUP_CONCAT(oi.QUANTITY ORDER BY oi.ID SEPARATOR ', ') AS quantities,
        GROUP_CONCAT( DISTINCT CASE WHEN p.IMG IS NOT NULL AND p.IMG REGEXP '^[0-9]+$' THEN ds.MEDIA_URL ELSE p.IMG END ORDER BY oi.ID SEPARATOR ', ' ) AS img
    FROM ORDERS o
    JOIN USERS u ON u.ID = o.USER_ID
    JOIN ORDER_ITEMS oi ON oi.ORDER_ID = o.ID
    JOIN PRODUCTS p ON p.ID = oi.PRODUCT_ID
    LEFT JOIN DATA_SOUSES ds
        ON (
            p.IMG IS NOT NULL
            AND p.IMG REGEXP '^[0-9]+$'
            AND ds.ID = CAST(p.IMG AS UNSIGNED)
        )
    GROUP BY
        o.ID, o.CREATED_AT, o.ORDER_CODE, o.ORDER_STATUS, o.TOTAL_AMOUNT,
        u.ID, u.GMAIL
    ORDER BY o.CREATED_AT DESC
""", nativeQuery = true)
    List<GetallOder> findAllOrderSummary();


    @Query(value = """
    SELECT EXISTS(
    SELECT 1
    FROM PAYMENTS p
    WHERE p.ORDER_ID = :orderId
    AND p.PAYMENT_STATUS = 'SUCCESS'
    )
""", nativeQuery = true)
    int existsPaidSuccess(@Param("orderId") Long orderId);


    @Query(value = """
    SELECT EXISTS(
    SELECT 1
    FROM INVOICES i
    WHERE i.ORDER_ID = :orderId
    AND i.STATUS = 'SUCCESS'
    )""", nativeQuery = true)
    int existsInvoiceSuccess(@Param("orderId") Long orderId);


    @Modifying
    @Query(value = """
    INSERT INTO INVOICES (INVOICE_CODE, ORDER_ID, MERCHANT_ID, AMOUNT, STATUS, CREATED_AT)
    VALUES (:invoiceCode, :orderId, :merchantId, :amount, :status, NOW())
""", nativeQuery = true)
    void insertInvoice(
            @Param("invoiceCode") String invoiceCode,
            @Param("orderId") Long orderId,
            @Param("merchantId") Long merchantId,
            @Param("amount") BigDecimal amount,
            @Param("status") String status
    );


    @Query(value = """
    SELECT
        i.ID AS id,
        i.INVOICE_CODE AS invoiceCode,
        i.ORDER_ID AS orderId,
        i.MERCHANT_ID AS merchantId,
        i.AMOUNT AS amount,
        i.STATUS AS status,
        i.CREATED_AT AS createdAt
    FROM INVOICES i
    WHERE i.ORDER_ID = :orderId
    ORDER BY i.ID DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<InvoiceProjection> findLatestByOrderId(@Param("orderId") Long orderId);

    interface InvoiceProjection {
        Long getId();
        String getInvoiceCode();
        Long getOrderId();
        Long getMerchantId();
        BigDecimal getAmount();
        String getStatus();
        LocalDateTime getCreatedAt();
    }


}