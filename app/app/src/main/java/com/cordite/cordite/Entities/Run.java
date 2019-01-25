package com.cordite.cordite.Entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Run implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(locations);
        dest.writeDouble(averageSpeed);
        dest.writeInt(timeElapsed);
        dest.writeDouble(distanceTravelled);
        dest.writeString(date);
        dest.writeInt(rating);
    }
}
