package com.example.library.mapper.book;

import com.example.library.dto.book.BookReadUpdateDto;
import com.example.library.mapper.author.AuthorMapper;
import com.example.library.model.Book;
public class BookReadUpdateMapper {
    public static BookReadUpdateDto mapToDto(Book book) {
        BookReadUpdateDto bookDto = BookReadUpdateDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .genre(book.getGenre())
                .description(book.getDescription())
                .ISBN(book.getISBN())
                .authorDto(AuthorMapper
                        .mapToDto(book.getAuthor()))
                //.quantities(book.getQuantities())
                .build();
        if (book.getCoAuthor() != null) {
            bookDto.setCoAuthorDto(AuthorMapper
                    .mapToDto(book.getCoAuthor()));
        }
        return bookDto;
    }

    public static Book mapToModel(BookReadUpdateDto bookDto) {
        return Book.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .genre(bookDto.getGenre())
                .description(bookDto.getDescription())
                .ISBN(bookDto.getISBN())
                .author(AuthorMapper
                        .mapToModel(bookDto.getAuthorDto()))
                //возможно тоже сделать проверку на null? но у нас есть проверка на наличие соавтора в контроллере
                .coAuthor(AuthorMapper
                        .mapToModel(bookDto.getCoAuthorDto()))
                //.quantities(bookDto.getQuantities())
                .build();
    }
}
