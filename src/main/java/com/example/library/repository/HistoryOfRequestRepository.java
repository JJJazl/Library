package com.example.library.repository;

import com.example.library.model.HistoryOfRequest;
import com.example.library.model.Quantity;
import com.example.library.model.enums.Status;
import com.example.library.model.enums.Type;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.library.model.enums.Status.WAITING;

@Repository
@RequiredArgsConstructor
public class HistoryOfRequestRepository {

    private final EntityManagerFactory entityManagerFactory;

    public void addRequest(HistoryOfRequest request) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(request);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HistoryOfRequest request) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(request);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HistoryOfRequest> getRequestedBooks(long id) {
        List<HistoryOfRequest> list;
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            list = entityManager.createQuery("SELECT r from HistoryOfRequest r left join fetch r.bookId " +
                                    "left join fetch r.bookId.author left join fetch r.bookId.coAuthor left join fetch r.userId where r.userId.id =:id",
                            HistoryOfRequest.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .toList();
            entityManager.getTransaction().commit();
        }
        return list;
    }

    public HistoryOfRequest getRequestById(long id) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            HistoryOfRequest request = entityManager.createQuery("select r from HistoryOfRequest r " +
                            "left join fetch r.bookId left join fetch r.bookId.author left join fetch r.bookId.coAuthor " +
                            "left join fetch r.userId where r.id =:id", HistoryOfRequest.class)
                    .setParameter("id", id).getSingleResult();
            entityManager.getTransaction().commit();
            return request;
        }
    }

    public List<HistoryOfRequest> getBooksWithStatusWaiting() {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            List<HistoryOfRequest> request =
                    entityManager.createQuery("select r from HistoryOfRequest r left join fetch r.bookId left join fetch r.bookId.author left join fetch r.bookId.coAuthor where r.status =: status", HistoryOfRequest.class)
                            .setParameter("status", WAITING).getResultList();
            entityManager.getTransaction().commit();
            return request;
        }
    }

    public void returnBookToLibrary(long requestId) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();

            HistoryOfRequest request = entityManager.createQuery("select r from HistoryOfRequest r left join fetch r.bookId left join fetch r.bookId.author left join fetch r.bookId.coAuthor where r.id =:requestId",
                            HistoryOfRequest.class)
                    .setParameter("requestId", requestId).getSingleResult();

            Quantity quantity = entityManager.createQuery("select q.bookId.quantities from HistoryOfRequest q where q.id =: requestId", Quantity.class)
                    .setParameter("requestId", requestId).setMaxResults(1).getSingleResult();

            quantity.setType(Type.FREE);

            request.setReturnDate(LocalDate.now());

            int result = LocalDate.now().compareTo(request.getShouldBeReturn());
            if (result > 0) {
                request.setStatus(Status.RETURNED_NOT_ON_TIME);
            } else {
                request.setStatus(Status.RETURNED_ON_TIME);
            }

            entityManager.merge(quantity);
            entityManager.merge(request);
            entityManager.getTransaction().commit();
        }
    }
}
