package com.nus.iss.login.service;

import com.nus.iss.login.entity.User;
import com.nus.iss.login.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void authenticateUser_Success() throws UsernameNotFoundException, InvalidCredentialsException {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setUserPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> authenticatedUser = userService.authenticateUser("testuser", "password");

        // Assert
        assertTrue(authenticatedUser.isPresent());
        assertEquals("testuser", authenticatedUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void authenticateUser_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException thrown = assertThrows(
            UsernameNotFoundException.class,
            () -> userService.authenticateUser("unknownuser", "password")
        );

        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownuser");
    }

    @Test
    void authenticateUser_InvalidCredentials() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setUserPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Act & Assert
        InvalidCredentialsException thrown = assertThrows(
            InvalidCredentialsException.class,
            () -> userService.authenticateUser("testuser", "wrongpassword")
        );

        assertEquals("Invalid credentials", thrown.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void handleGuestLogin_GuestUserExists() {
        // Arrange
        User guestUser = new User();
        guestUser.setUserID(4);
        guestUser.setUsername("guest");

        when(userRepository.findById(4)).thenReturn(Optional.of(guestUser));

        // Act
        User result = userService.handleGuestLogin();

        // Assert
        assertNotNull(result);
        assertEquals("guest", result.getUsername());
        verify(userRepository, times(1)).findById(4);
    }

    @Test
    void handleGuestLogin_GuestUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(4)).thenReturn(Optional.empty());

        // Act
        User result = userService.handleGuestLogin();

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(4);
    }

    @Test
    void getUserByUsername_UserExists() {
        // Arrange
        User mockUser = new User();
        mockUser.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userService.getUserByUsername("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_UserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserByUsername("nonexistent");

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}
