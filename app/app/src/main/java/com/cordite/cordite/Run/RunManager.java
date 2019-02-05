package com.cordite.cordite.Run;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.cordite.cordite.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;

import java.util.Stack;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

// Handle collecting and visualizing run data
public class RunManager {
    private RunVisualizer visualizer;
    private RunTracker tracker;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback locationCallback;

    private boolean trackingEnabled;

    private final int LOCATION_UPDATE_INTERVAL = 2500;

//    private Context context;

    public RunManager(FragmentManager fragmentManager, FusedLocationProviderClient mFusedLocationClient, GoogleMap mMap) {
        this.mFusedLocationClient = mFusedLocationClient;

        this.visualizer = new RunVisualizer(fragmentManager, mMap);
        this.tracker = new RunTracker();

        this.trackingEnabled = false;
    }

    private boolean getTrackingEnabled() {
        return this.trackingEnabled;
    }

    public boolean trackingEnabled() {
        return getTrackingEnabled();
    }

    public void startTracking() {
        this.trackingEnabled = true;
        setupTracker();
    }

    public void stopTracking() {
        this.trackingEnabled = false;
        teardownTracker();
    }

    public Stack<Location> getLocationStack() {
        return this.visualizer.getLocationStack();
    }

    private void handleLocationUpdate(Location location) {
        visualizer.update(location);
    }

    private LocationCallback getLocationCallback() {

        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if(location != null) {
                    handleLocationUpdate(location);
                }
            }
        };
    }

    private LocationRequest getLocationRequest() {
        LocationRequest request = LocationRequest.create();

        request.setInterval(LOCATION_UPDATE_INTERVAL);

        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        return request;
    }

    private void setupTracker() throws SecurityException {
        locationCallback = getLocationCallback();
        visualizer.startTimer();
        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.myLooper());
    }

    private void teardownTracker() {
        visualizer.clearPath();
        visualizer.stopTimer();
        visualizer.setDistance(0);
        visualizer.setSpeedTxt(0);

        if(locationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
