package com.example.book_exchange.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String status;        // e.g. "AVAILABLE", "BORROWED"
    private String uploadDate;    // formatted as "yyyy-MM-dd"
    private String ownerUsername; // username of the book owner
}
