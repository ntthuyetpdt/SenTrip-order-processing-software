package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.view.*;
import com.example.da_sentrip.model.entity.Order;
import com.example.da_sentrip.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
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
        o.ORDER_CODE AS orderCode,
        c.FULL_NAME AS fullNameCustomer,
        o.CREATED_AT AS createdAt,
        GROUP_CONCAT(DISTINCT p.SERVICE_TYPE ORDER BY oi.ID SEPARATOR ', ') AS serviceType,
        GROUP_CONCAT(DISTINCT p.ADDITIONAL_SERVICES ORDER BY oi.ID SEPARATOR ', ') AS additionalService,
        GROUP_CONCAT(DISTINCT p.ADDRESS ORDER BY oi.ID SEPARATOR ', ') AS address,
        o.TOTAL_AMOUNT AS totalAmount,
        o.ORDER_STATUS AS orderStatus,
        pm.PAYMENT_STATUS AS paymentStatus
    FROM ORDERS o
             JOIN customers c
                  ON c.USER_ID = o.USER_ID
             JOIN ORDER_ITEMS oi
                  ON oi.ORDER_ID = o.ID
             JOIN PRODUCTS p
                  ON p.ID = oi.PRODUCT_ID
             LEFT JOIN PAYMENTS pm
                       ON pm.ORDER_ID = o.ID
    GROUP BY
        o.ID,
        o.ORDER_CODE,
        c.FULL_NAME,
        o.CREATED_AT,
        o.TOTAL_AMOUNT,
        o.ORDER_STATUS,
        pm.PAYMENT_STATUS
    ORDER BY o.CREATED_AT DESC;
""", nativeQuery = true)
    List<GetallOder> findAllOrderSummary();

    @Query(value = """
    SELECT
        o.ORDER_CODE AS orderCode,
        c.FULL_NAME AS fullNameCustomer,
        o.CREATED_AT AS createdAt,
        GROUP_CONCAT(DISTINCT p.SERVICE_TYPE ORDER BY oi.ID SEPARATOR ', ') AS serviceType,
        GROUP_CONCAT(DISTINCT p.ADDITIONAL_SERVICES ORDER BY oi.ID SEPARATOR ', ') AS additionalService,
        GROUP_CONCAT(DISTINCT p.ADDRESS ORDER BY oi.ID SEPARATOR ', ') AS address,
        o.TOTAL_AMOUNT AS totalAmount,
        o.ORDER_STATUS AS orderStatus,
        pm.PAYMENT_STATUS AS paymentStatus
    FROM ORDERS o
             JOIN customers c
                  ON c.USER_ID = o.USER_ID
             JOIN ORDER_ITEMS oi
                  ON oi.ORDER_ID = o.ID
             JOIN PRODUCTS p
                  ON p.ID = oi.PRODUCT_ID
             LEFT JOIN PAYMENTS pm
                       ON pm.ORDER_ID = o.ID
    WHERE (:orderCode IS NULL OR LOWER(o.ORDER_CODE) LIKE LOWER(CONCAT('%', :orderCode, '%')))
      AND (:address IS NULL OR LOWER(p.ADDRESS) LIKE LOWER(CONCAT('%', :address, '%')))
      AND (:minPrice IS NULL OR o.TOTAL_AMOUNT >= :minPrice)
      AND (:maxPrice IS NULL OR o.TOTAL_AMOUNT <= :maxPrice)
      AND (:orderStatus IS NULL OR o.ORDER_STATUS = :orderStatus)
    GROUP BY
        o.ID,
        o.ORDER_CODE,
        c.FULL_NAME,
        o.CREATED_AT,
        o.TOTAL_AMOUNT,
        o.ORDER_STATUS,
        pm.PAYMENT_STATUS
    ORDER BY
        CASE WHEN :sortByPrice = 'asc' THEN o.TOTAL_AMOUNT END ASC,
        CASE WHEN :sortByPrice = 'desc' THEN o.TOTAL_AMOUNT END DESC,
        o.CREATED_AT DESC
