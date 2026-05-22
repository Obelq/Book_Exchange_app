# Book Exchange / Sale / Borrowing System

A web-based Book Exchange system developed using Spring Boot, Thymeleaf, MySQL (XAMPP), and IntelliJ IDEA.

## Features

- User registration and login
- Role-based access control (Admin and User)
- Add, search, borrow, and reserve books
- Book status tracking
- Nested comments system
- Private messaging between users
- Book filtering by multiple criteria
- Statistics page
- Dashboard for navigation

## Technologies Used

- Java
- Spring Boot
- Spring Security
- Thymeleaf
- MySQL (XAMPP)
- Hibernate / JPA
- Maven
- IntelliJ IDEA

## Database

The project uses MySQL through XAMPP.

Database name:

```text
book_exchange_db

Import the provided .sql file into phpMyAdmin before running the project.

How to Run
Start Apache and MySQL in XAMPP
Open the project in IntelliJ IDEA
Configure database settings in:
src/main/resources/application.properties
Run:
BookExchangeApplication.java
Open in browser:
http://localhost:8080
Main Pages
Login: /auth/login
Register: /auth/register
Dashboard: /dashboard
Search Books: /books/search
Add Book: /books/new
Statistics: /statistics
Author

Chioma Nkechi
