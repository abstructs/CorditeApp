package com.cordite.cordite.Report;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyViewHolder> {
    private List<Report> reports;


    public ReportListAdapter(List<Report> reports) {
        this.reports = reports;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView reportTypeTxt;
        public TextView addressTxt;
        public ImageView reportTypeImage;

        public MyViewHolder(final View v) {
            super(v);

            this.reportTypeTxt = v.findViewById(R.id.reportTypeTxt);
            this.addressTxt = v.findViewById(R.id.addressTxt);
            this.reportTypeImage = v.findViewById(R.id.reportTypeImage);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final Report report = reports.get(position);

        holder.addressTxt.setText(report.address);
        holder.reportTypeTxt.setText(report.type.toString());

        switch(report.type) {
            case beCareful:
                holder.reportTypeImage.setImageResource(R.drawable.ic_warning);
                break;
            case coolPlace:
                holder.reportTypeImage.setImageResource(R.drawable.ic_check);
                break;
            case photo:
                holder.reportTypeImage.setImageResource(R.drawable.ic_photo);
                break;
            case trailClosed:
                holder.reportTypeImage.setImageResource(R.drawable.ic_closed);
                break;
            case construction:
                holder.reportTypeImage.setImageResource(R.drawable.ic_construction);
                break;
            case waterFountain:
                holder.reportTypeImage.setImageResource(R.drawable.ic_water);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }
}