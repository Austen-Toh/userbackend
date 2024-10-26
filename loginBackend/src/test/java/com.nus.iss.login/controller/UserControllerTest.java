package com.nus.iss.login.controller;

import com.nus.iss.login.entity.User;
import com.nus.iss.login.entity.UserProfile;
import com.nus.iss.login.service.InvalidCredentialsException;
import com.nus.iss.login.service.UserService;
import com.nus.iss.login.service.UsernameNotFoundException;
import com.nus.iss.login.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserProfileService userProfileService;

    @Test
    void testLoginSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setUserID(1);

        when(userService.authenticateUser("testuser", "password")).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        when(userService.authenticateUser("testuser", "password")).thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"password\":\"password\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        when(userService.authenticateUser("testuser", "wrongpassword")).thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testGuestLoginSuccess() throws Exception {
        User guestUser = new User();
        guestUser.setUsername("guest");

        when(userService.handleGuestLogin()).thenReturn(guestUser);

        mockMvc.perform(get("/api/auth/guest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("guest"));
    }

    @Test
    void testGetUserProfileByUsernameSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");

        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/auth/profile/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testGetUserProfileByUsernameNotFound() throws Exception {
        when(userService.getUserByUsername("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/profile/username/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with username: nonexistent"));
    }

    @Test
    void testGetUserProfileByUserIdSuccess() throws Exception {
        UserProfile mockProfile = new UserProfile();
        mockProfile.setProfileName("testuser");

        when(userProfileService.getUserProfileByUserId(1)).thenReturn(Optional.of(mockProfile));

        mockMvc.perform(get("/api/auth/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileName").value("testuser"));
    }

    @Test
    void testGetUserProfileByUserIdNotFound() throws Exception {
        when(userProfileService.getUserProfileByUserId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/profile/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User profile not found for user ID: 999"));
    }
}
