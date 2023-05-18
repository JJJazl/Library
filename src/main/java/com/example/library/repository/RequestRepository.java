package com.example.library.repository;

import com.example.library.model.HistoryOfRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<HistoryOfRequest, Long> {
}
