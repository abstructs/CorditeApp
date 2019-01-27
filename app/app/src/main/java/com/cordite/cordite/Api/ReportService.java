package com.cordite.cordite.Api;

import com.cordite.cordite.Entities.Report;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReportService {
    @POST("reports")
    Call<JsonObject> saveReport(@Header("Authorization") String token, @Body Report report);
}
