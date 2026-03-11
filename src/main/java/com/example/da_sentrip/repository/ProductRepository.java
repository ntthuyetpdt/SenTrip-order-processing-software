package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.view.MerchantDashboardView;
import com.example.da_sentrip.model.dto.reponse.view.OrderCustomerView;
import com.example.da_sentrip.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        SELECT * FROM products p
        WHERE (:productName IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :productName, '%')))
          AND (:address IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%')))
          AND (:price IS NULL OR p.price = :price)
    """, nativeQuery = true)
    List<Product> search(
            @Param("productName") String productName,
            @Param("price") String price,
            @Param("address") String address
    );
    @Query(value = """
        SELECT
            O.ID AS orderId,
            O.ORDER_CODE AS orderCode,
            O.CREATED_AT AS createdAt,
            COALESCE(C.FULL_NAME, U.GMAIL) AS fullName,
            COALESCE(C.PHONE, '') AS phone,
            P.ADDRESS AS address,
            P.PRODUCT_NAME AS productName,
            P.TYPE AS type,
            P.ADDITIONAL_SERVICES AS additionalServices,
            OI.QUANTITY AS quantity,
            OI.PRICE AS price
        FROM ORDERS O
                 JOIN ORDER_ITEMS OI ON OI.ORDER_ID = O.ID
                 JOIN PRODUCTS P ON P.ID = OI.PRODUCT_ID
                 LEFT JOIN USERS U ON U.ID = O.USER_ID
                 LEFT JOIN CUSTOMERS C ON C.USER_ID = U.ID
        WHERE P.MERCHANT_ID = :MERCHANT_ID
        ORDER BY O.CREATED_AT DESC;
""", nativeQuery = true)
    List<OrderCustomerView> getOrderCustomerFull(@Param("MERCHANT_ID") Long merchantId);

    @Query(value = """
    SELECT 
        COALESCE(SUM(T.REVENUE), 0) AS totalRevenue,
        COUNT(DISTINCT T.USER_ID) AS totalCustomers,
        COUNT(DISTINCT T.ORDER_ID) AS totalOrders,
        COUNT(DISTINCT CASE 
            WHEN T.REVENUE > 0 THEN T.ORDER_ID 
        END) AS successOrders,
        COUNT(DISTINCT CASE 
            WHEN T.ORDER_STATUS = 'CANCELLED' THEN T.ORDER_ID 
        END) AS cancelledOrders,
        COUNT(DISTINCT CASE 
            WHEN T.REVENUE > 0 OR T.ORDER_STATUS = 'CANCELLED' THEN T.ORDER_ID 
        END) AS successAndCancelledOrders
    FROM (
        SELECT 
            O.ID AS ORDER_ID,
            O.USER_ID,
            O.ORDER_STATUS,
            MAX(CASE 
                WHEN PY.PAYMENT_STATUS = 'SUCCESS' THEN PY.AMOUNT 
                ELSE 0 
            END) AS REVENUE
        FROM ORDERS O
        JOIN ORDER_ITEMS OI ON OI.ORDER_ID = O.ID
        JOIN PRODUCTS P ON P.ID = OI.PRODUCT_ID
        LEFT JOIN PAYMENTS PY ON PY.ORDER_ID = O.ID
        WHERE P.MERCHANT_ID = :merchantId
          AND O.CREATED_AT >= :startDate
          AND O.CREATED_AT < :endDate
        GROUP BY O.ID, O.USER_ID, O.ORDER_STATUS
    ) T
    """, nativeQuery = true)
    MerchantDashboardView getMerchantDashboard(
            @Param("merchantId") Long merchantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    List<Product> findByMerchantId(Long merchantId);
}