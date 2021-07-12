package com.example.nexthand.models;

/* Simplified model of Items to be used for testing and work on various algorithms */

import org.json.JSONArray;

import java.io.File;

public class ItemToy {

    private String id;
    private String author;
    private File image;
    private String description;
    private boolean isDonation;
    private String latitude;
    private String longitude;
    private JSONArray inquiries;
    private String category;
    private boolean isAvailable;

    public ItemToy(String id, String author, File image, String description, boolean isDonation,
                   String latitude, String longitude, JSONArray inquiries, String category,
                   boolean isAvailable) {
        this.id = id;
        this.author = author;
        this.image = image;
        this.description = description;
        this.isDonation = isDonation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.inquiries = inquiries;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToyAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public File getToyImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDonation() {
        return isDonation;
    }

    public void setDonation(boolean donation) {
        isDonation = donation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public JSONArray getInquiries() {
        return inquiries;
    }

    public void setInquiries(JSONArray inquiries) {
        this.inquiries = inquiries;
    }

    public String getToyCategory() {
        return category;
    }

    public void setToyCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
