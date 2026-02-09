package com.example.da_sentrip.repository.custom;

import com.example.da_sentrip.model.dto.reponse.MenuResponseDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class MenuCustomRepositoryImpl implements MenuCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String MENU_SQL = """
            SELECT DISTINCT
                m.MENU_NAME AS menu_name,
                m.MENU_PATH AS menu_path
            FROM USERS u
            JOIN DIM_ROLE r        ON u.ROLE = r.ROLE_CODE
            JOIN ROLE_PERMISSION rp ON r.ID = rp.ROLE_ID
            JOIN MENU_PERMISSION mp ON rp.PERMISSION_ID = mp.PERMISSION_ID
            JOIN MENU m            ON mp.MENU_ID = m.ID
            WHERE u.GMAIL = ?
            LIMIT ? OFFSET ?
            """;

    private static final String MENU_COUNT_SQL = """
            SELECT COUNT(DISTINCT m.ID)
            FROM USERS u
            JOIN DIM_ROLE r        ON u.ROLE = r.ROLE_CODE
            JOIN ROLE_PERMISSION rp ON r.ID = rp.ROLE_ID
            JOIN MENU_PERMISSION mp ON rp.PERMISSION_ID = mp.PERMISSION_ID
            JOIN MENU m            ON mp.MENU_ID = m.ID
            WHERE u.GMAIL = ?
            """;

    public MenuCustomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Object> getMenuByEmail(String email, int page, int size) {
        int offset = page * size;

        List<MenuResponseDTO> data = jdbcTemplate.query(
                MENU_SQL,
                (rs, rowNum) -> new MenuResponseDTO(
                        rs.getString("menu_name"),
                        rs.getString("menu_path")
                ),
                email, size, offset
        );

        Integer total = jdbcTemplate.queryForObject(
                MENU_COUNT_SQL,
                Integer.class,
                email
        );

        return Map.of(
                "page",  page,
                "size",  size,
                "total", total != null ? total : 0,
                "data",  data
        );
    }
}