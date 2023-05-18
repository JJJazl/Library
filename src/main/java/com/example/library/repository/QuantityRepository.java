package com.example.library.repository;

import com.example.library.model.Book;
import com.example.library.model.Quantity;
import com.example.library.model.enums.Type;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuantityRepository {

    private final EntityManagerFactory entityManagerFactory;

    public void addQuantity(Book book, int count) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            for (int i = 0; i < count; i++) {
                entityManager.persist(new Quantity(book, Type.FREE));
            }
            entityManager.getTransaction().commit();
        }
    }

    public long getFirstFreeCopyByBookId(long bookId) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Long quantityId = entityManager.createQuery(
                            "select q.id from Quantity q where q.bookId.id=:bookId and q.type=:type",
                            Long.class)
                    .setParameter("bookId", bookId)
                    .setParameter("type", Type.FREE)
                    .setMaxResults(1)
                    .getSingleResult();
            entityManager.getTransaction().commit();
            return quantityId;
        }
    }

    public long getCountOfQuantityByBookId(long bookId) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            TypedQuery<Quantity> query = entityManager.createQuery(
                    "select q from Quantity q where q.bookId.id =:bookId and q.type =: type",
                    Quantity.class);
            query.setParameter("bookId", bookId);
            query.setParameter("type", Type.FREE);
            long result = query.getResultStream().count();
            entityManager.getTransaction().commit();
            return result;
        }
    }

    public void changeTypeOfCopyById(long id, Type type) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Quantity quantity = entityManager.getReference(Quantity.class, id);
            quantity.setType(type);
            entityManager.merge(quantity);
            entityManager.getTransaction().commit();
        }
    }

    public void deleteOneCopyById(long bookId) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            TypedQuery<Quantity> query = entityManager.createQuery(
                    "select q from Quantity q where q.bookId.id=:bookId",
                    Quantity.class).setMaxResults(1);
            query.setParameter("bookId", bookId);
            entityManager.remove(query.getSingleResult());
            entityManager.getTransaction().commit();
        }
    }
}
