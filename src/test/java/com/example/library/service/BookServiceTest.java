package com.example.library.service;

import com.example.library.dto.author.AuthorDto;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private QuantityRepository quantityRepository;

    @Mock
    private BookCreateMapper bookCreateMapper;

    @Mock
    private BookReadUpdateMapper bookReadUpdateMapper;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBookWithMainAuthor() {
        // Mock dependencies
        BookCreateDto bookDto = new BookCreateDto();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("John");
        authorDto.setSurname("Doe");
        bookDto.setAuthorDto(authorDto);
        // Set other properties

        Book book = new Book();
        Author author = new Author();
        author.setName("John");
        author.setSurname("Doe");

        when(authorRepository.findAuthorByNameAndSurname("John", "Doe"))
                .thenReturn(Optional.of(author));
        when(bookCreateMapper.mapToModel(bookDto)).thenReturn(book);

        // Call the method under test
        bookService.addBookWithMainAuthor(bookDto);

        // Verify the interactions
        verify(authorRepository).findAuthorByNameAndSurname("John", "Doe");
        verify(bookCreateMapper).mapToModel(bookDto);
        verify(bookRepository).save(book);
        verify(quantityRepository).addQuantity(book, bookDto.getQuantity());
    }

    @Test
    void testAddBookWithMainAuthorAndCoAuthor() {
        // Mock dependencies
        BookCreateDto bookDto = new BookCreateDto();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("John");
        authorDto.setSurname("Doe");
        bookDto.setAuthorDto(authorDto);
        AuthorDto coAuthorDto = new AuthorDto();
        coAuthorDto.setName("Jane");
        coAuthorDto.setSurname("Smith");
        bookDto.setCoAuthorDto(coAuthorDto);
        // Set other properties

        Book book = new Book();
        Author author = new Author();
        author.setName("John");
        author.setSurname("Doe");
        Author coAuthor = new Author();
        coAuthor.setName("Jane");
        coAuthor.setSurname("Smith");

        when(authorRepository.findAuthorByNameAndSurname("John", "Doe")).thenReturn(Optional.of(author));
        when(authorRepository.findAuthorByNameAndSurname("Jane", "Smith")).thenReturn(Optional.of(coAuthor));
        when(bookCreateMapper.mapToModel(bookDto)).thenReturn(book);

        // Call the method under test
        bookService.addBookWithMainAuthorAndCoAuthor(bookDto);

        // Verify the interactions
        verify(authorRepository).findAuthorByNameAndSurname("John", "Doe");
        verify(authorRepository).findAuthorByNameAndSurname("Jane", "Smith");
        verify(bookCreateMapper).mapToModel(bookDto);
        verify(bookRepository).save(book);
    }

    @Test
    void testUpdateBook() {
        // Mock dependencies
        long bookId = 1L;
        BookReadUpdateDto bookDto = new BookReadUpdateDto();

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("John");
        authorDto.setSurname("Doe");

        AuthorDto coAuthorDto = new AuthorDto();
        coAuthorDto.setName("CoJohn");
        coAuthorDto.setSurname("CoDoe");

        bookDto.setAuthorDto(authorDto);
        bookDto.setCoAuthorDto(coAuthorDto);
        // Set other properties

        Book book = new Book();

        Author author = new Author();
        author.setName("John");
        author.setSurname("Doe");

        Author coAuthor = new Author();
        author.setName("CoJohn");
        author.setSurname("CoDoe");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findAuthorByNameAndSurname("John", "Doe"))
                .thenReturn(Optional.of(author));
        when(authorRepository.findAuthorByNameAndSurname("CoJohn", "CoDoe"))
                .thenReturn(Optional.of(coAuthor));

        // Call the method under test
        bookService.updateBook(bookDto, bookId);

        // Verify the interactions
        verify(bookRepository).findById(bookId);
        verify(authorRepository).findAuthorByNameAndSurname("John", "Doe");
        verify(authorRepository).findAuthorByNameAndSurname("CoJohn", "CoDoe");
        verify(bookRepository).save(book);
        verify(quantityRepository).addQuantity(book, bookDto.getQuantity());
    }

    @Test
    void testDeleteBook() {
        long bookId = 1L;

        bookService.deleteBook(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void testListBook() {
        // Mock the book repository to return a list of books
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        // Mock the bookReadUpdateMapper to return a list of book DTOs
        List<BookReadUpdateDto> expectedDtoList = List.of(new BookReadUpdateDto(), new BookReadUpdateDto());
        when(bookReadUpdateMapper.mapToDto(any(Book.class))).thenReturn(new BookReadUpdateDto());

        // Call the method under test
        List<BookReadUpdateDto> result = bookService.listBook();

        // Verify the interactions and expected outcome
        verify(bookRepository).findAll();
        verify(bookReadUpdateMapper, times(books.size())).mapToDto(any(Book.class));
        assertEquals(expectedDtoList.size(), result.size());
    }

    @Test
    void testFindBookByTitle_WithEmptyTitle() {
        String title = "";

        // Mock the book repository to return a list of books
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        // Mock the bookReadUpdateMapper to return a list of book DTOs
        List<BookReadUpdateDto> expectedDtoList = List.of(new BookReadUpdateDto(), new BookReadUpdateDto());
        when(bookReadUpdateMapper.mapToDto(any(Book.class))).thenReturn(new BookReadUpdateDto());

        // Call the method under test
        List<BookReadUpdateDto> result = bookService.findBookByTitle(title);

        // Verify the interactions and expected outcome
        verify(bookRepository).findAll();
        verify(bookReadUpdateMapper, times(books.size())).mapToDto(any(Book.class));
        assertEquals(expectedDtoList.size(), result.size());
    }

    @Test
    void testFindBookByTitle_WithNonEmptyTitle() {
        String title = "Harry Potter";

        // Mock the book repository to return a list of books
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findBookByTitle(title)).thenReturn(books);

        // Mock the bookReadUpdateMapper to return a list of book DTOs
        List<BookReadUpdateDto> expectedDtoList = List.of(new BookReadUpdateDto(), new BookReadUpdateDto());
        when(bookReadUpdateMapper.mapToDto(any(Book.class))).thenReturn(new BookReadUpdateDto());

        // Call the method under test
        List<BookReadUpdateDto> result = bookService.findBookByTitle(title);

        // Verify the interactions and expected outcome
        verify(bookRepository).findBookByTitle(title);
        verify(bookReadUpdateMapper, times(books.size())).mapToDto(any(Book.class));
        assertEquals(expectedDtoList.size(), result.size());
    }

    @Test
    void testGetBook() {
        long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(book));

        BookReadUpdateDto expectedDto = new BookReadUpdateDto();
        when(bookReadUpdateMapper.mapToDto(book)).thenReturn(expectedDto);

        BookReadUpdateDto result = bookService.getBook(bookId);

        verify(bookRepository).findById(bookId);
        verify(bookReadUpdateMapper).mapToDto(book);
        assertEquals(expectedDto, result);
    }

    @Test
    void testAddQuantity() {
        Book book = new Book();
        int count = 5;

        bookService.addQuantity(book, count);

        verify(quantityRepository, times(count)).addQuantity(book, count);
    }

    @Test
    void testGetCountOfQuantityByBookId() {
        long bookId = 1L;
        long expectedCount = 10;

        when(quantityRepository.getCountOfQuantityByBookId(bookId)).thenReturn(expectedCount);

        long result = bookService.getCountOfQuantityByBookId(bookId);

        verify(quantityRepository).getCountOfQuantityByBookId(bookId);
        assertEquals(expectedCount, result);
    }

    @Test
    void testGetPopularBookInSelectedPeriod() {
        String firstDate = "2023-01-01";
        String secondDate = "2023-12-31";

        // Mock the book repository to return a list of books
        List<Book> expectedBooks = List.of(new Book(), new Book());
        when(bookRepository.getPopularBookInSelectedPeriod(
                LocalDate.parse(firstDate, DATE_TIME_FORMATTER),
                LocalDate.parse(secondDate, DATE_TIME_FORMATTER)
        )).thenReturn(expectedBooks);

        // Call the method under test
        List<Book> result = bookService.getPopularBookInSelectedPeriod(firstDate, secondDate);

        // Verify the interactions and expected outcome
        verify(bookRepository).getPopularBookInSelectedPeriod(
                LocalDate.parse(firstDate, DATE_TIME_FORMATTER),
                LocalDate.parse(secondDate, DATE_TIME_FORMATTER)
        );
        assertEquals(expectedBooks, result);
    }
}