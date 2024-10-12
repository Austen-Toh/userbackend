package com.nus.iss.login.controller;

import com.nus.iss.login.entity.User;
import com.nus.iss.login.service.InvalidCredentialsException;
import com.nus.iss.login.service.UserService;
import com.nus.iss.login.service.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nus.iss.login.service.UserProfileService;
import com.nus.iss.login.entity.UserProfile;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final UserProfileService userProfileService;

    public UserController(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            // Attempt to authenticate the user
            Optional<User> authenticatedUser = userService.authenticateUser(username, password);

            // If authentication is successful, return the user
            return ResponseEntity.ok(authenticatedUser.get());

        } catch (UsernameNotFoundException e) {
            // Return 404 if the user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (InvalidCredentialsException e) {
            // Return 401 if the password is incorrect
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            // Return 500 for any other internal server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/guest")
    public ResponseEntity<?> guestLogin() {
        try {
            // Calling the service to handle guest login logic
            User guestUser = userService.handleGuestLogin();
            return ResponseEntity.ok(guestUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during guest login.");
        }
    }

    @GetMapping("/profile/username/{username}")
    public ResponseEntity<?> getUserProfileByUsername(@PathVariable String username) {
        try {
            System.out.println("Received request to get profile for username: " + username);
            Optional<User> user = userService.getUserByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with username: " + username);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the user profile.");
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileByUserId(userId);
        if (userProfile.isPresent()) {
            return ResponseEntity.ok(userProfile.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found for user ID: " + userId);
        }
    }
}
