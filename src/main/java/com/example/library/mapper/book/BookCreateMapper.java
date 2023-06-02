package com.example.library.mapper.book;

import com.example.library.dto.book.BookCreateDto;
import com.example.library.model.Book;

public interface BookCreateMapper {
    Book mapToModel(BookCreateDto bookDto);
}
