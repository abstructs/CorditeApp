package com.cordite.cordite.Run;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;

import java.util.List;

public class RunGraphViewActivity extends AppCompatActivity {
    private Run run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_graph_view);
        setup();
        populateFields();

    }

    private void setup(){
        final Run run = getIntent().getParcelableExtra("run");
        this.run = run;
    }


    private void populateFields() {

        List<Location> locations = run.locations;
        String time = Long.toString(locations.get(0).getTime());
        TextView graphTxt = findViewById(R.id.graphTxtView);

        graphTxt.setText(time);
    }

}
