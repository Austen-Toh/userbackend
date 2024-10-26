package com.nus.iss.login.entity;

import javax.persistence.*;
import com.nus.iss.login.entity.UserProfile;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userProfileID")
    private Integer userProfileID;

    @Column(name = "profileName")
    private String profileName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    // Getters and Setters
    public Integer getUserProfileId() {
        return userProfileID;
    }

    public void setUserProfileId(Integer userProfileID) {
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
