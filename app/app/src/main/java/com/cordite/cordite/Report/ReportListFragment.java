package com.cordite.cordite.Report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cordite.cordite.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_select, container, false);
    }

}
