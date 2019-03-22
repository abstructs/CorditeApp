package com.cordite.cordite.Run;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.RunService;
import com.cordite.cordite.Deserializers.RunDeserializer;
import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.Entities.TimeFrame;
import com.cordite.cordite.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RunGraphViewFragment extends Fragment {

    private LineChart chart;
    private String des = "Weekly Progress";
    private RunService runService;

    private LineData lineData;
    private List<Entry> entries = new ArrayList<>();

    private static final int NUM_PAGES = 2;

    public enum TimeFrameType {
        ALL, MONTH, WEEK
    }

    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    private LineData avgSpeedEntries;
    private LineData avgDistanceEntries;

    private final int FIRST_PAGE = 0;
    private final int SECOND_PAGE = 1;

    private int text;
    private int lineColor;

    public void setdes(String des){
        this.des = des;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_run_graph_view_screen_slider, container, false);

        chart = rootView.findViewById(R.id.chart);

        enableGraph(TimeFrameType.WEEK,0);
        createColors();
        return rootView;
    }

    private void createColors(){

        text =  ContextCompat.getColor(getContext(), R.color.primaryText);
        lineColor = ContextCompat.getColor(getContext(), R.color.pathColour);
    }

    private LineChart getChart(){
        return chart;
    }


    public void enableGraph(TimeFrameType timeFrameType, int position){

        LineChart data = this.getChart();

        setupChart(data);

        graphRuns(timeFrameType,position);

    }
    private void setupChart(LineChart chart) {

        this.chart = chart; //set chart

        this.runService = APIClient.getClient().create(RunService.class); // set client

    }

    private void graphRuns(TimeFrameType timeFrameType, final int position) {
        clearGraph();

//        System.out.println(timeFrameType.toString());
        Call<JsonArray> request = runService.graphRuns(getToken(), new TimeFrame(timeFrameType.toString()));

        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (response.code() == 401) {
                    Toast.makeText(getContext(), "Error Retrieving runs", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Run> runs = convertJsonToRuns(response.body().getAsJsonArray());
                setChartData(runs, position);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setChartData(ArrayList<Run> data, int position) {
//        if(mPager == null){
//            lineData = populateTimeVsAvgSpeedEntries(data); //get new data each timeFrameType
//
//        }else {
            if (position == FIRST_PAGE) {
                lineData = populateTimeVsAvgSpeedEntries(data); //get new data each timeFrameType
            }

            if (position == SECOND_PAGE) {
                lineData = populateTimeVsDistanceEntries(data); //get new data each timeFrameType
            }
//        }
        lineData.setValueTextColor(Color.WHITE);

        chart.setData(lineData);

        chart.invalidate();
    }

    private LineData populateTimeVsAvgSpeedEntries(ArrayList<Run> data) {

        if(avgSpeedEntries != null && !entries.isEmpty()) {
            return avgSpeedEntries;
        }

        int size = data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int) data.get(i).averageSpeed;

            Entry point = new Entry(x, y);

            entries.add(point);
        }

        avgSpeedEntries = setupLineData(entries, "Time VS Speed");

        return avgSpeedEntries;
    }

    private LineData populateTimeVsDistanceEntries(ArrayList<Run> data) {

        if(avgDistanceEntries != null & !entries.isEmpty()) {
            return avgDistanceEntries;
        }

        int size = data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int) data.get(i).distanceTravelled;

            Entry point = new Entry(x, y);

            entries.add(point);
        }

        avgDistanceEntries = setupLineData(entries, "Time VS Distance");

        return avgDistanceEntries;
    }

    private LineData setupLineData(List<Entry> entries, String label) {

        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, label); // add entries to dataset
        Legend chartLegend = chart.getLegend();
        chart.getDescription().setText(des);


        styleDataSet(dataSet);
        styledChart(chartLegend);

        lineData = new LineData(dataSet);

        return lineData;
    }

    private void styledChart(Legend chartLegend) {
        chartLegend.setFormSize(11f); // set the size of the legend forms/shapes
        chartLegend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used

        chartLegend.setTextSize(13f);
        chartLegend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        chartLegend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
    }

    private void styleDataSet(LineDataSet dataSet) {

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(13f);
        dataSet.setValueTextColor(text);
        dataSet.setLineWidth(7f);
        dataSet.setCircleRadius(8f);
        dataSet.setValueTextSize(7f);

        dataSet.setColors(lineColor);
    }

    private void clearGraph() {

        if (lineData != null) {
            lineData.clearValues();
        }
        if (entries != null){
            entries.clear();
        }
        if (chart != null) {
            chart.invalidate();
            chart.clear();
        }
    }

    private String getToken() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        return preferences.getString("token", "");
    }

    private ArrayList<Run> convertJsonToRuns(JsonArray jsonArray) {
        ArrayList<Run> runs = new ArrayList<>();

        RunDeserializer deserializer = new RunDeserializer();

        for (JsonElement element : jsonArray) {

            Run run = deserializer.deserialize(element, Run.class, null);
            runs.add(run);
        }

        return runs;
    }

}