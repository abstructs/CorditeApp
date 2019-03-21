package com.cordite.cordite.Report;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cordite.cordite.Map.MapsActivity;
import com.cordite.cordite.R;

public class ReportSelectFragment extends Fragment {
    public ReportSelectFragment() {
    }

    public static ReportSelectFragment newInstance() {
        return new ReportSelectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_select, container, false);

        setupMapsActivityButtons(view);

        return view;
    }

    private void setupMapsActivityButtons(View view) {
        final Activity activity = getActivity();

        if(!(activity instanceof MapsActivity)) {
            return;
        }

        final MapsActivity mapsActivity = (MapsActivity) activity;

        FrameLayout layout = view.findViewById(R.id.reportSelectLayout);

        ImageButton trailClosedBtn = view.findViewById(R.id.trailClosedBtn);
//        ImageButton photoBtn = view.findViewById(R.id.photoBtn);

        ImageButton constructionBtn = view.findViewById(R.id.constructionBtn);
        ImageButton coolPlaceBtn = view.findViewById(R.id.coolPlaceBtn);

        ImageButton beCarefulBtn = view.findViewById(R.id.beCarefulBtn);
        ImageButton waterBtn = view.findViewById(R.id.waterBtn);

        trailClosedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.saveReport(ReportType.trailClosed);
                activity.onBackPressed();
            }
        });

//        photoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mapsActivity.saveReport(ReportType.photo);
//                activity.onBackPressed();
//            }
//        });

        constructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.saveReport(ReportType.construction);
                activity.onBackPressed();
            }
        });

        coolPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.saveReport(ReportType.coolPlace);
                activity.onBackPressed();
            }
        });

        beCarefulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.saveReport(ReportType.beCareful);
                activity.onBackPressed();
            }
        });

        waterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsActivity.saveReport(ReportType.waterFountain);
                activity.onBackPressed();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
}
