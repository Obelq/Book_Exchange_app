package com.example.book_exchange.controller;

import com.example.book_exchange.dto.CommentDTO;
import com.example.book_exchange.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Add a new comment or reply
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO savedComment = commentService.addComment(commentDTO);
        return ResponseEntity.ok(savedComment);
    }

    // Get all comments by a user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getUserComments(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // Get direct replies to a specific comment
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<List<CommentDTO>> getReplies(@PathVariable Long commentId) {
        List<CommentDTO> replies = commentService.getReplies(commentId);
        return ResponseEntity.ok(replies);
    }

    // Get all top-level comments with nested replies for a book
    @GetMapping("/book/{bookId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsWithReplies(@PathVariable Long bookId) {
        List<CommentDTO> comments = commentService.getCommentsByBookIdWithReplies(bookId);
        return ResponseEntity.ok(comments);
    }

    // Optional: Get a single comment by ID (useful for edit/view)
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        // You’d need to add this method in service/repository if you want this feature
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    // Optional: Delete a comment by ID
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
