package com.example.book_exchange.controller;

import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.Message;
import com.example.book_exchange.model.User;
import com.example.book_exchange.service.BookService;
import com.example.book_exchange.service.MessageService;
import com.example.book_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/send")
    public String showMessageForm(@RequestParam(required = false) Long bookId,
                                  @RequestParam(required = false) String sender,
                                  @RequestParam(required = false) String receiver,
                                  Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("senderUsername", sender);
        model.addAttribute("receiverUsername", receiver);
        return "message";
    }

    @GetMapping("/user")
    public String showUserMessages(Model model, @RequestParam String username) {
        List<Message> messages = messageService.getMessagesByUsername(username);
        model.addAttribute("messages", messages);
        model.addAttribute("senderUsername", username);
        return "message";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam(required = false) Long bookId,
                              @RequestParam String senderUsername,
                              @RequestParam String receiverUsername,
                              @RequestParam String messageContent,
                              Model model) {
        try {
            Message message = new Message();
            message.setContent(messageContent);
            message.setRead(false);
            message.setSentAt(LocalDateTime.now());

            Book book = (bookId != null) ? bookService.findById(bookId) : null;
            User sender = userService.getUserByUsername(senderUsername);
            User receiver = userService.getUserByUsername(receiverUsername);

            message.setBook(book);
            message.setSender(sender);
            message.setReceiver(receiver);

            messageService.sendMessage(message);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", true);
            e.printStackTrace();
        }
        return "message";
    }

    @PostMapping("/mark-read/{id}")
    public String markMessageAsRead(@PathVariable Long id) {
        Message message = messageService.markAsRead(id);
        return "redirect:/messages/user?username=" + message.getReceiver().getUsername();
    }

    @GetMapping("/book/{bookId}/between")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
            @PathVariable Long bookId,
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        List<Message> messages = messageService.getMessagesBetweenUsersForBook(bookId, senderId, receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Message>> getMessagesForBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(messageService.getMessagesByBookId(bookId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Message>> getMessagesForUser(@PathVariable String username) {
        return ResponseEntity.ok(messageService.getMessagesByUsername(username));
    }

    @GetMapping("/book/{bookId}/unread-count")
    public ResponseEntity<Long> getUnreadCountForBook(@PathVariable Long bookId,
                                                      @RequestParam Long receiverId) {
        long count = messageService.countUnreadMessagesForBook(bookId, receiverId);
        return ResponseEntity.ok(count);
    }
}
