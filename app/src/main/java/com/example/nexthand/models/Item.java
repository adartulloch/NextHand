 package com.example.nexthand.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import org.json.JSONArray;

@ParseClassName("Item")
public class Item extends ParseObject {

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_ISDONATION = "donation";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_INQUIRIES = "inquiries";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_ISAVAILABLE = "isAvailable";

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author) {
        put(KEY_AUTHOR, author);
    }
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public boolean getDonation() {
        return getBoolean(KEY_ISDONATION);
    }

    public void setDonation(Boolean isDonation) {
        put(KEY_ISDONATION, isDonation);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint point) {
        put(KEY_LOCATION, point);
    }

    public JSONArray getInquiries() {
        return getJSONArray(KEY_INQUIRIES);
    }

    public void setInquiries(JSONArray inquiries) {
        put(KEY_INQUIRIES, inquiries);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public boolean getIsAvailable() {
        return getBoolean(KEY_ISAVAILABLE);
    }

    public void setIsAvailable(Boolean isAvailable) {
        put(KEY_ISAVAILABLE, isAvailable);
    }
}
