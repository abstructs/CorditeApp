package com.cordite.cordite.Report;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ReportShowFragment extends Fragment {
    private static final String ARG_PARAM = "report";

    private Report report;


    public ReportShowFragment() {
    }

    public static ReportShowFragment newInstance(Report report) {
        ReportShowFragment fragment = new ReportShowFragment();
        Bundle args = new Bundle();

        args.putParcelable(ARG_PARAM, report);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            report = (Report) getArguments().get(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_show, container, false);
        setupFields(view);
        // Inflate the layout for this fragment
        return view;
    }


    private void setupFields(View view) {
        FrameLayout layout = view.findViewById(R.id.reportViewLayout);

        TextView typeTxt = view.findViewById(R.id.typeTxt);
        TextView distanceTxt = view.findViewById(R.id.distanceToTxt);
        TextView addressTxt = view.findViewById(R.id.addressTxt);
        TextView timeStamp = view.findViewById(R.id.timestampTxt);

        addressTxt.setText(report.address);
        distanceTxt.setText(String.valueOf(report.distanceTo));
        distanceTxt.append(getString(R.string.KM));
        typeTxt.setText(report.getTypeString());
        timeStamp.setText(String.valueOf(report.timestamp));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


}
