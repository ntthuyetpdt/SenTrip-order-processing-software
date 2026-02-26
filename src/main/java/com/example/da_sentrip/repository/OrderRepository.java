package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
}