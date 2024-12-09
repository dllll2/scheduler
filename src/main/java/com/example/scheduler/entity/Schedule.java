package com.example.scheduler.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Schedule {
    private Long id;
    private String task;
    private Long authorId;
    private String name;
    private String password;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Schedule(Long id, String task, Long authorId, String name, String password, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.task = task;
        this.authorId = authorId;
        this.name = name;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Schedule(String task, Long authorId, String name, String password) {
        this.task = task;
        this.authorId = authorId;
        this.name = name;
        this.password = password;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

}
