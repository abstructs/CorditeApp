package com.cordite.cordite.Map;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.cordite.cordite.Run.RunManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

public class TrackerService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.cordite.cordite.Map.UPDATE_LOCATION";
    private RunManager runManager;

    @Override
    public void onReceive(Context context, Intent intent) {
//        System.out.println("received");

        if(intent != null) {
            final String action = intent.getAction();

            if(ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);

                if(result != null) {
                    Location location = result.getLastLocation();

                    Toast.makeText(context,  String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();

                    runManager.handleLocationUpdate(location);
//                    System.out.println(location);
//                    System.out.println("holy shit");
                }
            }
        }
    }

//    private final IBinder binder = new LocalBinder();
//    public RunManager runManager;
//    private final int ONGOING_NOTIFICATION_ID = 1;
//    private FusedLocationProviderClient mFusedLocationClient;

//    public class LocalBinder extends Binder {
//        TrackerService getService() {
//            // Return this instance of LocalService so clients can call public methods
//
////            TrackerService.this.runManager = runManager;
//
//            return TrackerService.this;
//        }
//    }

//    public void setupFusedLocationClient(FragmentManager fragmentManager, GoogleMap mMap) {
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        runManager = new RunManager(this, fragmentManager, mFusedLocationClient, mMap);
//
////        mFusedLocationClient.
//    }

//    public Task<Location> getLastKnownLocation() throws SecurityException {
//        return mFusedLocationClient.getLastLocation();
//    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        setupNotificationChannel();
//
//        Notification notification = getNotification();
//
//        startForeground(1234, notification);
//
//        return super.onStartCommand(intent, flags, startId);
//    }

//    private Notification getNotification() {
//        Intent notificationIntent = new Intent(TrackerService.this, MapsActivity.class);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(TrackerService.this, 0,
//                notificationIntent, PendingIntent.FLAG_ONE_SHOT);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            return new Notification.Builder(TrackerService.this, "my_channel_01")
////                    .setSmallIcon(R.drawable.ic_launcher)
////                    .setContentText(getString(R.string.isRecording))
//                    .setContentIntent(pendingIntent).build();
//        } else {
//            return new Notification.Builder(TrackerService.this)
////                    .setSmallIcon(R.drawable.ic_launcher)
////                    .setContentText(getString(R.string.isRecording))
//                    .setContentIntent(pendingIntent).build();
//        }
//    }

//    private void setupNotificationChannel() {
//
////        int NOTIFICATION_ID = 234;
//
//        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            String CHANNEL_ID = "my_channel_01";
//            CharSequence name = "my_channel";
//            String Description = "This is my channel";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mChannel.setDescription(Description);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mChannel.setShowBadge(false);
//            notificationManager.createNotificationChannel(mChannel);
//        }

//        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_LOW;
//            NotificationChannel notificationChannel = new NotificationChannel("my_channel_01", "my_channel", importance);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "my_channel_01");
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
//        notificationManager.notify((int)(System.currentTimeMillis()/1000), mBuilder.build());
//    }

    public TrackerService(final RunManager runManager) {
        this.runManager = runManager;
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }

//    public void startTracking() {
//        runManager.startTracking();
//    }
//
//    public void stopTracking() {
//        runManager.stopTracking();
//    }
}
