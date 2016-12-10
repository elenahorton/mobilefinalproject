package com.example.elenahorton.mobilefinalproject.model;

import android.location.Location;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by elenahorton on 12/9/16.
 */
public class User {

    private String email;
    private String userName;
    private ArrayList<String> userPosts;

    public User(){}

    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
        this.userPosts = new ArrayList<String>();
        userPosts.add(null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public Location getLocation() {
//        return location;
//    }
//    public void setLocation(Location location) {
//        this.location = location;
//    }

    public ArrayList<String> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(ArrayList<String> userPosts) {
        this.userPosts = userPosts;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postList", userPosts);
        return result;
    }
}
