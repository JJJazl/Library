package com.example.library.dto.book;

import com.example.library.dto.author.AuthorDto;
import com.example.library.model.enums.Genre;
import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReadUpdateDto {

    private Long id;
    private String title;
    private Genre genre;
    private String description;
    private String ISBN;
    private AuthorDto authorDto;
    private AuthorDto coAuthorDto;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookReadUpdateDto bookDto = (BookReadUpdateDto) o;
        return title.equals(bookDto.title) && ISBN.equals(bookDto.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, ISBN);
    }
}
