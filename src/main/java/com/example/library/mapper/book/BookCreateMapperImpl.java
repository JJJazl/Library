package com.example.library.mapper.book;

import com.example.library.dto.book.BookCreateDto;
import com.example.library.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookCreateMapperImpl implements BookCreateMapper {

    @Override
    public Book mapToModel(BookCreateDto bookDto) {
        return Book.builder()
                .title(bookDto.getTitle())
                .genre(bookDto.getGenre())
                .description(bookDto.getDescription())
                .ISBN(bookDto.getISBN())
                .build();
    }
}
