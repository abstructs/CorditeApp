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


        try {
            addressTxt.setText(getAddress(report.location.getLatitude(), report.location.getLongitude()));
        } catch(IOException e) {
            // todo: inform user with toast
            e.printStackTrace();
        }

        distanceTxt.setText(String.valueOf(report.distanceTo));
        distanceTxt.append(getString(R.string.KM));
        typeTxt.setText(fromCamelCase(report.type.toString()));
        timeStamp.setText(String.valueOf(report.timestamp));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private String fromCamelCase(String str) {
        if(str.length() == 0) return "";

        StringBuilder builder = new StringBuilder();

        builder.append(Character.toUpperCase(str.charAt(0)));

        for(int i = 1; i < str.length(); i++) {
            char strChar = str.charAt(i);

            if(Character.isUpperCase(strChar)) {
                builder.append(" ");
                builder.append(strChar);
            } else {
                builder.append(strChar);
            }
        }
        return builder.toString();
    }

    private String getAddress(double latitude, double longitude) throws IOException{

        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        return addresses.get(0).getAddressLine(0);
    }

}
