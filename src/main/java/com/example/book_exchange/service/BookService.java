package com.example.book_exchange.service;

import com.example.book_exchange.dto.BookDTO;
import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.User;
import com.example.book_exchange.model.enums.BookStatus;
import com.example.book_exchange.repository.BookRepository;
import com.example.book_exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book saveBookFromDTO(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory(bookDTO.getCategory());
        book.setDescription(bookDTO.getDescription());

        // Set status
        if (bookDTO.getStatus() != null && !bookDTO.getStatus().isEmpty()) {
            try {
                book.setStatus(BookStatus.valueOf(bookDTO.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                book.setStatus(BookStatus.AVAILABLE);
            }
        } else {
            book.setStatus(BookStatus.AVAILABLE);
        }

        // Set upload date
        if (bookDTO.getUploadDate() != null && !bookDTO.getUploadDate().isEmpty()) {
            try {
                book.setUploadDate(LocalDate.parse(bookDTO.getUploadDate()));
            } catch (DateTimeParseException e) {
                book.setUploadDate(LocalDate.now());
            }
        } else {
            book.setUploadDate(LocalDate.now());
        }

        // Set owner
        if (bookDTO.getOwnerUsername() != null && !bookDTO.getOwnerUsername().isEmpty()) {
            User owner = userRepository.findByUsername(bookDTO.getOwnerUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + bookDTO.getOwnerUsername()));
            book.setOwner(owner);
        }

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByUserUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return bookRepository.findByOwner(user);
    }

    public List<Book> getBooksByUser(User user) {
        return bookRepository.findByOwner(user);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));
    }

    public Book reserveBook(Long bookId, String username) {
        Book book = findById(bookId);

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for reservation.");
        }

        userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        book.setStatus(BookStatus.RESERVED);
        return bookRepository.save(book);
    }

    public Book borrowBook(Long bookId, String username) {
        Book book = findById(bookId);

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for borrowing.");
        }

        userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        book.setStatus(BookStatus.BORROWED);
        return bookRepository.save(book);
    }

    public Book updateBookStatus(Long bookId, BookStatus status) {
        Book book = findById(bookId);
        book.setStatus(status);
        return bookRepository.save(book);
    }

    // Renamed from advancedSearch to searchBooks
    public List<Book> searchBooks(
            String title,
            String author,
            String category,
            String status,
            String startDateStr,
            String endDateStr) {

        // Normalize input
        title = (title != null) ? title.trim() : "";
        author = (author != null) ? author.trim() : "";
        category = (category != null) ? category.trim() : "";

        BookStatus bookStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                bookStatus = BookStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return List.of();  // Invalid status, return empty list
            }
        }

        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
        } catch (DateTimeParseException e) {
            return List.of();  // Invalid date format, return empty list
        }

        // Match appropriate repository method
        if (startDate != null && endDate != null && bookStatus != null && !category.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategoryIgnoreCaseAndStatusAndUploadDateBetween(
                    title, author, category, bookStatus, startDate, endDate);
        }

        if (bookStatus != null && !category.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategoryIgnoreCaseAndStatus(
                    title, author, category, bookStatus);
        }

        if (!category.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategoryIgnoreCase(
                    title, author, category);
        }

        if (bookStatus != null) {
            return bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndStatus(
                    title, author, bookStatus);
        }

        return bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author);
    }

    public long countBooksByStatus(BookStatus status) {
        return bookRepository.countByStatus(status);
    }

    public long countBooksByOwnerAndStatus(User owner, BookStatus status) {
        return bookRepository.countByOwnerAndStatus(owner, status);
    }


    public long countAllBooks() {
        return bookRepository.count();
    }

    public long countBooksByStatus(String status) {
        return bookRepository.countByStatus(status);
    }



    public void deleteBook(Long bookId) {
        Book book = findById(bookId);
        if (book.getStatus() == BookStatus.AVAILABLE) {
            bookRepository.delete(book);
        } else {
            throw new IllegalStateException("Only available books can be deleted.");
        }
    }
}
