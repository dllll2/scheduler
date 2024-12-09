package com.example.scheduler.dto;

import com.example.scheduler.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long id;                  // 일정 ID
    private String task;              // 할 일
    private Long authorId;            // 작성자 ID
    private String name;              // 작성자 이름
    private LocalDateTime created_at; // 생성일
    private LocalDateTime updated_at; // 수정일

    // 모든 필드를 포함하는 생성자
    public ScheduleResponseDto(Long id, String task, Long authorId, String name, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.task = task;
        this.authorId = authorId;
        this.name = name; // 작성자 이름만 반환
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Schedule 엔티티에서 변환할 때 사용하는 생성자
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.task = schedule.getTask();
        this.authorId = schedule.getAuthorId();
        this.name = schedule.getName();
        this.created_at = schedule.getCreated_at();
        this.updated_at = schedule.getUpdated_at();
    }
}
