package com.example.book_exchange.controller;

import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.User;
import com.example.book_exchange.service.BookService;
import com.example.book_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final BookService bookService;
    private final UserService userService;

    // Root URL, landing page redirect
    @GetMapping("/")
    public String rootRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            // User is logged in, redirect to dashboard
            return "redirect:/dashboard";
        }
        // Not logged in, redirect to login page
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username); // Use the method in UserService

        List<Book> books = bookService.getBooksByUserUsername(username);
        model.addAttribute("books", books);
        model.addAttribute("user", user);

        return "dashboard";
    }
}
