package com.cordite.cordite.Run;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.RunService;
import com.cordite.cordite.Deserializers.RunDeserializer;
import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunGraphViewActivity extends AppCompatActivity {

    private RunService runService;
    private  LineChart chart;
    private LineData lineData;
    private List<Entry> entries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_graph_view);
        setup();
    }

    private void setup(){
        chart = (LineChart) findViewById(R.id.chart);

        this.runService = APIClient.getClient().create(RunService.class);

//        Button weekViewBtn = (Button) findViewById(R.id.weekViewBtn);
//        Button monthViewBtn = (Button) findViewById(R.id.monthViewBtn);
        Button allViewBtn = (Button) findViewById(R.id.allViewBtn);

//        weekViewBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View ref) {
//                graphRuns("week");
//            }
//        });
//
//        monthViewBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View ref) {
//                graphRuns("month");
//
//            }
//        });

        allViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                graphRuns("all");
            }
        });
    }

    private void graphRuns(String timeframe) {
        clearGraph(); // clear previous graph

        Call<JsonArray> request = runService.getUserRuns(getToken());

        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.code() == 401) {
                    Toast.makeText(RunGraphViewActivity.this, "Error Retrieving runs", Toast.LENGTH_SHORT).show();
                } else {

                    ArrayList<Run> runs = convertJsonToRuns(response.body().getAsJsonArray());
                    createChart(runs);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(RunGraphViewActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createChart(ArrayList<Run> data){

        lineData = populateEntries(data); //get new data each time
        lineData.setValueTextColor(Color.WHITE);
        chart.setData(lineData);
        chart.notifyDataSetChanged();
        chart.invalidate();

    }

    private LineData populateEntries(ArrayList<Run> data) {

        int size =data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int)data.get(i).averageSpeed;

            Entry point = new Entry(x,y);

            entries.add(point);
        }

        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, "Time vs Speed"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(0);

        lineData = new LineData(dataSet);
        return lineData;
    }

    private void clearGraph(){
        chart.invalidate();
        chart.clear();

    }

    private String getToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        return preferences.getString("token", "");
    }

    private ArrayList<Run> convertJsonToRuns(JsonArray jsonArray) {
        ArrayList<Run> runs = new ArrayList<>();

        RunDeserializer deserializer = new RunDeserializer();

        for(JsonElement element : jsonArray) {

            Run run = deserializer.deserialize(element, Run.class, null);
            runs.add(run);
        }

        return runs;
    }

}
