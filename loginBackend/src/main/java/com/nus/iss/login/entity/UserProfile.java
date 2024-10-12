package com.nus.iss.login.entity;

import javax.persistence.*;
import com.nus.iss.login.entity.UserProfile;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileID;

    private String profileName;
    private String email;
    private String phone;

    @OneToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    // Getters and Setters
    public Long getUserProfileId() {
        return userProfileID;
    }

    public void setUserProfileId(Long userProfileID) {
        this.userProfileID = userProfileID;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
