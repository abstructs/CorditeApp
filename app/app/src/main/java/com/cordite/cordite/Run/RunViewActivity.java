package com.cordite.cordite.Run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.Home.HomeActivity;
import com.cordite.cordite.MainActivity;
import com.cordite.cordite.Map.MapsActivity;
import com.cordite.cordite.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RunViewActivity extends AppCompatActivity {

    private Run run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_view);

        setup();
        populateFields();
    }

    private void setup() {
        final Run run = getIntent().getParcelableExtra("run");

        this.run = run;

        System.out.println(run.date);
        System.out.println(run.averageSpeed);
        System.out.println(run.distanceTravelled);
        System.out.println(run.timeElapsed);

        System.out.println(run.locations);

        Button viewBtn = findViewById(R.id.viewBtn);
        Button graphBtn = findViewById(R.id.graphs);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RunViewActivity.this, MapsActivity.class);

                intent.putExtra("viewMode", true);
                intent.putExtra("run", run);

                startActivity(intent);
            }
        });

        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RunViewActivity.this, RunGraphViewActivity.class);

                intent.putExtra("run", run);

                startActivity(intent);
            }
        });
    }

    private void populateFields() {
        String date = run.date;
        String distance = String.valueOf(run.distanceTravelled);
        String averageSpeed = String.valueOf(run.averageSpeed);
        String timeElapsed = String.valueOf(run.timeElapsed);


        TextView dateTxt = findViewById(R.id.dateTxt);
        TextView infoTxt = findViewById(R.id.infoTxt);
        TextView timeElapsedTxt = findViewById(R.id.timeElapsedTxt);

        long minutesRan = TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(timeElapsed));

        String avgMessage = "You traveled a distance of " + distance + " KM, at an average speed of " + averageSpeed + " KM/h";
        String timeMessage = "The journey took you " + minutesRan + " minutes.";

        dateTxt.setText(date);
        infoTxt.setText(avgMessage);
        timeElapsedTxt.setText(timeMessage);
    }
}
