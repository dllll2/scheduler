package com.example.scheduler.repository;

import com.example.scheduler.entity.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcTemplateAuthorRepository implements AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAuthorRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 이름과 이메일로 작성자 ID 조회
    @Override
    public Long findAuthorIdByNameAndEmail(String name, String email) {
        String sql = "SELECT id FROM author WHERE name = ? AND email = ?";
        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? rs.getLong("id") : null,
                name, email
        );
    }

    // 작성자 저장
    @Override
    public Long saveAuthor(Author author) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("author")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", author.getName());
        params.put("email", author.getEmail());
        params.put("created_at", LocalDateTime.now());
        params.put("updated_at", LocalDateTime.now());

        return insert.executeAndReturnKey(params).longValue();
    }
}
