package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.dto.reponse.view.DashboardSummaryProjection;
import com.example.da_sentrip.model.dto.reponse.view.MonthlyRevenueProjection;
import com.example.da_sentrip.model.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("""
        SELECT m FROM Merchant m
        WHERE (:merchantName IS NULL OR m.merchantName LIKE CONCAT('%', :merchantName, '%'))
          AND (:address IS NULL OR m.address LIKE CONCAT('%', :address, '%'))
          AND (:businessLicense IS NULL OR m.businessLicense LIKE CONCAT('%', :businessLicense, '%'))
    """)
    List<Merchant> findBySearch(
            @Param("merchantName") String merchantName,
            @Param("address") String address,
            @Param("businessLicense") String businessLicense
    );

    Optional<Merchant> findByUserId(Long userId);
    Optional<Merchant> findByUser_Id(Long userId);

    @Query(value = """
    SELECT 
        DATE_FORMAT(O.CREATED_AT, '%Y-%m') AS month,
        COALESCE(SUM(O.TOTAL_AMOUNT), 0) AS revenue
    FROM ORDERS O
    JOIN (
        SELECT DISTINCT PY.ORDER_ID
        FROM PAYMENTS PY
        WHERE PY.PAYMENT_STATUS = 'SUCCESS'
    ) P ON P.ORDER_ID = O.ID
    WHERE O.CREATED_AT >= :startDate
      AND O.CREATED_AT < :endDate
    GROUP BY DATE_FORMAT(O.CREATED_AT, '%Y-%m')
    ORDER BY month ASC
    """, nativeQuery = true)
    List<MonthlyRevenueProjection> getTotalRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query(value = """
    SELECT
        COUNT(DISTINCT O.ID) AS totalOrders,
        COUNT(DISTINCT CASE
            WHEN O.ORDER_STATUS = 'CANCELLED' THEN O.ID
        END) AS cancelledOrders,
        COUNT(DISTINCT CASE
            WHEN EXISTS (
                SELECT 1
                FROM PAYMENTS PY
                WHERE PY.ORDER_ID = O.ID
                  AND PY.PAYMENT_STATUS = 'SUCCESS'
            ) THEN O.ID
        END) AS successOrders,
        COUNT(DISTINCT O.USER_ID) AS totalCustomers,
        (SELECT COUNT(*) FROM MERCHANTS) AS totalMerchants,
        (SELECT COUNT(*) FROM EMPLOYEES) AS totalEmployees
    FROM ORDERS O
    WHERE O.CREATED_AT >= :startDate
      AND O.CREATED_AT < :endDate
    """, nativeQuery = true)
    DashboardSummaryProjection getDashboardSummary(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate
    );



}
