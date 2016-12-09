package com.example.elenahorton.mobilefinalproject.model;

/**
 * Created by elenahorton on 12/9/16.
 */
import android.graphics.Bitmap;
import android.location.Location;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String uid;
    private String author;
    private String description;
    private String category;
    private Bitmap image;
    private int costRating;
    private Location location;

    public Post() {
    }

    public Post(String uid, String author, String description, String category, Bitmap image, int costRating, Location location) {
        this.uid = uid;
        this.author = author;
        this.category = category;
        this.description = description;
        this.image = image;
        this.costRating = costRating;
        this.location = location;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("description", description);
        result.put("category", category);
        result.put("image", image);
        result.put("costRating", costRating);
        result.put("location", location);
        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getCostRating() {
        return costRating;
    }

    public void setCostRating(int costRating) {
        this.costRating = costRating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}