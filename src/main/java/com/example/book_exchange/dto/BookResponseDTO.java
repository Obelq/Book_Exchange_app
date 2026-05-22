package com.example.book_exchange.dto;

import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.User;
import com.example.book_exchange.model.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String category;
    private BookStatus status;
    private LocalDate uploadDate;
    private String ownerUsername;

    // Constructor to map from Book entity
    public BookResponseDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.category = book.getCategory();
        this.status = book.getStatus();
        this.uploadDate = book.getUploadDate();

        User owner = book.getOwner();
        this.ownerUsername = (owner != null) ? owner.getUsername() : null;
    }
}
