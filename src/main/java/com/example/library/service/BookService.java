package com.example.library.service;

import com.example.library.dto.book.BookCreateDto;
import com.example.library.dto.book.BookReadUpdateDto;
import com.example.library.mapper.author.AuthorMapper;
import com.example.library.mapper.author.AuthorMapperImpl;
import com.example.library.mapper.book.BookCreateMapper;
import com.example.library.mapper.book.BookReadUpdateMapper;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.QuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final QuantityRepository quantityRepository;
    private final BookCreateMapper bookCreateMapper;
    private final BookReadUpdateMapper bookReadUpdateMapper;
    private final AuthorMapper authorMapper;

    public void addBookWithMainAuthor(BookCreateDto bookDto) {
        Book book = bookCreateMapper.mapToModel(bookDto);
        Optional<Author> author = authorRepository.findAuthorByNameAndSurname(
                bookDto.getAuthorDto().getName(),
                bookDto.getAuthorDto().getSurname()
        );
        if (author.isPresent()) {
            book.setAuthor(author.get());
        } else {
            book.setAuthor(authorRepository.save(authorMapper
                    .mapToModel(bookDto.getAuthorDto())));
        }
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            return;
        }
        quantityRepository.addQuantity(book, bookDto.getQuantity());
    }

    public void addBookWithMainAuthorAndCoAuthor(BookCreateDto bookDto) {
        Book book = bookCreateMapper.mapToModel(bookDto);
        Optional<Author> mainAuthor = authorRepository.findAuthorByNameAndSurname(
                bookDto.getAuthorDto().getName(),
                bookDto.getAuthorDto().getSurname()
        );
        Optional<Author> coAuthor = authorRepository.findAuthorByNameAndSurname(
                bookDto.getCoAuthorDto().getName(),
                bookDto.getCoAuthorDto().getSurname()
        );
        if (mainAuthor.isPresent()) {
            book.setAuthor(mainAuthor.get());
        } else {
            book.setAuthor(authorRepository.save(authorMapper
                    .mapToModel(bookDto.getAuthorDto())));
        }
        if (coAuthor.isPresent()) {
            book.setCoAuthor(coAuthor.get());
        } else {
            book.setCoAuthor(authorRepository.save(authorMapper
                    .mapToModel(bookDto.getCoAuthorDto())));
        }
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            return;
        }
        addQuantity(book, bookDto.getQuantity());
    }

    public void updateBook(BookReadUpdateDto bookDto, long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow();
        Optional<Author> mainAuthor = authorRepository.findAuthorByNameAndSurname(
                bookDto.getAuthorDto().getName(),
                bookDto.getAuthorDto().getSurname()
        );
        Optional<Author> coAuthor = authorRepository.findAuthorByNameAndSurname(
                bookDto.getCoAuthorDto().getName(),
                bookDto.getCoAuthorDto().getSurname()
        );
        if (mainAuthor.isPresent()) {
            book.setAuthor(mainAuthor.get());
        } else {
            book.setAuthor(authorRepository.save(authorMapper
                    .mapToModel(bookDto.getAuthorDto())));
        }
        if (coAuthor.isPresent()) {
            book.setCoAuthor(coAuthor.get());
        } else {
            book.setCoAuthor(authorRepository.save(authorMapper
                    .mapToModel(bookDto.getCoAuthorDto())));
        }
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setGenre(bookDto.getGenre());
        book.setISBN(bookDto.getISBN());
        quantityRepository.addQuantity(book, bookDto.getQuantity());
        bookRepository.save(book);
    }

    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    public List<BookReadUpdateDto> listBook() {
        return bookRepository.findAll().stream()
                .map(bookReadUpdateMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookReadUpdateDto> findBookByTitle(String title) {
        List<Book> books = title.isEmpty()
                ? bookRepository.findAll()
                : bookRepository.findBookByTitle(title);
        return books.stream()
                .map(bookReadUpdateMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public BookReadUpdateDto getBook(long id) {
        return (bookRepository.findById(id)
                .map(bookReadUpdateMapper::mapToDto)
                .orElseThrow());
    }

    public void addQuantity(Book book, int count) {
        for (int i = 0; i < count; i++) {
            quantityRepository.addQuantity(book, count);
        }
    }

    public long getCountOfQuantityByBookId(long bookId) {
        return quantityRepository.getCountOfQuantityByBookId(bookId);
    }

    public List<Book> getPopularBookInSelectedPeriod(String firstDate, String secondDate) {
        try {
            return bookRepository.getPopularBookInSelectedPeriod(
                    LocalDate.parse(firstDate, DATE_TIME_FORMATTER),
                    LocalDate.parse(secondDate, DATE_TIME_FORMATTER));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
