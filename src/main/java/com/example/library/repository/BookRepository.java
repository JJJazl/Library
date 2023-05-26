package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBookByTitle(String title);

    @Query("SELECT b FROM Book b LEFT JOIN b.author LEFT JOIN b.coAuthor LEFT JOIN b.requests r WHERE r.dateOfIssue BETWEEN :firstDate AND :secondDate GROUP BY b.id ORDER BY COUNT(b.id) DESC")
    List<Book> getPopularBookInSelectedPeriod(@Param("firstDate") LocalDate firstDate, @Param("secondDate") LocalDate secondDate);
}
