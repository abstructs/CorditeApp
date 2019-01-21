package com.cordite.cordite.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import retrofit2.Response;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.RunService;
import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;
import com.cordite.cordite.Run.RunManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    private RunManager runManager;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private RunService runService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupToolbar();

        this.runService = APIClient.getClient().create(RunService.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.wtf("map", "Map was null");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
    }

    public int indexOf(String element, String[] items) {
        int i = 0;
        for(String item : items) {
            if(item.equals(element))
                return i;

            i++;
        }

        return -1;
    }

    private void goToMyLocation() {
        getLastKnownLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if(location != null) {
                    animateMapCameraToLocation(task.getResult());
                } else {
                    Log.wtf("map", "couldn't find location");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean locationPermissionGranted = false;

        for(String provider : getLocationProvider()) {
            int providerIndex = indexOf(provider, permissions);

            if(providerIndex == -1)
                return;

            if(grantResults[providerIndex] == PermissionChecker.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                break;
            }
        }

        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(locationPermissionGranted) {
                    setupMap();
                    goToMyLocation();
                }
                break;
        }
    }

    private Task<Location> getLastKnownLocation() throws SecurityException {
        return mFusedLocationClient.getLastLocation();
    }

    private void animateMapCameraToLocation(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18f));
    }

//    private void moveMapCameraToLocation(Location location) {
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
//    }

    private String[] getLocationProvider() {
        return new String[]{ Manifest.permission.ACCESS_FINE_LOCATION};
    }

    private void requestLocationPermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(getLocationProvider(),
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, getLocationProvider(),
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

//    private LocationCallback getLocationCallback() {
//        return new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                Location location = locationResult.getLastLocation();
//                if(location != null)
//                    updatePath(location);
//            }
//        };
//    }

//    private void setupTracker() throws SecurityException {
//        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), getLocationCallback(), Looper.myLooper());
//    }

    private void startTracking() {
        FloatingActionButton trackFab = findViewById(R.id.trackFab);

        trackFab.setImageDrawable(getDrawable(R.drawable.ic_stop));
        runManager.startTracking();
    }

    private void stopTracking() {
        FloatingActionButton trackFab = findViewById(R.id.trackFab);

        trackFab.setImageDrawable(getDrawable(R.drawable.ic_play));
        runManager.stopTracking();
    }

    private void setupButtons() {
        final FloatingActionButton trackFab = findViewById(R.id.trackFab);

        trackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(runManager.trackingEnabled()) {
                    showStopTrackingDialog();
                } else {
                    startTracking();
                }
            }
        });
    }

    private Run getRun() {
        Run run = new Run();

        run.locations = runManager.getLocationStack();
        run.averageSpeed = 0;
        run.timeElapsed = 0;
        run.distanceTravelled = 0;
        run.rating = 0;

        return run;
    }

    private String getToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        return preferences.getString("token", "");
    }

    private void saveRunAndExit() {
        final Run run = getRun();

        class SaveRun extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Response<JsonObject> response = runService.saveRun(getToken(), run).execute();

                    System.out.println(response);

                } catch(IOException e) {
                    System.out.println("Bad request");
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                stopTracking();
            }
        }

        new SaveRun().execute();
    }

    private void showStopTrackingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        builder.setMessage("Done with your run?")
                .setPositiveButton("Yes, save and exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveRunAndExit();
                    }
                })
                .setNegativeButton("No, continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void setupTracker(GoogleMap mMap) {
        runManager = new RunManager(getSupportFragmentManager(), mFusedLocationClient, mMap);
    }

    private void setupMap() throws SecurityException {
        mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style));
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            setupButtons();
            setupTracker(mMap);
            setupMap();

            goToMyLocation();
        } catch(SecurityException e) {
            requestLocationPermissions();
        }
    }

    private void setupToolbar() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

        setSupportActionBar(bottomAppBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);

        return true;
    }
}
