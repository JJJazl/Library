package com.example.library.mapper.request;

import com.example.library.dto.request.RequestReadDto;
import com.example.library.mapper.book.BookReadUpdateMapper;
import com.example.library.mapper.book.BookReadUpdateMapperImpl;
import com.example.library.mapper.user.UserReadMapper;
import com.example.library.model.HistoryOfRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestReadMapperImpl implements RequestReadMapper {
    private final UserReadMapper userReadMapper;
    private final BookReadUpdateMapper bookReadUpdateMapper;
    public RequestReadDto mapToDto(HistoryOfRequest request) {
        return RequestReadDto.builder()
                .id(request.getId())
                .bookDto(bookReadUpdateMapper.mapToDto(
                        request.getBookId()))
                .userDto(userReadMapper.mapToDto(
                        request.getUserId()))
                .dateOfIssue(request.getDateOfIssue())
                .shouldBeReturn(request.getShouldBeReturn())
                .returnDate(request.getReturnDate())
                .status(request.getStatus())
                .build();
    }
}
