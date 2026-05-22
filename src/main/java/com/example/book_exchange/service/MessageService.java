package com.example.book_exchange.service;

import com.example.book_exchange.model.Message;
import com.example.book_exchange.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    // ✅ Save and return the saved message
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    // ✅ Get all messages for a specific book
    public List<Message> getMessagesByBookId(Long bookId) {
        return messageRepository.findByBookId(bookId);
    }

    // ✅ Get all messages where the user is sender or receiver
    public List<Message> getMessagesByUsername(String username) {
        return messageRepository.findBySender_UsernameOrReceiver_Username(username, username);
    }

    // ✅ Mark a message as read and return the updated message
    public Message markAsRead(Long messageId) {
        return messageRepository.findById(messageId)
                .map(message -> {
                    message.setRead(true);
                    return messageRepository.save(message);
                })
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    // ✅ Get messages for a specific book and between two users (sender/receiver)
    public List<Message> getMessagesBetweenUsersForBook(Long bookId, Long senderId, Long receiverId) {
        return messageRepository.findByBookIdAndSenderIdAndReceiverIdOrderBySentAtAsc(bookId, senderId, receiverId);
    }

    // ✅ Count unread messages for a specific book and receiver
    public long countUnreadMessagesForBook(Long bookId, Long receiverId) {
        return messageRepository.countByBookIdAndReceiverIdAndIsReadFalse(bookId, receiverId);
    }
}
