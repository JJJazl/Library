package com.example.library.mapper.book;

import com.example.library.dto.book.BookCreateDto;
import com.example.library.model.Book;

public class BookCreateMapper {
    public static Book mapToModel(BookCreateDto bookDto) {
        return Book.builder()
                .title(bookDto.getTitle())
                .genre(bookDto.getGenre())
                .description(bookDto.getDescription())
                .ISBN(bookDto.getISBN())
                .build();
        //return book;
    }
}
