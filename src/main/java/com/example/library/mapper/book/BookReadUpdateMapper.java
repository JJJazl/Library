package com.example.library.mapper.book;

import com.example.library.dto.book.BookReadUpdateDto;
import com.example.library.model.Book;

public interface BookReadUpdateMapper {
    BookReadUpdateDto mapToDto(Book book);
}
