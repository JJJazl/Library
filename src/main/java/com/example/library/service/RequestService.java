package com.example.library.service;

import com.example.library.dto.request.RequestReadDto;
import com.example.library.mapper.request.RequestReadMapper;
import com.example.library.mapper.request.RequestReadMapperImpl;
import com.example.library.model.HistoryOfRequest;
import com.example.library.model.enums.Status;
import com.example.library.model.enums.Type;
import com.example.library.repository.BookRepository;
import com.example.library.repository.HistoryOfRequestRepository;
import com.example.library.repository.QuantityRepository;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final HistoryOfRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final QuantityRepository quantityRepository;
    private final RequestReadMapper requestReadMapper;

    private long processRequest(long id, Status status) {
        HistoryOfRequest request = requestRepository.getRequestById(id);
        requestRepository.processRequest(
                Optional.of(request)
                        .map(ofRequest -> {
                            ofRequest.setStatus(status);
                            ofRequest.setRequestProcessingDate(LocalDate.now());
                            return ofRequest;
                        }).orElseThrow()
        );
        return request.getBookId().getId();
    }

    public void addRequest(long bookId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String userEmail = userDetails.getUsername();

        HistoryOfRequest request = new HistoryOfRequest();
        request.setBookId(bookRepository.findById(bookId).orElseThrow());
        request.setUserId(userRepository.findByEmail(userEmail)
                .orElseThrow());
        request.setDateOfIssue(LocalDate.now());
        request.setShouldBeReturn(LocalDate.now().plusMonths(3));

        long countOfBookCopies = quantityRepository.getCountOfQuantityByBookId(bookId);
        request.setStatus(countOfBookCopies == 0 ? Status.NOT_AVAILABLE : Status.WAITING);

        requestRepository.addRequest(request);
    }

    public void acceptRequest(long id) {
        long bookId = processRequest(id, Status.READING);
        long copyId = quantityRepository.getFirstFreeCopyByBookId(bookId);
        quantityRepository.changeTypeOfCopyById(copyId, Type.READING);
    }

    public void rejectRequest(long id) {
        processRequest(id, Status.REJECTED);
    }

    public List<RequestReadDto> getRequestedBooks(long id) {
        return requestRepository.getRequestedBooks(id).stream()
                .map(requestReadMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public HistoryOfRequest getRequestById(long id) {
        return requestRepository.getRequestById(id);
    }

    public List<HistoryOfRequest> getBooksWithStatusWaiting() {
        return requestRepository.getBooksWithStatusWaiting();
    }

    public void returnBookToLibrary(long requestId) {
        requestRepository.returnBookToLibrary(requestId);
    }
}
