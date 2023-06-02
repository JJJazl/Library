package com.example.library.mapper.book;

import com.example.library.dto.book.BookReadUpdateDto;
import com.example.library.mapper.author.AuthorMapper;
import com.example.library.mapper.author.AuthorMapperImpl;
import com.example.library.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReadUpdateMapperImpl implements BookReadUpdateMapper {

    private final AuthorMapper authorMapper;

    @Override
    public BookReadUpdateDto mapToDto(Book book) {
        BookReadUpdateDto bookDto = BookReadUpdateDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .genre(book.getGenre())
                .description(book.getDescription())
                .ISBN(book.getISBN())
                .authorDto(authorMapper
                        .mapToDto(book.getAuthor()))
                .build();
        if (book.getCoAuthor() != null) {
            bookDto.setCoAuthorDto(authorMapper
                    .mapToDto(book.getCoAuthor()));
        }
        return bookDto;
    }
}
