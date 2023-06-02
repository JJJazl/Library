package com.example.library.mapper.author;

import com.example.library.dto.author.AuthorDto;
import com.example.library.model.Author;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author mapToModel(AuthorDto authorDto) {
        return Author.builder()
                .name(authorDto.getName())
                .surname(authorDto.getSurname())
                .books(Collections.emptyList())
                .build();
    }

    @Override
    public AuthorDto mapToDto(Author author) {
        return AuthorDto.builder()
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }
}
