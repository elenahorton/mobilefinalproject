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
    private String imageUrl;
    private int costRating;
    private double lat;
    private double lng;

    public Post() {
    }

    //temporarily removed location and cost rating
    public Post(String uid, String author, String description, String category, String imageUrl, int costRating, double lat, double lng) {
//    public Post(String uid, String author, String description, String category, String imgUrl, int costRating, Location location) {
        this.uid = uid;
        this.author = author;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.costRating = costRating;
        this.lat = lat;
        this.lng = lng;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("description", description);
        result.put("category", category);
        result.put("imageUrl", imageUrl);
        result.put("costRating", costRating);
        result.put("lat", lat);
        result.put("lng", lng);
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

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCostRating() {
        return costRating;
    }

    public void setCostRating(int costRating) {
        this.costRating = costRating;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}