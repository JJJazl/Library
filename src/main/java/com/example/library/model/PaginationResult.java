package com.example.library.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginationResult<E> {
    int currentPageNumber;
    int lastPageNumber;
    int pageSize;
    long totalRecords;
    List<E> records;
}
