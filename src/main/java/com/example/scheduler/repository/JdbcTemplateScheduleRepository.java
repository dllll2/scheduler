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
     * 일정 작성 API
     * @param schedule
     * @return
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
     * 모든 일정 조회하는 API
     * @param page 페이지 번호
     * @param size 페이지 사이즈
     * @return
     */
    @Override
    public List<ScheduleResponseDto> findAllSchedules(int page, int size) {

        int offset = (page-1) * size;
        String sql = "SELECT s.id, s.task, s.author_id, a.name, s.created_at, s.updated_at FROM schedule s JOIN author a ON s.author_id = a.id LIMIT ?, ?";


        return jdbcTemplate.query(sql, (rs, rowNum) -> new ScheduleResponseDto(
                rs.getLong("id"),
                rs.getString("task"),
                rs.getLong("author_id"),
                rs.getString("name"),  // 작성자 이름
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ), offset, size);
    }

    /**
     * 작성글id로 조회하는 APU
     * @param id
     * @return
     */
    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";

        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), id);

        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule " + id + " not found"));
    }

    /**
     * 작성자id 로 작성글 조회하는 API
     * @param authorId
     * @return
     */
    @Override
    public List<ScheduleResponseDto> findSchedulesByAuthorId(Long authorId) {
        String sql = "SELECT s.id, s.task, s.author_id, a.name AS author_name, s.created_at, s.updated_at " +
                "FROM schedule s " +
                "JOIN author a ON s.author_id = a.id " +
                "WHERE s.author_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ScheduleResponseDto(
                rs.getLong("id"),
                rs.getString("task"),
                rs.getLong("author_id"),
                rs.getString("author_name"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ), authorId);
    }


    /**
     * 일정 수정 API
     * @param id
     * @param task
     * @param name
     * @param password
     * @return
     */
    @Override
    public int updateSchedule(Long id, String task, String name, String password) {
        String sql = "UPDATE schedule SET task = ?, name = ?, updated_at = ? " +
                "WHERE id = ? AND password = ?";
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        return jdbcTemplate.update(sql, task, name, now, id, password);
    }

    /**
     * 일정 삭제 API
     * @param id
     * @return
     */
    @Override
    public int deleteSchedule(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> new Schedule(
                rs.getLong("id"),
                rs.getString("task"),
                rs.getLong("author_id"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }

}
