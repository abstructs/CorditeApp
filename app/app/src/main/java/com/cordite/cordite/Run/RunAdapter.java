package com.cordite.cordite.Run;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.R;

import java.util.ArrayList;

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

        public MyViewHolder(View v) {
            super(v);

            this.dateTxt = v.findViewById(R.id.dateTxt);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dateTxt.setText(runs.get(position).date);
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}