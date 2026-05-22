
package com.example.book_exchange.controller;

import com.example.book_exchange.model.enums.BookStatus;
import com.example.book_exchange.service.BookService;
import com.example.book_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StatisticsController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        long totalBooks = bookService.countAllBooks();
        long totalUsers = userService.countAllUsers();
        long borrowedBooks = bookService.countBooksByStatus(BookStatus.BORROWED);
        long reservedBooks = bookService.countBooksByStatus(BookStatus.RESERVED);
        long availableBooks = bookService.countBooksByStatus(BookStatus.AVAILABLE);

        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("reservedBooks", reservedBooks);
        model.addAttribute("availableBooks", availableBooks);

        return "statistics";
    }
}
