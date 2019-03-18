package com.cordite.cordite.Api;

import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.Entities.TimeFrame;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RunService {
    @POST("runs")
    Call<JsonObject> saveRun(@Header("Authorization") String token, @Body Run run);

    @POST("runs/myRuns")
    Call<JsonArray> getUserRuns(@Header("Authorization") String token);

    @POST("runs/graphRuns")
    Call<JsonArray> graphRuns(@Header("Authorization") String token , @Body TimeFrame timeFrame);
}
