package com.example.library.model;

import com.example.library.model.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity<Long> {

    @Column(name = "title")
    public String title;

    @Enumerated(value = EnumType.STRING)
    public Genre genre;

    @Column(name = "description")
    public String description;

    @Column(name = "isbn")
    public String ISBN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_author_id")
    private Author coAuthor;

    @OneToMany(mappedBy = "bookId", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<HistoryOfRequest> requests;

    @OneToMany(mappedBy = "bookId", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<Quantity> quantities;

    @Builder
    public Book(Long id,
                String title,
                Genre genre,
                String description,
                String ISBN,
                Author author,
                Author coAuthor) {
        super(id);
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.ISBN = ISBN;
        this.author = author;
        this.coAuthor = coAuthor;
    }
}
