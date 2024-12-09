package com.example.scheduler.service;

import com.example.scheduler.entity.Author;

public interface AuthorService {
    Long saveAuthor(Author author);
    Author findAuthorById(Long id);
}

