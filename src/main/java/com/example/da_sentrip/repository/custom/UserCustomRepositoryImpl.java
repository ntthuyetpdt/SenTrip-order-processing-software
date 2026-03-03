package com.example.da_sentrip.repository.custom;

import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_PROFILE_BY_EMAIL_SQL = """
        SELECT 
            u.GMAIL AS gmail,
            r.ROLE_NAME AS role_name,
            p.PERMISSION_CODE AS permission_code,
            COALESCE(e.FULL_NAME, c.FULL_NAME, m.MERCHANT_NAME) AS name,
            CASE
                WHEN COALESCE(e.IMG, c.IMG, m.IMG) IS NOT NULL
                     AND COALESCE(e.IMG, c.IMG, m.IMG) REGEXP '^[0-9]+$'
                THEN ds.MEDIA_URL
                ELSE COALESCE(e.IMG, c.IMG, m.IMG)
            END AS img
        FROM USERS u
        LEFT JOIN DIM_ROLE r ON u.ROLE = r.ROLE_CODE
        LEFT JOIN ROLE_PERMISSION rp ON r.ID = rp.ROLE_ID
        LEFT JOIN PERMISSION p ON rp.PERMISSION_ID = p.ID
        LEFT JOIN EMPLOYEES e ON e.USER_ID = u.ID
        LEFT JOIN CUSTOMERS c ON c.USER_ID = u.ID
        LEFT JOIN MERCHANTS m ON m.USER_ID = u.ID
        LEFT JOIN DATA_SOUSES ds
            ON (
                COALESCE(e.IMG, c.IMG, m.IMG) IS NOT NULL
                AND COALESCE(e.IMG, c.IMG, m.IMG) REGEXP '^[0-9]+$'
                AND ds.ID = CAST(COALESCE(e.IMG, c.IMG, m.IMG) AS UNSIGNED)
            )
        WHERE LOWER(u.GMAIL) = LOWER(?)
        """;

    public UserCustomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProfileResponseDTO getProfileByEmail(String email) {
        String normalizedEmail = email == null ? null : email.trim();
        if (normalizedEmail == null || normalizedEmail.isBlank()) {
            throw new RuntimeException("Email is required");
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_PROFILE_BY_EMAIL_SQL, normalizedEmail);
        if (rows.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Map<String, Object> firstRow = rows.get(0);
        String gmail = firstRow.get("gmail") != null ? firstRow.get("gmail").toString() : null;
        String role  = firstRow.get("role_name") != null ? firstRow.get("role_name").toString() : null;
        String name  = firstRow.get("name") != null ? firstRow.get("name").toString() : null;
        String img   = firstRow.get("img") != null ? firstRow.get("img").toString() : null;

        List<String> permissions = rows.stream()
                .map(row -> row.get("permission_code"))
                .filter(v -> v != null)
                .map(Object::toString)
                .distinct()
                .toList();
        return new ProfileResponseDTO(gmail, role, name, img, permissions);
    }
}