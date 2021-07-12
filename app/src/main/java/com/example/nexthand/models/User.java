package com.example.nexthand.models;

public class User {

    /* Here are the keys necessary for database retrieval. We don't actually need to define the keys "objectId", "createdAt",
    and "updatedAt" as they are default fields provided by methods defined in the Parse Library */

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_EMAIL_ADDRESS = "emailAddress";

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        return put(KEY_USERNAME, username);
    }

    public ParseFile getProfileImage() {
        return getString("profileImage");
    }

    public void setProfileImage(ParseFile profileImage) {
        return put(KEY_PROFILE_IMAGE, profileImage);
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
