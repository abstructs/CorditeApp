package com.cordite.cordite.Entities;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Run {
    @SerializedName("locations")
    public List<Location> locations;

    @SerializedName("averageSpeed")
    public double averageSpeed;

    // in milliseconds
    @SerializedName("timeElapsed")
    public int timeElapsed;

    @SerializedName("distanceTravelled")
    public double distanceTravelled;

    @SerializedName("date")
    public String date;

    @SerializedName("rating")
    public int rating;
}
