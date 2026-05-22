package com.example.book_exchange.repository;

import com.example.book_exchange.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Fetch top-level comments (without parent) for a book,
     * eagerly loading their immediate replies.
     */
    @EntityGraph(attributePaths = {"replies"})
    @Query("SELECT c FROM Comment c WHERE c.book.id = :bookId AND c.parentComment IS NULL")
    List<Comment> findTopLevelCommentsWithRepliesByBookId(@Param("bookId") Long bookId);

    /**
     * Find all comments posted by a specific user.
     */
    List<Comment> findByUserId(Long userId);

    /**
     * Find all comments for a specific book.
     */
    List<Comment> findByBookId(Long bookId);

    /**
     * Find comments by their parent comment ID (i.e., replies).
     */
    List<Comment> findByParentComment_Id(Long parentCommentId);

    /**
     * Fetch all comments for a book with their immediate replies eagerly loaded.
     */
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.book.id = :bookId")
    List<Comment> findByBookIdWithReplies(@Param("bookId") Long bookId);

    /**
     * Fetch replies for a given parent comment.
     */
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.parentComment.id = :parentCommentId")
    List<Comment> findRepliesByParentCommentId(@Param("parentCommentId") Long parentCommentId);
}
