package com.cordite.cordite.Run;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cordite.cordite.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RunDataFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_run_data, container, false);
    }

    public static RunDataFragment newInstance() {
        return new RunDataFragment();
    }
}
