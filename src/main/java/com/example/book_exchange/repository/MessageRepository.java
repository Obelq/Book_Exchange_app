package com.example.book_exchange.repository;

import com.example.book_exchange.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find unread messages by receiver ID
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);

    // Find all messages for a specific book
    List<Message> findByBookId(Long bookId);

    // Find messages where sender or receiver has the given username
    List<Message> findBySender_UsernameOrReceiver_Username(String senderUsername, String receiverUsername);

    // Find messages by receiver username
    List<Message> findByReceiver_Username(String receiverUsername);

    // Find messages for a book, newest first
    List<Message> findByBookIdOrderBySentAtDesc(Long bookId);

    // ✅ Optional: Find messages for a book between two specific users
    List<Message> findByBookIdAndSenderIdAndReceiverIdOrderBySentAtAsc(Long bookId, Long senderId, Long receiverId);

    // ✅ Optional: Count unread messages for a specific book and receiver
    Long countByBookIdAndReceiverIdAndIsReadFalse(Long bookId, Long receiverId);
}
