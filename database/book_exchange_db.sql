-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2026 at 10:00 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `book_exchange_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` bigint(20) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `average_rating` double DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `upload_date` date DEFAULT NULL,
  `borrower_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `author`, `status`, `title`, `owner_id`, `average_rating`, `category`, `description`, `upload_date`, `borrower_id`, `user_id`) VALUES
(1, 'George Orwell', 'AVAILABLE', '1984', 5, NULL, 'Dystopian', NULL, NULL, NULL, NULL),
(2, 'J.K. Rowling', 'RESERVED', 'Harry Potter and the Sorcerer\'s Stone', 5, NULL, 'Fantasy', NULL, NULL, NULL, NULL),
(3, 'J.R.R. Tolkien', 'BORROWED', 'The Hobbit', 6, NULL, 'Fantasy', NULL, NULL, NULL, NULL),
(4, 'Chinua Achebe', 'AVAILABLE', 'Things Fall Apart', 6, NULL, 'Historical Fiction', NULL, NULL, NULL, NULL),
(5, 'F. Scott Fitzgerald', 'AVAILABLE', 'The Great Gatsby', 5, NULL, 'Classic', NULL, NULL, NULL, NULL),
(6, 'Frances Hodgson Burnett', 'AVAILABLE', 'The Secret Garden', 5, NULL, 'Children\'s Literature', NULL, NULL, NULL, NULL),
(7, 'Bill Bryson', 'AVAILABLE', 'A Walk in the Woods', 8, NULL, 'Travel', NULL, NULL, NULL, NULL),
(8, 'Elizabeth von Arnim', 'AVAILABLE', 'The Enchanted April', 8, NULL, 'Romance', NULL, NULL, NULL, NULL),
(9, 'Cheryl Strayed', 'AVAILABLE', 'Wild: From Lost to Found', 7, NULL, 'Memoir', NULL, NULL, NULL, NULL),
(10, 'Tan Twan Eng', 'AVAILABLE', 'The Garden of Evening Mists', 7, NULL, 'Historical Fiction', NULL, NULL, NULL, NULL),
(11, 'Clarissa Pinkola', 'AVAILABLE', 'Women who run with the wolves', NULL, NULL, 'Memoir', 'A deep spiritual book', '2025-05-25', NULL, NULL),
(12, 'Peculiar Brown', 'BORROWED', 'We are One', NULL, NULL, 'Children\'s Literature', 'Kids lovely book', '2025-05-25', NULL, NULL),
(13, 'Jurate ', 'AVAILABLE', 'good for you', NULL, NULL, 'Dystopian', 'BOOKS THAT MAKES YOU CRY', '2025-05-26', NULL, NULL),
(14, 'Brown Peace', 'AVAILABLE', 'Little Children', NULL, NULL, 'Children\'s Literature', 'Book for little children', '2026-05-22', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `author` varchar(100) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `status` enum('AVAILABLE','BORROWED','RESERVED','SOLD') DEFAULT 'AVAILABLE',
  `owner_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id`, `content`, `book_id`, `parent_id`, `user_id`, `created_at`) VALUES
(1, 'This is a great book!', 8, NULL, 8, '2025-05-17 12:11:36.000000'),
(2, 'This is a great book!', 8, NULL, 8, '2025-05-17 12:41:11.000000'),
(3, 'This is a great book!', 8, NULL, 8, '2025-05-17 22:13:30.000000'),
(4, 'This is a great book!', 2, NULL, 1, '2025-05-17 22:26:28.000000'),
(5, 'This is a great book!', 2, NULL, 1, '2025-05-17 23:08:41.000000'),
(6, 'This is a parent comment', 7, NULL, 7, '2025-05-18 02:27:12.000000'),
(7, 'This is a reply to comment 6', 7, 6, 7, '2025-05-18 02:29:09.000000'),
(8, 'I agree with your point!', 2, 7, 1, '2025-05-17 23:29:30.000000');

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `target_user_id` int(11) DEFAULT NULL,
  `text` text DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `isread` tinyint(1) NOT NULL DEFAULT 0,
  `sent_at` datetime(6) DEFAULT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `is_read` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`id`, `content`, `isread`, `sent_at`, `book_id`, `receiver_id`, `sender_id`, `is_read`) VALUES
(1, 'i want to borrow the book', 0, '2025-05-14 21:47:41.000000', 7, 7, 8, b'0'),
(2, 'Did you enjoy reading the book?', 0, '2025-05-18 21:39:56.000000', 8, 8, 7, b'1'),
(3, 'When do you want to borrow the book?', 0, '2025-05-25 10:06:59.000000', 8, 8, 7, b'1'),
(4, 'It was a good read!', 0, '2025-05-25 13:23:44.000000', NULL, 7, 8, b'0');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','USER') DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `contact`, `date_of_birth`, `first_name`, `last_name`, `password`, `role`, `username`) VALUES
(1, '1234567890', '1995-05-01', 'Alice', 'Johnson', 'password123', 'USER', 'alice'),
(2, '0987654321', '1990-10-10', 'Bob', 'Smith', 'securepass', 'USER', 'bob'),
(3, '1112223333', '1985-01-20', 'Admin', 'User', 'adminpass', 'ADMIN', 'admin'),
(5, '123456789', '2000-01-01', 'John', 'Doe', '$2a$10$4ixtGlM869lupat4Y7shNeHh.8Q2/3Sn.H12QpEHW2YfWNMiLK8jy', 'USER', 'newuser'),
(6, 'test@email.com', '2000-01-01', 'Test', 'User', '$2a$10$Ta.acyQOI3nYkOa89fQhHevXQt4chvYYbF9KVYzYWUnBE8N7jWSbe', 'USER', 'testUser123'),
(7, 'asa@email.com', '2000-01-02', 'oma', 'her', '$2a$10$pqodFw/4VNi6pQgxRvxQ/OLCJJogqdfS/YcT2/.9vlt1Y28sIkTpC', 'USER', 'asa'),
(8, '3456789', '2020-02-02', 'boo', 'bae', '$2a$10$9WjUTLB4PGMjVjHThx/1Y.8KyMgqVgNliLRR1IshacxluLz9BrP.y', 'USER', 'chi');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `role` enum('USER','ADMIN') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK8oixnbmawbivwggwk27w1m1cm` (`owner_id`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`),
  ADD KEY `owner_id` (`owner_id`);

--
-- Indexes for table `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkko96rdq8d82wm91vh2jsfak7` (`book_id`),
  ADD KEY `FKde3rfu96lep00br5ov0mdieyt` (`parent_id`),
  ADD KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `target_user_id` (`target_user_id`),
  ADD KEY `parent_id` (`parent_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKd1nxwr12omf8n5fr0a3ira2f4` (`book_id`),
  ADD KEY `FK86f0kc2mt26ifwupnivu6v8oa` (`receiver_id`),
  ADD KEY `FKcnj2qaf5yc36v2f90jw2ipl9b` (`sender_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `comment`
--
ALTER TABLE `comment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `message`
--
ALTER TABLE `message`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `FK8oixnbmawbivwggwk27w1m1cm` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `books_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKde3rfu96lep00br5ov0mdieyt` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`),
  ADD CONSTRAINT `FKkko96rdq8d82wm91vh2jsfak7` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`target_user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `comments_ibfk_3` FOREIGN KEY (`parent_id`) REFERENCES `comments` (`id`),
  ADD CONSTRAINT `comments_ibfk_4` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`);

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FK86f0kc2mt26ifwupnivu6v8oa` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKcnj2qaf5yc36v2f90jw2ipl9b` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKd1nxwr12omf8n5fr0a3ira2f4` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `messages_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
