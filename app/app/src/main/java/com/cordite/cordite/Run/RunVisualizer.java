package com.cordite.cordite.Run;

import android.location.Location;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.cordite.cordite.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

class RunVisualizer {
    private Polyline polylinePath;
    private View fragmentView;

    private float distance;
    private float speed;

    private CountDownTimer timer;

    private final int MAX_TIMER_AMOUNT = 3600000;

    Stack<Location> locationStack;

    RunVisualizer(FragmentManager fragmentManager, GoogleMap mMap) {
        RunDataFragment runDataFragment = (RunDataFragment) fragmentManager.findFragmentById(R.id.runDataFragment);

        fragmentView = runDataFragment.getView();

        setupTimer();

        polylinePath = mMap.addPolyline(new PolylineOptions()
            .color(fragmentView.getContext().getColor(R.color.pathColour))
            .width(12));

        locationStack = new Stack<>();

        setDistance(0);
    }

    void setDistance(float distance) {
        this.distance = distance;
    }

//    void setSpeed(float speed) {
//        this.speed = speed;
//    }

    void setSpeedTxt(float speed) {
        TextView speedTxt = fragmentView.findViewById(R.id.speedTxt);

        speedTxt.setText((String.valueOf(speed) + " km/h"));
    }

    void update(Location location) {
        locationStack.push(location);

        updateSpeed(location);
        updateDistance(location);
        updatePolylinePoints(location);
    }

//    private String getTime(long milliseconds) {
//
//    }

    private String zeroPad(long num, int padding) {
        return String.format(Locale.getDefault(), "%0" + padding +"d", num);
    }

    private void setupTimer() {
        final TextView timerTxt = fragmentView.findViewById(R.id.timerTxt);

        timer = new CountDownTimer(MAX_TIMER_AMOUNT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long timeElapsed = MAX_TIMER_AMOUNT - millisUntilFinished;

                String seconds = zeroPad((timeElapsed / 1000) % 60, 2);
                String minutes = zeroPad((timeElapsed / (1000 * 60)) % 60, 2);
                String hours = zeroPad((timeElapsed / (1000 * 60 * 60)) % 24, 2);

                String timeStr = hours + ":" + minutes + ":" + seconds;

//                StringBuilder timeStrBuilder = new StringBuilder();

//                timeStrBuilder.append(String.format(Locale.getDefault(), "%04d", time));

//                timeStrBuilder.insert(2, ":");

                timerTxt.setText(timeStr);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    Stack<Location> getLocationStack() { return this.locationStack; }

    void startTimer() {
        timer.start();
    }

    void stopTimer() {
        timer.cancel();
    }

    private void updateDistance(Location location) {
        TextView distanceTxt = fragmentView.findViewById(R.id.distanceTxt);
        List<LatLng> points = polylinePath.getPoints();

        if(points.size() > 0) {
            LatLng lastPoint = points.get(points.size() - 1);

            float[] results = new float[1];

            Location.distanceBetween(lastPoint.latitude, lastPoint.longitude,
                    location.getLatitude(), location.getLongitude(),
                    results);

            distance += results[0];

            String distanceStr = String.format(Locale.getDefault(),"%.1f m", distance);

            distanceTxt.setText(distanceStr);
        }
    }

    private void updateSpeed(Location location) {
        TextView speedTxt = fragmentView.findViewById(R.id.speedTxt);
        speed = location.getSpeed();

        speedTxt.setText((String.valueOf(speed) + " km/h"));
    }

    private void updatePolylinePoints(Location location) {
        List<LatLng> points = polylinePath.getPoints();

        points.add(new LatLng(location.getLatitude(), location.getLongitude()));

        polylinePath.setPoints(points);
    }

    void clearPath() {
        polylinePath.setPoints(new ArrayList<LatLng>());
    }
}