""", nativeQuery = true)
    List<GetallOder> searchOrder(
            @Param("orderCode") String orderCode,
            @Param("address") String address,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("sortByPrice") String sortByPrice,
            @Param("orderStatus") String orderStatus
    );

    @Query(value = """
    SELECT
        o.ID AS orderId,
        o.ORDER_CODE AS orderCode,
        o.USER_ID AS userId,
        o.MERCHANT_ID AS merchantId,
        c.FULL_NAME AS fullNameCustomer,
        c.PHONE AS phone,
        c.ADDRESS AS address,
        o.CREATED_AT AS createdAt,
        o.TOTAL_AMOUNT AS totalAmount,
        o.ORDER_STATUS AS orderStatus,
        COALESCE(pm.PAYMENT_STATUS, 'UNPAID') AS paymentStatus,
        GROUP_CONCAT(p.PRODUCT_NAME ORDER BY oi.ID SEPARATOR ', ') AS productNames,
        GROUP_CONCAT(oi.QUANTITY ORDER BY oi.ID SEPARATOR ', ') AS quantities
    FROM ORDERS o
             JOIN CUSTOMERS c ON c.USER_ID = o.USER_ID
             JOIN ORDER_ITEMS oi ON oi.ORDER_ID = o.ID
             JOIN PRODUCTS p ON p.ID = oi.PRODUCT_ID
             LEFT JOIN PAYMENTS pm ON pm.ORDER_ID = o.ID
    WHERE o.ORDER_CODE = :orderCode
    GROUP BY
        o.ID,
        o.ORDER_CODE,
        o.USER_ID,
        o.MERCHANT_ID,
        c.FULL_NAME,
        c.PHONE,
        c.ADDRESS,
        o.CREATED_AT,
        o.TOTAL_AMOUNT,
        o.ORDER_STATUS,
        pm.PAYMENT_STATUS
    """, nativeQuery = true)
    InvoiceProjection findInvoiceByOrderCode(@Param("orderCode") String orderCode);

    @Query(value = """
SELECT
    o.ORDER_CODE AS orderCode,
    c.FULL_NAME AS fullNameCustomer,
    c.CCCD AS cccd,
    c.PHONE AS phone,
    c.ADDRESS AS address,
    u.GMAIL AS gmail,
    o.CREATED_AT AS createdAt,
    o.UPDATED_AT AS updatedAt,
    o.TOTAL_AMOUNT AS totalAmount,
    o.ORDER_STATUS AS orderStatus,
    COALESCE(pm.PAYMENT_STATUS, 'UNPAID') AS paymentStatus,
    pm.PAYMENT_CODE AS paymentCode,
    pm.AMOUNT AS paymentAmount,
    pm.PAID_AT AS paidAt,
    GROUP_CONCAT(p.PRODUCT_NAME ORDER BY oi.ID SEPARATOR ', ') AS productNames,
    GROUP_CONCAT(oi.QUANTITY ORDER BY oi.ID SEPARATOR ', ') AS quantities
FROM ORDERS o
JOIN CUSTOMERS c ON c.USER_ID = o.USER_ID
JOIN USERS u ON u.ID = c.USER_ID
JOIN ORDER_ITEMS oi ON oi.ORDER_ID = o.ID
JOIN PRODUCTS p ON p.ID = oi.PRODUCT_ID
LEFT JOIN PAYMENTS pm ON pm.ORDER_ID = o.ID
WHERE o.ORDER_CODE = :orderCode
GROUP BY
    o.ID,
    o.ORDER_CODE,
    c.FULL_NAME,
    c.CCCD,
    c.PHONE,
    c.ADDRESS,
    u.GMAIL,
    o.CREATED_AT,
    o.UPDATED_AT,
    o.TOTAL_AMOUNT,
    o.ORDER_STATUS,
    pm.PAYMENT_STATUS,
    pm.PAYMENT_CODE,
    pm.AMOUNT,
    pm.PAID_AT
""", nativeQuery = true)
    DetailedSet getOrderDetail(@Param("orderCode") String orderCode);


    @Modifying
    @Query("UPDATE Order o SET o.orderStatus = :orderStatus WHERE o.orderCode = :orderCode")
    int updateOrderStatus(@Param("orderCode") String orderCode,
                          @Param("orderStatus") OrderStatus orderStatus);


}