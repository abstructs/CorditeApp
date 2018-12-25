package com.cordite.cordite.Map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

class RunTracker {

    private List<LatLng> points;
//    private Polyline tracker;

    public RunTracker() {
        points = new ArrayList<>();
    }

    void addPoint(Location location) {
        points.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    void clear() {
        points = new ArrayList<>();
    }
}
