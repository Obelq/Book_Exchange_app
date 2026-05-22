package com.example.book_exchange.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String contact;
    private LocalDate dateOfBirth;
}
