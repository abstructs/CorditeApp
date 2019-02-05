package com.cordite.cordite.Run;

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

import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RunAdapter extends RecyclerView.Adapter<RunAdapter.MyViewHolder> {
    private ArrayList<Run> runs;


    public RunAdapter(ArrayList<Run> runs) {
        this.runs = runs;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView dateTxt;
        public TextView avgSpeedTxt;
        public TextView distanceTxt;
        public Button viewBtn;
        public RatingBar ratingBar;
        public Context context;
        public ImageView imageView;

        public MyViewHolder(final View v) {
            super(v);

            this.dateTxt = v.findViewById(R.id.dateTxt);
            this.avgSpeedTxt = v.findViewById(R.id.speedTxt);
            this.distanceTxt = v.findViewById(R.id.distanceTxt);
            this.viewBtn = v.findViewById(R.id.viewBtn);
            this.ratingBar = v.findViewById(R.id.ratingBar);
            this.imageView = v.findViewById(R.id.imageView);
            this.context = v.getContext();

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final Run run = runs.get(position);

        holder.dateTxt.setText(run.date);
        holder.ratingBar.setRating(run.rating);
        holder.distanceTxt.setText(String.valueOf(run.distanceTravelled) + "KM in " + run.timeElapsed + "ms");
        holder.avgSpeedTxt.setText(String.valueOf(run.averageSpeed) + "KM/h Average Speed");

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                holder.context.startActivity(setJournalItemIntent(run, holder.context));
            }
        });
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }

    private Intent setJournalItemIntent(Run run, Context context) {
        Intent intent = new Intent(context, RunViewActivity.class);

        String runViewDateTxt = String.valueOf(run.date);
        String runViewDistanceTxt = String.valueOf(run.distanceTravelled);
        String runViewAvgSpeedTxt = String.valueOf(run.averageSpeed);
        String runViewTimeTxt = String.valueOf(run.timeElapsed);

        intent.putExtra("runViewDateTxt", runViewDateTxt);
        intent.putExtra("runViewDistanceTxt", runViewDistanceTxt);
        intent.putExtra("runViewAvgSpeedTxt", runViewAvgSpeedTxt);
        intent.putExtra("runViewTimeTxt", runViewTimeTxt);

        return intent;
    }
}