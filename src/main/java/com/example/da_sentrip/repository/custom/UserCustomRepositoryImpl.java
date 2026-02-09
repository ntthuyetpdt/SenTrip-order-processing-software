package com.example.da_sentrip.repository.custom;

import com.example.da_sentrip.model.dto.reponse.ProfileResponseDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_PROFILE_SQL = """
            SELECT u.GMAIL AS gmail, 
                   r.ROLE_NAME AS role_name, 
                   p.PERMISSION_CODE AS permission_code 
            FROM USERS u
            JOIN DIM_ROLE r ON u.ROLE = r.ROLE_CODE
            JOIN ROLE_PERMISSION rp ON r.ID = rp.ROLE_ID
            JOIN PERMISSION p ON rp.PERMISSION_ID = p.ID
            WHERE u.GMAIL = ?
            """;

    public UserCustomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ProfileResponseDTO getProfileByEmail(String email) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_PROFILE_SQL, email);

        if (rows.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        String role = rows.get(0).get("role_name").toString();

        List<String> permissions = rows.stream()
                .map(row -> row.get("permission_code").toString())
                .distinct()
                .toList();

        return new ProfileResponseDTO(email, role, permissions);
    }
}