package com.cordite.cordite.Run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunGraphViewActivity extends AppCompatActivity {

    private RunService runService;

    private LineChart chart;
    private LineData lineData;
    private List<Entry> entries = new ArrayList<>();

    private static final int NUM_PAGES = 2;
    private TimeFrameType timeFrameType;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    private LineData avgSpeedEntries;
    private LineData avgDistanceEntries;

    private int text;
    private int lineColor;

    private enum TimeFrameType {
        ALL, MONTH, WEEK
    }

    private final int FIRST_PAGE = 0;
    private final int SECOND_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_graph_view);

        createColors();
        createPager(); // set pager
        createButtons();

    }

    @Override
    protected void onPause() {
        clearGraph();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void createPager(){
        pagerAdapter = new RunGraphViewActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(pagerAdapter);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new RunGraphViewFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);

            return fragment;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    private void createColors(){

        text =  ContextCompat.getColor(this, R.color.primaryText);
        lineColor = ContextCompat.getColor(this, R.color.pathColour);
    }

    private void createButtons() {
        Button allViewBtn = findViewById(R.id.allViewBtn);
        Button weekViewBtn = findViewById(R.id.weekViewBtn);
        Button monthViewBtn = findViewById(R.id.monthViewBtn);

        allViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                timeFrameType = TimeFrameType.ALL;

                RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                        .getRegisteredFragment(mPager.getCurrentItem());

                LineChart data = fragment.getChart();

                setupChart(data);

                graphRuns(timeFrameType);
            }
        });
        weekViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                timeFrameType = TimeFrameType.WEEK;

                RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                        .getRegisteredFragment(mPager.getCurrentItem());

                LineChart data = fragment.getChart();

                setupChart(data);

                graphRuns(timeFrameType);
            }
        });
        monthViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                timeFrameType = TimeFrameType.MONTH;

                RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                        .getRegisteredFragment(mPager.getCurrentItem());

                LineChart data = fragment.getChart();

                setupChart(data);

                graphRuns(timeFrameType);
            }
        });
    }

    private void setupChart(LineChart chart) {

        this.chart = chart; //set chart

        this.runService = APIClient.getClient().create(RunService.class); // set client

    }

    private void graphRuns(TimeFrameType timeFrameType) {
        clearGraph();

//        System.out.println(timeFrameType.toString());
        Call<JsonArray> request = runService.graphRuns(getToken(), new TimeFrame(timeFrameType.toString()));

        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (response.code() == 401) {
                    Toast.makeText(RunGraphViewActivity.this, "Error Retrieving runs", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Run> runs = convertJsonToRuns(response.body().getAsJsonArray());
                setChartData(runs);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(RunGraphViewActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setChartData(ArrayList<Run> data) {

        if (mPager.getCurrentItem() == FIRST_PAGE) {
            lineData = populateTimeVsAvgSpeedEntries(data); //get new data each timeFrameType
        }

        if (mPager.getCurrentItem() == SECOND_PAGE) {
            lineData = populateTimeVsDistanceEntries(data); //get new data each timeFrameType
        }

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
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
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
