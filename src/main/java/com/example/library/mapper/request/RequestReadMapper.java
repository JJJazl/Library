package com.example.library.mapper.request;

import com.example.library.dto.request.RequestReadDto;
import com.example.library.model.HistoryOfRequest;

public interface RequestReadMapper {
    RequestReadDto mapToDto(HistoryOfRequest request);
}
