package com.example.library.service;

import com.example.library.repository.QuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuantityService {
    private final QuantityRepository quantityRepository;

    @Autowired
    public QuantityService(QuantityRepository quantityRepository) {
        this.quantityRepository = quantityRepository;
    }

    public void deleteOneCopyById(long bookId) {
        quantityRepository.deleteOneCopyById(bookId);
    }
}
