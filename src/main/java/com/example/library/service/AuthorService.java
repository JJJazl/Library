package com.example.library.service;

import com.example.library.dto.author.AuthorDto;
import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public Optional<Author> getAuthorByNameAndSurname(AuthorDto authorDto) {
        return authorRepository.findAuthorByNameAndSurname(authorDto.getName(), authorDto.getSurname());
    }
}
