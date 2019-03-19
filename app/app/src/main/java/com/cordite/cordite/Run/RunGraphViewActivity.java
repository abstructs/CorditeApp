package com.cordite.cordite.Run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.github.mikephil.charting.charts.Chart;
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
    private String time;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_graph_view);

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

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                clearGraph();
            }

            public void onPageSelected(int position) {
            }
        });

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

    private void createButtons() {
        Button allViewBtn = (Button) findViewById(R.id.allViewBtn);
        Button weekViewBtn = (Button) findViewById(R.id.weekViewBtn);
        Button monthViewBtn = (Button) findViewById(R.id.monthViewBtn);

        allViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                time = "all";

                RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                        .getRegisteredFragment(mPager.getCurrentItem());

                LineChart data = fragment.getChart();

                setupChart(data);

                graphRuns(time);
            }
        });
        weekViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                time = "week";

                RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                        .getRegisteredFragment(mPager.getCurrentItem());

                LineChart data = fragment.getChart();

                setupChart(data);

                graphRuns(time);
            }
        });
        monthViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                time = "month";

                RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                        .getRegisteredFragment(mPager.getCurrentItem());

                LineChart data = fragment.getChart();

                setupChart(data);

                graphRuns(time);
            }
        });
    }

    private void setupChart(LineChart chart) {

        this.chart = chart; //set chart

        this.runService = APIClient.getClient().create(RunService.class); // set client

    }

    private void graphRuns(String time) {
        clearGraph();

        TimeFrame timeFrame = new TimeFrame(time);

        Call<JsonArray> request = runService.graphRuns(getToken(), timeFrame);

        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if (response.code() == 401) {
                    Toast.makeText(RunGraphViewActivity.this, "Error Retrieving runs", Toast.LENGTH_SHORT).show();
                } else {

                    assert response.body() != null;
                    ArrayList<Run> runs = convertJsonToRuns(response.body().getAsJsonArray());
                    setChartData(runs);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(RunGraphViewActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setChartData(ArrayList<Run> data) {

        if (mPager.getCurrentItem() == 1) {
            lineData = populateTimeVsDistanceEntries(data); //get new data each time
        }
        if (mPager.getCurrentItem() == 0) {
            lineData = populateTimeVsAvgSpeedEntries(data); //get new data each time
        }

        lineData.setValueTextColor(Color.WHITE);

        chart.setData(lineData);

        chart.invalidate();
    }

    private LineData populateTimeVsAvgSpeedEntries(ArrayList<Run> data) {

        int size = data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int) data.get(i).averageSpeed;

            Entry point = new Entry(x, y);

            entries.add(point);
        }

        return sortLineData(entries, "Time VS Speed");
    }

    private LineData populateTimeVsDistanceEntries(ArrayList<Run> data) {

        int size = data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int) data.get(i).distanceTravelled;

            Entry point = new Entry(x, y);

            entries.add(point);
        }

        return sortLineData(entries, "Time VS Distance");
    }

    private LineData sortLineData(List<Entry> entries, String label) {

        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, label); // add entries to dataset

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setLineWidth(4f);
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(0);
        dataSet.setLineWidth(12f);

        Legend chartLegend = chart.getLegend();

        chartLegend.setTextSize(16f);
        chartLegend.setTextColor(Color.WHITE);

        lineData = new LineData(dataSet);
        return lineData;
    }

    private void clearGraph() {

        if (lineData != null) {
            lineData.clearValues();
        }
        if ( entries != null){
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
