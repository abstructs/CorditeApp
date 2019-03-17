package com.cordite.cordite.Run;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cordite.cordite.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import androidx.fragment.app.Fragment;


public class RunGraphViewFragment extends Fragment {

    private LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_run_graph_view_screen_slider, container, false);

        chart = (LineChart) rootView.findViewById(R.id.chart);

        return rootView;
    }

    public LineChart getChart(){
      return chart;
    }
}