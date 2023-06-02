package com.example.library.mapper.author;

import com.example.library.dto.author.AuthorDto;
import com.example.library.model.Author;

public interface AuthorMapper {
    Author mapToModel(AuthorDto authorDto);
    AuthorDto mapToDto(Author author);
}
