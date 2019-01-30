package com.cordite.cordite.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.ReportService;
import com.cordite.cordite.Api.RunService;
import com.cordite.cordite.Deserializers.ReportDeserializer;
import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;
import com.cordite.cordite.Report.ReportSelectFragment;
import com.cordite.cordite.Report.ReportShowFragment;
import com.cordite.cordite.Report.ReportType;
import com.cordite.cordite.Run.RunManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    private RunManager runManager;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private RunService runService;
    private ReportService reportService;

    private Fragment showReportFragment;
    private Fragment selectReportFragment;
    private Fragment runDataFragment;

    private Polygon circleHighlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupToolbar();

        this.runService = APIClient.getClient().create(RunService.class);
        this.reportService = APIClient.getClient().create(ReportService.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.wtf("map", "Map was null");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
    }

//    private void setupFragments() {
//        FrameLayout showReportLayout = findViewById(R.id.reportShowLayout);
//
////        showReportLayout.setY(-100);
//    }

    private void addReportToMap(Report report) {
        MarkerOptions markerOptions = new MarkerOptions();

        Location location = report.location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        markerOptions.position(latLng);

        markerOptions.icon(report.getIcon(MapsActivity.this));

//        markerOptions

        Marker marker = mMap.addMarker(markerOptions);

        marker.setTag(report);
    }

    private void getReportsAndAddToMap(final Location location) {
        Call<JsonArray> request = reportService.getReports(getToken(), location);

        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                ReportDeserializer reportDeserializer = new ReportDeserializer();
                JsonArray body = response.body();

                for(JsonElement element : body) {
                    Report report = reportDeserializer.deserialize(element, Report.class, null);

                    addReportToMap(report);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private int indexOf(String element, String[] items) {
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
                    animateMapCameraToLocation(location);
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16f));
    }

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

    public void saveReport(final ReportType reportType) {
        Task<Location> task = getLastKnownLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    Report report = new Report();

                    report.location = location;
                    report.type = reportType;

                    saveReportAndAddToMap(report);
                }
            }
        });
    }

    private void saveReportAndAddToMap(final Report report) {
        Call<JsonObject> request = reportService.saveReport(getToken(), report);

        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonElement reportObj = response.body().get("report");

                ReportDeserializer reportDeserializer = new ReportDeserializer();

                Report report = reportDeserializer.deserialize(reportObj, Report.class, null);

                addReportToMap(report);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Network error! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

        return run;
    }

    private String getToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        return preferences.getString("token", "");
    }

    private void saveRunAndExit(final Run run) {
        System.out.println(run);

        Call<JsonObject> request = runService.saveRun(getToken(), run);

        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                stopTracking();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO: save locally for upload later
                Toast.makeText(MapsActivity.this, "Network error! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStopTrackingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        builder.setMessage("Done with your run?")
                .setPositiveButton("Yes, save and exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveRunAndExit(getRun());
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

    private void hideRunDataFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        final View view = fragmentManager.findFragmentById(R.id.runDataFragment).getView();

        view.animate().alpha(0).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void showRunDataFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        final View view = fragmentManager.findFragmentById(R.id.runDataFragment).getView();

        view.animate().alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void showReportFragment(Report report) {
        hideRunDataFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ReportShowFragment fragment = ReportShowFragment.newInstance(report);

        this.showReportFragment = fragment;

        transaction.setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top, R.animator.slide_in_top, R.animator.slide_out_top);
        transaction.replace(R.id.reportShowLayout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    // Taken from StackOverflow
    // Link: https://stackoverflow.com/a/42509627
    private Iterable<LatLng> drawHole(LatLng center, double radius) {
        int points = 50; // number of corners of inscribed polygon

        final float EARTH_RADIUS = 6371;

        double radiusLatitude = Math.toDegrees(radius / EARTH_RADIUS);
        double radiusLongitude = radiusLatitude / Math.cos(Math.toRadians(center.latitude));

        List<LatLng> result = new ArrayList<>(points);

        double anglePerCircleRegion = 2 * Math.PI / points;

        for (int i = 0; i < points; i++) {
            double theta = i * anglePerCircleRegion;
            double latitude = center.latitude + (radiusLatitude * Math.sin(theta)) + 0.000175;
            double longitude = center.longitude + (radiusLongitude * Math.cos(theta));

            result.add(new LatLng(latitude, longitude));
        }

        return result;
    }

    // Taken from StackOverflow
    // Link: https://stackoverflow.com/a/42509627
    private List<LatLng> drawBoundaries() {
        final float delta = 0.01f;

        return new ArrayList<LatLng>() {{
            add(new LatLng(90 - delta, -180 + delta));
            add(new LatLng(0, -180 + delta));
            add(new LatLng(-90 + delta, -180 + delta));
            add(new LatLng(-90 + delta, 0));
            add(new LatLng(-90 + delta, 180 - delta));
            add(new LatLng(0, 180 - delta));
            add(new LatLng(90 - delta, 180 - delta));
            add(new LatLng(90 - delta, 0));
            add(new LatLng(90 - delta, -180 + delta));
        }};
    }

    private void removeCircleHighlight() {
        if(circleHighlight != null) {
            circleHighlight.remove();
            circleHighlight = null;
        }
    }

    private void addCircleHighlight(Location center) {
        PolygonOptions polygonOptions = new PolygonOptions();

        polygonOptions.fillColor(getColor(R.color.overlay));
        polygonOptions.addAll(drawBoundaries());
        polygonOptions.addHole(drawHole(new LatLng(center.getLatitude(), center.getLongitude()), 0.05));
        polygonOptions.strokeWidth(0);

        circleHighlight = mMap.addPolygon(polygonOptions);
    }

    private void setupMap() throws SecurityException {
        mMap.setMyLocationEnabled(true);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
            Report report = (Report) marker.getTag();

            if(report != null) {
                animateMapCameraToLocation(report.location);

                addCircleHighlight(report.location);
                showReportFragment(report);

                return true;
            }

            return false;
            }
        });

        Task<Location> task = getLastKnownLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                getReportsAndAddToMap(location);
            }
        });

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

    private void clearShowReportFragment() {
        showReportFragment = null;
        removeCircleHighlight();
    }

    @Override
    public void onBackPressed() {
        if(showReportFragment != null) {
            clearShowReportFragment();
            showRunDataFragment();
        } else if(selectReportFragment != null) {
            selectReportFragment = null;
        } else if(runManager.trackingEnabled()) {
            showStopTrackingDialog();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);

        return true;
    }

    private void showReportSelectionFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ReportSelectFragment fragment = ReportSelectFragment.newInstance();

        selectReportFragment = fragment;

        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.reportSelectLayout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sendReportItem:
                showReportSelectionFragment();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
