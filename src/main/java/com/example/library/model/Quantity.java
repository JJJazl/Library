package com.example.library.model;

import com.example.library.model.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "quantity")
@AllArgsConstructor
@NoArgsConstructor
public class Quantity extends BaseEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    public Book bookId;

    @Enumerated(EnumType.STRING)
    public Type type;
}
