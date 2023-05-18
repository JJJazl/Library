package com.example.library.service;

import com.example.library.dto.book.BookCreateDto;
import com.example.library.dto.book.BookReadUpdateDto;
import com.example.library.mapper.author.AuthorMapper;
import com.example.library.mapper.book.BookCreateMapper;
import com.example.library.mapper.book.BookReadUpdateMapper;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.QuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    //private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final QuantityRepository quantityRepository;

    public void addBookWithMainAuthor(BookCreateDto bookDto) {
        //перенести строку с преобразованием в dto в самый конец
        Book book = BookCreateMapper.mapToModel(bookDto);
        Optional<Author> author = authorRepository.findAuthorByNameAndSurname(
                bookDto.getAuthorDto().getName(),
                bookDto.getAuthorDto().getSurname()
        );
       /* Optional<Author> author = authorService.getAuthorByNameAndSurname(
                bookDto.getAuthorDto());*/
        if (author.isPresent()) {
            book.setAuthor(author.get());
        } else {
            book.setAuthor(authorRepository.save(AuthorMapper
                    .mapToModel(bookDto.getAuthorDto())));
        }
        bookRepository.save(book);
        quantityRepository.addQuantity(book, bookDto.getQuantity());
    }

    public void addBookWithMainAuthorAndCoAuthor(BookCreateDto bookDto) {
        //перенести строку с преобразованием в dto в самый конец
        Book book = BookCreateMapper.mapToModel(bookDto);
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
            book.setAuthor(authorRepository.save(AuthorMapper
                    .mapToModel(bookDto.getAuthorDto())));
        }
        if (coAuthor.isPresent()) {
            book.setCoAuthor(coAuthor.get());
        } else {
            book.setCoAuthor(authorRepository.save(AuthorMapper
                    .mapToModel(bookDto.getCoAuthorDto())));
        }
        bookRepository.save(book);
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
            book.setAuthor(authorRepository.save(AuthorMapper
                    .mapToModel(bookDto.getAuthorDto())));
        }
        if (coAuthor.isPresent()) {
            book.setCoAuthor(coAuthor.get());
        } else {
            book.setCoAuthor(authorRepository.save(AuthorMapper
                    .mapToModel(bookDto.getCoAuthorDto())));
        }
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setGenre(bookDto.getGenre());
        book.setISBN(bookDto.getISBN());
        quantityRepository.addQuantity(book, bookDto.getQuantity());
    }

    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    public List<BookReadUpdateDto> listBook() {
        return bookRepository.findAll().stream()
                .map(BookReadUpdateMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookReadUpdateDto> findBookByTitle(String title) {
        return bookRepository.findBookByTitle(title).stream()
                .map(BookReadUpdateMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public BookReadUpdateDto getBook(long id) {
        return (bookRepository.findById(id)
                .map(BookReadUpdateMapper::mapToDto)
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
//        return bookRepository.getPopularBookInSelectedPeriod(firstDate, secondDate);
        return null;
    }

    public List<Book> getUnpopularBookInSelectedPeriod(String firstDate, String secondDate) {
//        return bookRepository.getUnpopularBookInSelectedPeriod(firstDate, secondDate);
        return null;
    }

}
