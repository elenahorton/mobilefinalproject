package com.example.elenahorton.mobilefinalproject.model;

import android.location.Location;

/**
 * Created by elenahorton on 12/9/16.
 */
public class User {

    private String email;
    private String userName;
    private Location location;

    public User(){}

    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
        this.location = null;
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

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
}
