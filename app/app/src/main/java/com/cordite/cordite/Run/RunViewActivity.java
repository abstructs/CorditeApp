package com.cordite.cordite.Run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cordite.cordite.Home.HomeActivity;
import com.cordite.cordite.R;

public class RunViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_view);

        Intent intent = getIntent();
        populateFields(intent);

    }

    private void populateFields(Intent intent) {

        String dateTxt = intent.getStringExtra("runViewDateTxt");
        String distanceTxt = intent.getStringExtra("runViewDistanceTxt");
        String averageSpeed = intent.getStringExtra("runViewAvgSpeedTxt");
        String timeElasped = intent.getStringExtra("runViewTimeTxt");

        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView avgSpeedDistanceView = findViewById(R.id.distanceAvgSpeed);
        TextView timeElaspedView = findViewById(R.id.runViewTimeTxt);

        String avgMessage = "You traveled a distance of "+distanceTxt +" KM, at an average speed of "+ averageSpeed+ " KM/h";
        String timeMessage = "The journey took you " + timeElasped;

        dateTextView.setText(dateTxt);
        avgSpeedDistanceView.setText(avgMessage);
        timeElaspedView.setText(timeMessage);
    }

}
