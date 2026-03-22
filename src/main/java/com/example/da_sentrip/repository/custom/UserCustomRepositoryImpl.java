package com.example.da_sentrip.repository.custom;

import com.example.da_sentrip.model.dto.reponse.CustomerProfileDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeProfileDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantProfileDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager em;

    @Override
    public Object getProfileByEmail(String email) {
        String roleQuery = "SELECT u.ROLE FROM USERS u WHERE u.GMAIL = :email";
        Object roleRaw = em.createNativeQuery(roleQuery)
                .setParameter("email", email)
                .getSingleResult();

        String role = roleRaw != null ? roleRaw.toString().trim() : "";

        return switch (role) {
            case "2", "3" -> getEmployeeProfile(email);
            case "4"      -> getCustomerProfile(email);
            case "5"      -> getMerchantProfile(email);
            case "1"      -> throw new RuntimeException("Admin profile not supported");
            default       -> throw new RuntimeException("Role not found: " + role);
        };
    }

    private EmployeeProfileDTO getEmployeeProfile(String email) {
        String sql = """
                SELECT u.GMAIL, r.ROLE_NAME,
                       e.MNV, e.FULL_NAME, e.PHONE, e.GENDER,
                       e.DATE, e.ADDRESS, e.JOIN_DATE,
                       e.CCCD, e.BANK_NAME, e.ACCOUNT_BANK,
                       CASE
                           WHEN e.IMG IS NOT NULL AND e.IMG REGEXP '^[0-9]+$'
                           THEN ds.MEDIA_URL
                           ELSE e.IMG
                       END AS img
                FROM USERS u
                JOIN DIM_ROLE r ON r.ROLE_CODE = u.ROLE
                LEFT JOIN EMPLOYEES e ON e.USER_ID = u.ID
                LEFT JOIN DATA_SOUSES ds
                    ON e.IMG IS NOT NULL
                    AND e.IMG REGEXP '^[0-9]+$'
                    AND ds.ID = CAST(e.IMG AS UNSIGNED)
                WHERE u.GMAIL = :email
                """;

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("email", email)
                .getSingleResult();

        return new EmployeeProfileDTO(
                (String) r[0],
                (String) r[1],
                getPermissions(email),
                r[2]  != null ? (String) r[2]  : null,
                r[3]  != null ? (String) r[3]  : null,
                r[4]  != null ? (String) r[4]  : null,
                r[5]  != null ? (String) r[5]  : null,
                toLocalDateTime(r[6]),
                r[7]  != null ? (String) r[7]  : null,
                toLocalDateTime(r[8]),
                r[9]  != null ? (String) r[9]  : null,
                r[10] != null ? (String) r[10] : null,
                r[11] != null ? (String) r[11] : null,
                r[12] != null ? (String) r[12] : null
        );
    }

    private MerchantProfileDTO getMerchantProfile(String email) {
        String sql = """
                SELECT u.GMAIL, r.ROLE_NAME,
                       m.MERCHANT_CODE, m.MERCHANT_NAME, m.PHONE,
                       m.CCCD, m.BANK_NAME, m.BANK_ACCOUNT,
                       m.ADDRESS, m.BUSINESS_LICENSE_NUMBER,
                       CASE
                           WHEN m.IMG IS NOT NULL AND m.IMG REGEXP '^[0-9]+$'
                           THEN ds.MEDIA_URL
                           ELSE m.IMG
                       END AS img
                FROM USERS u
                JOIN DIM_ROLE r ON r.ROLE_CODE = u.ROLE
                LEFT JOIN MERCHANTS m ON m.USER_ID = u.ID
                LEFT JOIN DATA_SOUSES ds
                    ON m.IMG IS NOT NULL
                    AND m.IMG REGEXP '^[0-9]+$'
                    AND ds.ID = CAST(m.IMG AS UNSIGNED)
                WHERE u.GMAIL = :email
                """;

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("email", email)
                .getSingleResult();

        return new MerchantProfileDTO(
                (String) r[0],
                (String) r[1],
                getPermissions(email),
                r[2]  != null ? (String) r[2]  : null,
                r[3]  != null ? (String) r[3]  : null,
                r[4]  != null ? (String) r[4]  : null,
                r[5]  != null ? (String) r[5]  : null,
                r[6]  != null ? (String) r[6]  : null,
                r[7]  != null ? (String) r[7]  : null,
                r[8]  != null ? (String) r[8]  : null,
                r[10] != null ? (String) r[10] : null,
                r[9]  != null ? (String) r[9]  : null
        );
    }

    private CustomerProfileDTO getCustomerProfile(String email) {
        String sql = """
                SELECT u.GMAIL, r.ROLE_NAME,
                       c.CCCD, c.FULL_NAME, c.DATE,
                       c.PHONE, c.ADDRESS,
                       CASE
                           WHEN c.IMG IS NOT NULL AND c.IMG REGEXP '^[0-9]+$'
                           THEN ds.MEDIA_URL
                           ELSE c.IMG
                       END AS img
                FROM USERS u
                JOIN DIM_ROLE r ON r.ROLE_CODE = u.ROLE
                LEFT JOIN CUSTOMERS c ON c.USER_ID = u.ID
                LEFT JOIN DATA_SOUSES ds
                    ON c.IMG IS NOT NULL
                    AND c.IMG REGEXP '^[0-9]+$'
                    AND ds.ID = CAST(c.IMG AS UNSIGNED)
                WHERE u.GMAIL = :email
                """;

        Object[] r = (Object[]) em.createNativeQuery(sql)
                .setParameter("email", email)
                .getSingleResult();

        return new CustomerProfileDTO(
                (String) r[0],
                (String) r[1],
                getPermissions(email),
                r[2] != null ? (String) r[2] : null,
                r[3] != null ? (String) r[3] : null,
                toLocalDateTime(r[4]),
                r[5] != null ? (String) r[5] : null,
                r[6] != null ? (String) r[6] : null,
                r[7] != null ? (String) r[7] : null
        );
    }

    @SuppressWarnings("unchecked")
    private List<String> getPermissions(String email) {
        String sql = """
                SELECT p.PERMISSION_CODE FROM PERMISSION p
                JOIN ROLE_PERMISSION rp ON rp.PERMISSION_ID = p.ID
                JOIN DIM_ROLE r ON r.ID = rp.ROLE_ID
                JOIN USERS u ON u.ROLE = r.ROLE_CODE
                WHERE u.GMAIL = :email
                """;
        return em.createNativeQuery(sql)
                .setParameter("email", email)
                .getResultList();
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) return null;
        if (value instanceof Timestamp ts) return ts.toLocalDateTime();
        return null;
    }
}