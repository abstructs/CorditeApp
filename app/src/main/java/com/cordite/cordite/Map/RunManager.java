package com.cordite.cordite.Map;

import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;

// Handle collecting and visualizing run data
class RunManager {
    private RunVisualizer visualizer;
    private RunTracker tracker;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback locationCallback;

    private boolean trackingEnabled;

    private final int LOCATION_UPDATE_INTERVAL = 2500;

    RunManager(FusedLocationProviderClient mFusedLocationClient, GoogleMap mMap) {
        this.mFusedLocationClient = mFusedLocationClient;
        this.visualizer = new RunVisualizer(mMap);
        this.tracker = new RunTracker();
        this.trackingEnabled = false;
    }

    private boolean getTrackingEnabled() {
        return this.trackingEnabled;
    }

    boolean trackingEnabled() {
        return getTrackingEnabled();
    }

    void startTracking() {
        this.trackingEnabled = true;
        setupTracker();
    }

    void stopTracking() {
        this.trackingEnabled = false;
        teardownTracker();
    }

    private void handleLocationUpdate(Location location) {
        visualizer.updatePath(location);
        tracker.addPoint(location);
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

        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.myLooper());
    }

    private void teardownTracker() {
        visualizer.clearPath();

        if(locationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);

        }
    }

}
