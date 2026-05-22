package com.example.book_exchange.service;

import com.example.book_exchange.model.User;
import com.example.book_exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a user, ensuring password is encoded
    public User registerUser(User user) {
        // Ensure that the username is unique
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Check if username already exists
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Get a user by their username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    // Get a list of all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by their ID
    public String deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return "User with ID: " + id + " has been deleted successfully.";
        } else {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
    }


    public long countAllUsers() {
        return userRepository.count();
    }





    // Validate user login by checking username and matching password
    public User validateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Compare the password with the encoded password in the database
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;  // Return user object upon successful validation
            } else {
                throw new IllegalArgumentException("Invalid password");
            }
        } else {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
    }
}
