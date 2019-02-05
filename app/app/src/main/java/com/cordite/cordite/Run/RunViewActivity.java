package com.cordite.cordite.Run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cordite.cordite.Home.HomeActivity;
import com.cordite.cordite.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RunViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_view);

        populateFields(getIntent());

    }

    private void populateFields(Intent intent) {

        String dateTxt = intent.getStringExtra("runViewDateTxt");
        String distanceTxt = intent.getStringExtra("runViewDistanceTxt");
        String averageSpeed = intent.getStringExtra("runViewAvgSpeedTxt");
        String timeElapsed = intent.getStringExtra("runViewTimeTxt");

        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView avgSpeedDistanceView = findViewById(R.id.distanceAvgSpeedTextView);
        TextView timeElapsedView = findViewById(R.id.runViewTimeTextView);

        String avgMessage = "You traveled a distance of " + distanceTxt + " KM, at an average speed of " + averageSpeed + " KM/h";
        String timeMessage = "The journey took you " + timeElapsed;

        dateTextView.setText(dateTxt);
        avgSpeedDistanceView.setText(avgMessage);
        timeElapsedView.setText(timeMessage);
    }
}
