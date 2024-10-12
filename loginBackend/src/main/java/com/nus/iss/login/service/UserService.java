package com.nus.iss.login.service;

import com.nus.iss.login.entity.User;
import com.nus.iss.login.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // This method is responsible for user authentication
    public Optional<User> authenticateUser(String username, String password) throws UsernameNotFoundException, InvalidCredentialsException {
        Optional<User> user = userRepository.findByUsername(username);

        // Check if user exists
        if (!user.isPresent()) {
            // Throw a custom exception if the user is not found
            throw new UsernameNotFoundException("User not found");
        }

        // Check if the password matches
        if (!user.get().getUserPassword().equals(password)) {
            // Throw a custom exception if the password is incorrect
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return user; // If everything is fine, return the user
    }

    public User handleGuestLogin() {
        // Create or retrieve a guest user
        // Example: You might have a predefined user with ID 4 representing a guest
        Optional<User> guestUser = userRepository.findById(4L);
        if (guestUser.isPresent()) {
            return guestUser.get();
        } else {
            return null;
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
