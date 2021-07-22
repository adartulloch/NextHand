package com.example.nexthand.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Inquiry")
public class Inquiry extends ParseObject implements Serializable {
    public static final String KEY_ITEM = "item";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECIPIENT = "recipient";

    public Inquiry () {}

    public ParseObject getItem() {
        return getParseObject(KEY_ITEM);
    }

    public void setItem(Item item) {
        put(KEY_ITEM, item);
    }

    public ParseUser getSender() {
        return getParseUser(KEY_SENDER);
    }

    public void setSender(ParseUser sender) {
         put(KEY_SENDER, sender);
    }

    public ParseUser getRecipient() {
        return getParseUser(KEY_RECIPIENT);
    }

    public void setRecipient(ParseUser recipient) {
        put(KEY_RECIPIENT, recipient);
    }
}
