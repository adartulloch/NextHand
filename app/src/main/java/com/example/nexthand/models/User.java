package com.example.nexthand.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_EMAIL_ADDRESS = "emailAddress";
    public static final String KEY_FIRSTNAME = "firstname";

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public ParseFile getProfileImage() {
        return getParseFile("profileImage");
    }

    public void setProfileImage(ParseFile profileImage) {
        put(KEY_PROFILE_IMAGE, profileImage);
    }

    public String getPhoneNumber() {
        return getString(KEY_PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber) {
        put(KEY_PHONE_NUMBER, phoneNumber);
    }

    public String getEmailAddress() {
        return getString(KEY_EMAIL_ADDRESS);
    }

    public void setEmailAddress(String emailAddress) {
        put(KEY_EMAIL_ADDRESS, emailAddress);
    }
}
