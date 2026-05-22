package com.example.book_exchange.controller;

import com.example.book_exchange.dto.CommentDTO;
import com.example.book_exchange.model.User;
import com.example.book_exchange.service.CommentService;
import com.example.book_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentWebController {

    private final CommentService commentService;
    private final UserService userService;

    // Add a new comment or reply from a Thymeleaf form
    @PostMapping("/add")
    public String addComment(@RequestParam Long bookId,
                             @RequestParam String content,
                             @RequestParam String username,
                             @RequestParam(required = false) Long parentCommentId) {

        User user = userService.getUserByUsername(username);

        CommentDTO dto = new CommentDTO();
        dto.setBookId(bookId);
        dto.setContent(content);
        dto.setParentCommentId(parentCommentId);
        dto.setUserId(user.getId());
        dto.setAuthorUsername(user.getUsername());

        commentService.addComment(dto);

        return "redirect:/books/view/" + bookId;
    }
}

