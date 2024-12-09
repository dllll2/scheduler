package com.example.scheduler.controller;

import com.example.scheduler.entity.Author;
import com.example.scheduler.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<Long> createAuthor(@RequestBody Author author) {
        Long authorId = authorService.saveAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.findAuthorById(id));
    }
}

