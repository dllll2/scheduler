package com.example.scheduler.repository;

import com.example.scheduler.entity.Author;

public interface AuthorRepository {
    Long findAuthorIdByNameAndEmail(String name, String email);
    Long saveAuthor(Author author);
}
