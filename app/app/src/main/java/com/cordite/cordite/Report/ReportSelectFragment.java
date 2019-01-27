package com.cordite.cordite.Report;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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

        setupButtons(view);

        return view;
    }

    private void setupButtons(View view) {
        FrameLayout layout = view.findViewById(R.id.reportSelectLayout);

        final ImageButton trailClosedBtn = view.findViewById(R.id.trailClosedBtn);
        ImageButton photoBtn = view.findViewById(R.id.photoBtn);

        ImageButton constructionBtn = view.findViewById(R.id.constructionBtn);
        ImageButton coolPlaceBtn = view.findViewById(R.id.coolPlaceBtn);

        ImageButton beCarefulBtn = view.findViewById(R.id.beCarefulBtn);
        ImageButton waterBtn = view.findViewById(R.id.waterBtn);

        trailClosedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("trail clicked");
                Activity activity = getActivity();

                if(activity instanceof MapsActivity) {
                    MapsActivity mapsActivity = (MapsActivity) activity;

                    mapsActivity.addReport(ReportType.trailClosed);
                }
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("photo");
            }
        });

        constructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("construction");
            }
        });

        coolPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("cool place");
            }
        });

        beCarefulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("be careful");
            }
        });

        waterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("water");
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
