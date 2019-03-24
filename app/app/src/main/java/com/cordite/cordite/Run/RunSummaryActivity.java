package com.cordite.cordite.Run;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;

public class RunSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_summary);

        Run run = getIntent().getParcelableExtra("run");

        setup();
        populateFields(run);
    }

    private void setup() {
        Button doneBtn = findViewById(R.id.doneBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void populateFields(Run run) {

        TextView durationTxt = findViewById(R.id.durationTxt);
        TextView distanceTxt = findViewById(R.id.distanceTxt);
        TextView speedTxt = findViewById(R.id.speedTxt);

        distanceTxt.setText(String.valueOf(run.distanceTravelled) + " km");
        durationTxt.setText(run.timeElapsed + " ms");
        speedTxt.setText(String.valueOf(run.averageSpeed) + " km/s");
    }
}
