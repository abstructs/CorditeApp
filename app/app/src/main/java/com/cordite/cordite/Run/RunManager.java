package com.cordite.cordite.Run;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.cordite.cordite.Map.MapsActivity;
import com.cordite.cordite.Map.TrackerService;
import com.cordite.cordite.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

    PendingIntent pendingIntent;

    private Context context;

    public RunManager(Context context, FragmentManager fragmentManager, FusedLocationProviderClient mFusedLocationClient, GoogleMap mMap) {
        this.mFusedLocationClient = mFusedLocationClient;

        this.visualizer = new RunVisualizer(fragmentManager, mMap);
        this.tracker = new RunTracker();

        this.trackingEnabled = false;
        this.context = context;
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

    public void handleLocationUpdate(Location location) {
        System.out.println("location updated");
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

        Intent intent = new Intent(TrackerService.ACTION_PROCESS_UPDATE);

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), pendingIntent);
    }

    private void teardownTracker() {
        visualizer.clearPath();
        visualizer.stopTimer();
        visualizer.setDistance(0);
        visualizer.setSpeedTxt(0);

        if(locationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

        mFusedLocationClient.removeLocationUpdates(pendingIntent);
    }
}
