package com.example.nexthand.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Contact")
public class Contact extends ParseObject {
    public static final String KEY_USER = "user";

    public Contact () {}

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user) {
        put(KEY_USER, user);
    }
}
