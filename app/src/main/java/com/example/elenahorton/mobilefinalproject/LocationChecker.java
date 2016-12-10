package com.example.elenahorton.mobilefinalproject;

import java.util.ArrayList;

/**
 * Created by elladzenitis on 12/10/16.
 */
public interface LocationChecker {
    public ArrayList<String> getValidLocations();
    public void getPostsByLocation();
    public boolean getLocationReady();
}
