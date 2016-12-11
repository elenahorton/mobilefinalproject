package com.example.elenahorton.mobilefinalproject.location;

/**
 * Created by elenahorton on 12/10/16.
 */
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


public class PostLocationManager implements LocationListener {

    public interface OnLocChanged {
        void locationChanged(Location location);
        Location getUserLocation();
    }

    private Context context;
    private LocationManager locMan;
    private OnLocChanged onLocChanged;

    public PostLocationManager(Context aContext, OnLocChanged onLocChanged) {
        context = aContext;
        this.onLocChanged = onLocChanged;
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    public void startLocationMonitoring() {
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public void stopLocationMonitoring() {
        try {
            locMan.removeUpdates(this);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public LocationManager getLocationManager(){
        return locMan;
    }

    @Override
    public void onLocationChanged(Location location) {
        onLocChanged.locationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}