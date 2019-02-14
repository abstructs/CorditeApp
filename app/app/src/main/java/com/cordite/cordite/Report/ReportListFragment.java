package com.cordite.cordite.Report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public class ReportListFragment extends Fragment {

    private List<Report> reports;
//    private LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        ReportListAdapter adapter = new ReportListAdapter(reports);

        LayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    public static ReportListFragment newInstance(List<Report> reports) {
        ReportListFragment fragment = new ReportListFragment();

//        fragment.layoutManager = layoutManager;
        fragment.reports = reports;
        return fragment;
    }
}
