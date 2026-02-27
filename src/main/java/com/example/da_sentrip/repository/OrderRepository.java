package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Order;
import com.example.da_sentrip.service.OrderSummaryView;
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
            O.ORDER_CODE, O.ORDER_STATUS, O.TOTAL_AMOUNT, O.CREATED_AT,
            OI.PRODUCT_ID, OI.QUANTITY, OI.PRICE AS ITEM_PRICE,
            P.PRODUCT_CODE, P.PRODUCT_NAME, P.SERVICE_TYPE, P.PRICE AS PRODUCT_PRICE,
            P.REFUNDABLE, P.STATUS, P.TYPE, P.ADDRESS, P.IMG
        FROM ORDERS O
        JOIN ORDER_ITEMS OI ON OI.ORDER_ID = O.ID
        JOIN PRODUCTS P ON P.ID = OI.PRODUCT_ID
        WHERE O.ORDER_CODE = :orderCode
        """, nativeQuery = true)
    List<Object[]> findOrderDetailByOrderCode(@Param("orderCode") String orderCode);

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
        o.CREATED_AT as createdAt,
        o.ORDER_CODE as orderCode,
        o.ORDER_STATUS as orderStatus,
        o.TOTAL_AMOUNT as totalAmount,
        u.ID as userId,
        u.GMAIL as gmail,
        GROUP_CONCAT(p.PRODUCT_NAME SEPARATOR ', ') as productNames
    FROM ORDERS o
    JOIN USERS u ON u.ID = o.USER_ID
    JOIN ORDER_ITEMS oi ON oi.ORDER_ID = o.ID
    JOIN PRODUCTS p ON p.ID = oi.PRODUCT_ID
    WHERE o.USER_ID = :userId
    GROUP BY o.CREATED_AT, o.ORDER_CODE, o.ORDER_STATUS, o.TOTAL_AMOUNT, u.ID, u.GMAIL
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


}