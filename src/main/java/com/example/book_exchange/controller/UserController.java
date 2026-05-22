package com.example.book_exchange.controller;

import com.example.book_exchange.model.User;
import com.example.book_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // REMOVED: Duplicate /auth/register endpoint

    @GetMapping("/profile/{username}")
    public ResponseEntity<User> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}
