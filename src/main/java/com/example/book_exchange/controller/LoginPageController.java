package com.example.book_exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "/auth")

public class LoginPageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This will resolve to templates/login.html
    }
}

