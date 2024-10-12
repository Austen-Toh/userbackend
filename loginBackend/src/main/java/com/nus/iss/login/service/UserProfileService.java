package com.nus.iss.login.service;

import com.nus.iss.login.entity.UserProfile;
import com.nus.iss.login.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Optional<UserProfile> getUserProfileByUserId(Long userId) {
        return userProfileRepository.findByUserUserID(userId);
    }
}
