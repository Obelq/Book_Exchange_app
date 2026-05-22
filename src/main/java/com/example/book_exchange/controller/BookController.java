package com.example.book_exchange.controller;

import com.example.book_exchange.dto.BookDTO;
import com.example.book_exchange.dto.BookResponseDTO;
import com.example.book_exchange.dto.CommentDTO;
import com.example.book_exchange.model.Book;
import com.example.book_exchange.model.User;
import com.example.book_exchange.model.enums.BookStatus;
import com.example.book_exchange.service.BookService;
import com.example.book_exchange.service.CommentService;
import com.example.book_exchange.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final UserService userService;

    // Add a new book (admin or user)
    @PostMapping("/add")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            Book saved = bookService.saveBookFromDTO(bookDTO);
            return ResponseEntity.ok(new BookResponseDTO(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add book: " + e.getMessage());
        }
    }



    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String saveBook(@ModelAttribute("book") @Valid BookDTO bookDTO, Model model) {
        try {
            bookService.saveBookFromDTO(bookDTO);
            return "redirect:/books/all"; // or wherever you want to go after success
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add book: " + e.getMessage());
            return "book_form";
        }
    }


    // Get all books as JSON
    @GetMapping("/all-json")
    @ResponseBody
    public ResponseEntity<List<Book>> getAllBooksAsJson() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }



    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String showBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        // List of predefined categories
        List<String> categories = List.of(
                "Dystopian",
                "Fantasy",
                "Historical Fiction",
                "Classic",
                "Children's Literature",
                "Travel",
                "Romance",
                "Memoir"
        );
        model.addAttribute("categories", categories);
        return "book_form";
    }


    // View all books (HTML)
    @GetMapping("/all")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book-list"; // templates/book-list.html
    }

    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        String t = (title != null) ? title : "";
        String a = (author != null) ? author : "";
        String c = (category != null) ? category : "";

        // Validate date format but still pass raw strings to service
        try {
            if (startDate != null && !startDate.isEmpty()) LocalDate.parse(startDate);
            if (endDate != null && !endDate.isEmpty()) LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date format");
            model.addAttribute("books", Collections.emptyList());
            return "book-search";
        }

        // Validate status enum but still pass raw string to service
        if (status != null && !status.isEmpty()) {
            try {
                BookStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Invalid book status");
                model.addAttribute("books", Collections.emptyList());
                return "book-search";
            }
        }

        List<Book> books = bookService.searchBooks(t, a, c, status, startDate, endDate);
        model.addAttribute("books", books);

        return "book-search"; // your Thymeleaf template
    }


    @GetMapping("/books/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        List<CommentDTO> comments = commentService.getCommentsByBookIdWithReplies(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book_details";
    }



    // Reserve a book
    @PutMapping("/reserve/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> reserveBook(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            Book reserved = bookService.reserveBook(id, username);
            return ResponseEntity.ok(new BookResponseDTO(reserved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot reserve book: " + e.getMessage());
        }
    }

    // Borrow a book
    @PutMapping("/borrow/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> borrowBook(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            Book borrowed = bookService.borrowBook(id, username);
            return ResponseEntity.ok(new BookResponseDTO(borrowed));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot borrow book: " + e.getMessage());
        }
    }

    // Update book status
    @PutMapping("/status/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam BookStatus status) {
        try {
            Book updated = bookService.updateBookStatus(id, status);
            return ResponseEntity.ok(new BookResponseDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot update status: " + e.getMessage());
        }
    }

    // Delete a book
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot delete book: " + e.getMessage());
        }
    }

    // View book details with nested comments
    @GetMapping("/{bookId}")
    public String getBookDetails(@PathVariable Long bookId, Model model, Authentication authentication) {
        try {
            Book book = bookService.getBookById(bookId);
            List<CommentDTO> comments = commentService.getCommentsByBookIdWithReplies(bookId);

            Long userId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                User user = userService.getUserByUsername(authentication.getName());
                userId = user.getId();
            }

            model.addAttribute("book", book);
            model.addAttribute("comments", comments);
            model.addAttribute("userId", userId);

            return "book_details"; // templates/book_details.html
        } catch (Exception e) {
            model.addAttribute("error", "Book not found");
            return "error";
        }
    }
}
