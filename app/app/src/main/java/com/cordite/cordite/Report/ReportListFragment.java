package com.cordite.cordite.Report;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.Map.MapsActivity;
import com.cordite.cordite.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public class ReportListFragment extends Fragment {

    private List<Report> reports;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        setupFragment(view);
        return view;
    }

    private void setupFragment(View view) {
        final Activity activity = getActivity();

        if(!(activity instanceof MapsActivity)) {
            return;
        }

        final MapsActivity mapsActivity = (MapsActivity) activity;

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        FrameLayout layout = view.findViewById(R.id.reportListLayout);

        ReportListAdapter adapter = new ReportListAdapter(reports);

        LayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.onBackPressed();
            }
        });

    }

    public static ReportListFragment newInstance(List<Report> reports) {
        ReportListFragment fragment = new ReportListFragment();

        fragment.reports = reports;
        return fragment;
    }
}
