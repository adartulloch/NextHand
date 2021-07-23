package com.example.nexthand.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Contact")
public class Contact extends ParseObject implements Serializable {
    public static final String KEY_USER = "user";
    public static final String KEY_RECIPIENT = "recipient";

    public Contact () {}

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user) { put(KEY_USER, user); }

    public ParseUser getRecipient() {
        return getParseUser(KEY_RECIPIENT);
    }

    public void setRecipient(ParseUser recipient) {
        put(KEY_RECIPIENT, recipient);
    }
}
