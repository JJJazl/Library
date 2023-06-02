package com.example.library.service;

import com.example.library.dto.request.RequestReadDto;
import com.example.library.mapper.request.RequestReadMapper;
import com.example.library.model.Book;
import com.example.library.model.HistoryOfRequest;
import com.example.library.model.User;
import com.example.library.model.enums.Role;
import com.example.library.model.enums.Type;
import com.example.library.repository.BookRepository;
import com.example.library.repository.HistoryOfRequestRepository;
import com.example.library.repository.QuantityRepository;
import com.example.library.repository.UserRepository;
import com.example.library.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    private HistoryOfRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private QuantityRepository quantityRepository;

    @Mock
    private RequestReadMapper requestReadMapper;
    @InjectMocks
    private RequestService requestService;

    @Test
    void testAddRequest() {
        long bookId = 1L;
        String userEmail = "test@example.com";
        User user = User.builder()
                .email(userEmail)
                .password("password")
                .role(Role.READER)
                .build();
        Book book = Book.builder()
                .id(bookId)
                .build();

        UserDetails userDetails = new UserDetailsImpl(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(quantityRepository.getCountOfQuantityByBookId(bookId)).thenReturn(0L);

        requestService.addRequest(bookId);

        verify(requestRepository).addRequest(any(HistoryOfRequest.class));
    }

    @Test
    void testAcceptRequest() {
        long requestId = 1L;
        long bookId = 1L;
        long copyId = 1L;
        Book book = Book.builder()
                .id(bookId)
                .build();
        HistoryOfRequest request = HistoryOfRequest.builder()
                .bookId(book)
                .build();

        when(requestRepository.getRequestById(requestId)).thenReturn(request);
        when(quantityRepository.getFirstFreeCopyByBookId(bookId)).thenReturn(copyId);

        requestService.acceptRequest(requestId);

        verify(requestRepository).processRequest(any(HistoryOfRequest.class));
        verify(quantityRepository).changeTypeOfCopyById(copyId, Type.READING);
    }

    @Test
    void testRejectRequest() {
        long requestId = 1L;
        long bookId = 1L;
        Book book = Book.builder()
                .id(bookId)
                .build();
        HistoryOfRequest request = HistoryOfRequest.builder()
                .bookId(book)
                .build();
        when(requestRepository.getRequestById(requestId)).thenReturn(request);

        requestService.rejectRequest(requestId);

        verify(requestRepository).processRequest(any(HistoryOfRequest.class));
    }

    @Test
    void testGetRequestedBooks() {
        long id = 1L;
        List<HistoryOfRequest> requests = Arrays.asList(new HistoryOfRequest());
        RequestReadDto requestReadDto = RequestReadDto.builder()
                .id(1L)
                .build();
        List<RequestReadDto> dtos = Arrays.asList(requestReadDto);

        when(requestRepository.getRequestedBooks(id)).thenReturn(requests);
        when(requestReadMapper.mapToDto(any(HistoryOfRequest.class))).thenReturn(requestReadDto);

        List<RequestReadDto> result = requestService.getRequestedBooks(id);

        assertEquals(dtos.size(), result.size());
        assertEquals(dtos.get(0), result.get(0));
    }

    @Test
    void testGetRequestById() {
        long id = 1L;
        HistoryOfRequest request = new HistoryOfRequest();

        when(requestRepository.getRequestById(id)).thenReturn(request);

        HistoryOfRequest result = requestService.getRequestById(id);

        assertEquals(request, result);
    }
}