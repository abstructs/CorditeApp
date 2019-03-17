package com.cordite.cordite.Run;

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
import com.cordite.cordite.R;
import com.github.mikephil.charting.charts.LineChart;
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
    private  LineChart chart;
    private LineData lineData;
    private List<Entry> entries = new ArrayList<>();
    private static final int NUM_PAGES = 5;

    private ViewPager mPager;

    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_graph_view);

        pagerAdapter = new RunGraphViewActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Here's your instance
                Fragment fragment =(RunGraphViewFragment)((ScreenSlidePagerAdapter) pagerAdapter).getRegisteredFragment(position);
                setup(((RunGraphViewFragment) fragment).getChart());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    ///graph logic
    private void setup(LineChart chart){

        this.chart = chart;

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

    //runs gathered from DB are based on timeframe which is hardcoded on button click,
    // if String is tampered do nothing

    private void graphRuns(String timeframe) {
        clearGraph(); // clear previous graph this may need to be on each screen load of the view pager

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
        //if screen one then:populateTimeVsAvgSpeedEntries
        //else populateTimeVsDistanceEntries

        lineData = populateTimeVsAvgSpeedEntries(data); //get new data each time

        lineData.setValueTextColor(Color.WHITE);


        this.chart.setData(lineData);
        this.chart.notifyDataSetChanged();
        this.chart.invalidate();

    }

    private LineData populateTimeVsAvgSpeedEntries(ArrayList<Run> data) {

        int size =data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int)data.get(i).averageSpeed;

            Entry point = new Entry(x,y);

            entries.add(point);
        }

        return sortLineData(entries);
    }

    private LineData populateTimeVsDistanceEntries(ArrayList<Run> data) {

        int size =data.size();

        for (int i = 0; i < size; i++) {

            int x = data.get(i).timeElapsed / 1000;
            int y = (int)data.get(i).distanceTravelled;

            Entry point = new Entry(x,y);

            entries.add(point);
        }
        return sortLineData(entries);
    }

    public LineData sortLineData(List<Entry> entries ){

        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, "Time vs Speed"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setLineWidth(4f);

        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(0);

        lineData = new LineData(dataSet);
        return  lineData;
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
