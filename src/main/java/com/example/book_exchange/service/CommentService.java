package com.example.book_exchange.service;

import com.example.book_exchange.dto.CommentDTO;
import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.Comment;
import com.example.book_exchange.model.User;
import com.example.book_exchange.repository.BookRepository;
import com.example.book_exchange.repository.CommentRepository;
import com.example.book_exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * Add a new comment (top-level or reply).
     * Sets createdAt only when creating a new comment.
     */
    public CommentDTO addComment(CommentDTO dto) {
        Comment comment = convertToEntity(dto);
        // Set createdAt only if new comment (no ID)
        if (comment.getId() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        Comment saved = commentRepository.save(comment);
        return convertToDTO(saved);
    }

    /**
     * Get all top-level comments with nested replies for a book.
     */
    public List<CommentDTO> getCommentsByBookIdWithReplies(Long bookId) {
        List<Comment> comments = commentRepository.findTopLevelCommentsWithRepliesByBookId(bookId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all comments by user ID.
     */
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get direct replies to a specific comment.
     */
    public List<CommentDTO> getReplies(Long commentId) {
        List<Comment> replies = commentRepository.findByParentComment_Id(commentId);
        return replies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all comments for a book (flat list).
     */
    public List<CommentDTO> getCommentsByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a single comment by its ID.
     */
    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        return convertToDTO(comment);
    }

    /**
     * Delete a comment by its ID.
     */
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    /**
     * Convert Comment entity to DTO, recursively converting replies.
     */
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getId());
        dto.setAuthorUsername(comment.getUser().getUsername());
        dto.setBookId(comment.getBook().getId());
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        dto.setCreatedAt(comment.getCreatedAt());

        dto.setReplies(comment.getReplies() != null
                ? comment.getReplies().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList())
                : List.of());

        return dto;
    }

    /**
     * Convert CommentDTO to Comment entity.
     */
    private Comment convertToEntity(CommentDTO dto) {
        Comment comment = new Comment();

        if (dto.getId() != null) {
            comment.setId(dto.getId()); // Support updates if needed
        }

        comment.setContent(dto.getContent());

        // Set user
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);

        // Set book
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        comment.setBook(book);

        // Set parent comment if present
        if (dto.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParentComment(parent);
        }

        // Don't set createdAt here; handled in addComment method for new comments

        return comment;
    }
}
