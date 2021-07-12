package com.example.nexthand.models;

public class UserToy {

    /* Simplified model of Items to be used for testing and work on various algorithms */

    private String username;
    private String profileImage;
    private String phoneNumber;
    private String emailAddress;

    public UserToy(String username, String profileImage, String phoneNumber, String emailAddress) {
        this.username = username;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToyProfileImage() {
        return profileImage;
    }

    public void setToyProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
