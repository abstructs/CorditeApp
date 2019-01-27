package com.cordite.cordite.Entities;

import android.location.Location;

import com.cordite.cordite.Report.ReportType;
import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("location")
    public Location location;

    @SerializedName("type")
    public ReportType type;
}
