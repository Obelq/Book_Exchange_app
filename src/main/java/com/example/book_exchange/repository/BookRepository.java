package com.example.book_exchange.repository;

import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.User;
import com.example.book_exchange.model.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(String author);

    List<Book> findByTitleContainingIgnoreCase(String keyword);

    List<Book> findByOwnerId(Long ownerId);

    List<Book> findByOwner(User owner);

    List<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);

    List<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndStatus(String title, String author, BookStatus status);

    List<Book> findByCategoryIgnoreCase(String category);

    List<Book> findByStatus(BookStatus status);

    List<Book> findByUploadDateBetween(LocalDate startDate, LocalDate endDate);

    List<Book> findByCategoryIgnoreCaseAndStatus(String category, BookStatus status);

    List<Book> findByAuthorContainingIgnoreCaseAndCategoryIgnoreCaseAndStatus(String author, String category, BookStatus status);

    List<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategoryIgnoreCaseAndStatus(
            String title, String author, String category, BookStatus status);

    List<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategoryIgnoreCase(
            String title, String author, String category);

    List<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategoryIgnoreCaseAndStatusAndUploadDateBetween(
            String title, String author, String category, BookStatus status, LocalDate startDate, LocalDate endDate);

    long countByStatus(BookStatus status);

    long countByStatus(String status);


    long countByOwnerAndStatus(User owner, BookStatus status);
}
