package com.cordite.cordite.Entities;
import com.google.gson.annotations.SerializedName;


public class TimeFrame{
    @SerializedName("timeFrame")
    public String timeFrame;

    public TimeFrame(String timeFrame){
        this.timeFrame = timeFrame;
    }
}