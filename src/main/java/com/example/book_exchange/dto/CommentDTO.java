package com.example.book_exchange.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private String authorUsername;      // <-- helpful for display
    private Long bookId;
    private Long parentCommentId;
    private LocalDateTime createdAt;    // <-- optional timestamp
    private List<CommentDTO> replies;   // nested structure
}
