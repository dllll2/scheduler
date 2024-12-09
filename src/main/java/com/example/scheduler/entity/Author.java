package com.example.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
}
