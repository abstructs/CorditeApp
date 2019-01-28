package com.cordite.cordite.Api;

import android.location.Location;

import com.cordite.cordite.Entities.Report;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReportService {
    @POST("reports")
    Call<JsonObject> saveReport(@Header("Authorization") String token, @Body Report report);

    @POST("reports/aroundMe")
    Call<JsonArray> getReports(@Header("Authorization") String token, @Body Location location);
}
