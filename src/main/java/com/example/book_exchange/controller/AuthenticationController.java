package com.example.book_exchange.controller;

import com.example.book_exchange.model.User;
import com.example.book_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    // ✅ Show registration page for Thymeleaf
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // ✅ Handle registration from HTML form (Thymeleaf)
    @PostMapping("/register")
    public String registerForm(@ModelAttribute User user, Model model) {
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        userService.registerUser(user);
        return "redirect:/auth/login?success";
    }

    // ✅ JSON-based registration (e.g., for Postman or JS frontend)
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> registerApi(@RequestBody User user) {
        if (userService.usernameExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with ID: " + savedUser.getId() +
                        " and username: " + savedUser.getUsername());
    }

    // ✅ Show login form (Thymeleaf)
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
