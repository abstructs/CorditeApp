package com.cordite.cordite.Map;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

class RunVisualizer {
    private Polyline polylinePath;

//    private GoogleMap mMap;

    RunVisualizer(GoogleMap mMap) {
        polylinePath = mMap.addPolyline(new PolylineOptions()
        .color(Color.BLUE)
        .width(4));

//        this.mMap = mMap;
    }

    void updatePath(Location location) {
        List<LatLng> points = polylinePath.getPoints();

        points.add(new LatLng(location.getLatitude(), location.getLongitude()));

        polylinePath.setPoints(points);
    }

    void clearPath() {
        polylinePath.setPoints(new ArrayList<LatLng>());
    }
}
