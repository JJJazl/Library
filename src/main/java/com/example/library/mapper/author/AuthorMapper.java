package com.example.library.mapper.author;

import com.example.library.dto.author.AuthorDto;
import com.example.library.model.Author;

import java.util.Collections;

public class AuthorMapper {
    public static Author mapToModel(AuthorDto authorDto) {
        return Author.builder()
                .name(authorDto.getName())
                .surname(authorDto.getSurname())
                .books(Collections.emptyList())
                .build();
    }

    public static AuthorDto mapToDto(Author author) {
        return AuthorDto.builder()
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }
}
