package com.example.library.service;

import com.example.library.dto.author.AuthorDto;
import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testAddAuthor() {
        Author author = new Author();

        authorService.addAuthor(author);

        verify(authorRepository).save(author);
    }

    @Test
    void testGetAuthorByNameAndSurname_Exists() {
        String name = "John";
        String surname = "Doe";
        AuthorDto authorDto = new AuthorDto(name, surname);
        Author author = new Author();

        when(authorRepository.findAuthorByNameAndSurname(name, surname)).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.getAuthorByNameAndSurname(authorDto);

        assertTrue(result.isPresent());
        assertEquals(author, result.get());
    }

    @Test
    void testGetAuthorByNameAndSurname_NotExists() {
        String name = "Jane";
        String surname = "Smith";
        AuthorDto authorDto = new AuthorDto(name, surname);

        when(authorRepository.findAuthorByNameAndSurname(name, surname)).thenReturn(Optional.empty());

        Optional<Author> result = authorService.getAuthorByNameAndSurname(authorDto);

        assertFalse(result.isPresent());
    }
}