package com.cordite.cordite.Entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public Run() {

    }

    public String getTime() {

        long milliseconds = timeElapsed / 1000;

        long s = milliseconds % 60;
        long m = (milliseconds / 60) % 60;
        long h = (milliseconds / (60 * 60)) % 24;

        if(h > 0) {
            return String.format(Locale.getDefault(),"%d hour, %d minutes", h, m);
        } else if(m > 0) {
            return String.format(Locale.getDefault(), "%d minutes, %d seconds", m, s);
        } else {
            return String.format(Locale.getDefault(), "%d seconds", s);
        }
    }

    protected Run(Parcel in) {
        this.locations = new ArrayList<>();

        in.readTypedList(this.locations, Location.CREATOR);
        averageSpeed = in.readDouble();
        timeElapsed = in.readInt();
        distanceTravelled = in.readDouble();
        date = in.readString();
        rating = in.readInt();
    }

    public static final Creator<Run> CREATOR = new Creator<Run>() {
        @Override
        public Run createFromParcel(Parcel in) {
            return new Run(in);
        }

        @Override
        public Run[] newArray(int size) {
            return new Run[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(locations);
        dest.writeDouble(averageSpeed);
        dest.writeInt(timeElapsed);
        dest.writeDouble(distanceTravelled);
        dest.writeString(date);
        dest.writeInt(rating);
    }
}
