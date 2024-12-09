package com.example.scheduler.repository;

import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 일정 저장 메서드
     * 작성자 이름도 schedule 테이블에 함께 저장
     */
    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", schedule.getTask());
        parameters.put("author_id", schedule.getAuthorId());
        parameters.put("name", schedule.getName()); // 작성자 이름 포함
        parameters.put("password", schedule.getPassword());
        parameters.put("created_at", LocalDateTime.now());
        parameters.put("updated_at", LocalDateTime.now());

        // 저장 후 생성된 키 반환
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(
                key.longValue(),
                schedule.getTask(),
                schedule.getAuthorId(),
                schedule.getName(), // 작성자 이름 포함
                schedule.getCreated_at(),
                schedule.getUpdated_at()
        );
    }


    /**
     * 모든 일정 조회
     * 작성자의 이름을 포함해서 조회
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        String sql = "SELECT s.id, s.task, s.author_id, s.name, s.created_at, s.updated_at " +
                "FROM schedule s " +
                "JOIN author a ON s.author_id = a.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ScheduleResponseDto(
                rs.getLong("id"),
                rs.getString("task"),
                rs.getLong("author_id"),
                rs.getString("name"),  // 작성자 이름
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ));
    }

    /**
     * 특정 일정 조회 (ID 기준)
     * 작성자 이름도 포함해서 조회
     */
    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        String sql = "SELECT id, task, author_id, name, password, created_at, updated_at " +
                "FROM schedule WHERE id = ?";

        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), id);

        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule with ID " + id + " not found"));
    }


    /**
     * 일정 수정 메서드
     * 할 일, 작성자 이름, 비밀번호 업데이트 가능
     */
    @Override
    public int updateSchedule(Long id, String task, String name, String password) {
        String sql = "UPDATE schedule SET task = ?, name = ?, updated_at = ? " +
                "WHERE id = ? AND password = ?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        return jdbcTemplate.update(sql, task, name, now, id, password);
    }

    /**
     * 일정 삭제 메서드
     */
    @Override
    public int deleteSchedule(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * RowMapper: Schedule 객체 생성
     */
    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> new Schedule(
                rs.getLong("id"),                        // id
                rs.getString("task"),                    // task
                rs.getLong("author_id"),                 // author_id
                rs.getString("name"),                    // name (schedule 테이블의 컬럼)
                rs.getString("password"),                // password
                rs.getTimestamp("created_at").toLocalDateTime(),  // created_at
                rs.getTimestamp("updated_at").toLocalDateTime()   // updated_at
        );
    }

}
