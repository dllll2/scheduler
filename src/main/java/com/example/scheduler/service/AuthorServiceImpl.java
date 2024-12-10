package com.example.scheduler.service;

import com.example.scheduler.entity.Author;
import com.example.scheduler.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Long saveAuthor(Author author) {
        // 작성자 저장
        return authorRepository.saveAuthor(author);
    }

}

