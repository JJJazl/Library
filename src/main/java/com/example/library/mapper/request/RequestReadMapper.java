package com.example.library.mapper.request;

import com.example.library.dto.request.RequestReadDto;
import com.example.library.mapper.book.BookReadUpdateMapper;
import com.example.library.mapper.user.UserReadMapper;
import com.example.library.model.HistoryOfRequest;

public class RequestReadMapper {
    public static RequestReadDto mapToDto(HistoryOfRequest request) {
        /*HistoryOfRequest request = HistoryOfRequest.builder()
                .userId(UserCreateMapper.mapToModel(requestDto.getUserDto()))
                .bookId(BookReadUpdateMapper.mapToModel(requestDto.getBookDto()))
                .dateOfIssue(requestDto.getDateOfIssue())
                .shouldBeReturn(requestDto.getShouldBeReturn())
                .returnDate(requestDto.getReturnDate())
                .status(requestDto.getStatus())
                .build();
        return request;*/
        return RequestReadDto.builder()
                .id(request.getId())
                .bookDto(BookReadUpdateMapper.mapToDto(
                        request.getBookId()
                ))
                .userDto(UserReadMapper.mapToDto(
                        request.getUserId()))
                .dateOfIssue(request.getDateOfIssue())
                .shouldBeReturn(request.getShouldBeReturn())
                .returnDate(request.getReturnDate())
                .status(request.getStatus())
                .build();
    }
}
