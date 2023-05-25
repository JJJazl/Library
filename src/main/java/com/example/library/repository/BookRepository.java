package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBookByTitle(String title);

    @Query("SELECT h.bookId, b, a, coa FROM HistoryOfRequest h LEFT JOIN FETCH h.bookId b LEFT JOIN FETCH b.author a LEFT JOIN FETCH b.coAuthor coa WHERE h.dateOfIssue BETWEEN :firstDate AND :secondDate GROUP BY h.bookId, b, a, coa ORDER BY COUNT(h.bookId) DESC")
    List<Book> getPopularBookInSelectedPeriod(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate);
    @Query("SELECT h.bookId, b, a, coa FROM HistoryOfRequest h LEFT JOIN FETCH h.bookId b LEFT JOIN FETCH b.author a LEFT JOIN FETCH b.coAuthor coa WHERE h.dateOfIssue BETWEEN :firstDate AND :secondDate GROUP BY h.bookId, b, a, coa ORDER BY COUNT(h.bookId)")
    List<Book> getUnpopularBookInSelectedPeriod(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate);
}
