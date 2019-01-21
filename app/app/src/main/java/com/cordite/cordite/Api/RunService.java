package com.cordite.cordite.Api;

import com.cordite.cordite.Entities.Run;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RunService {
    @POST("runs")
    Call<JsonObject> saveRun(@Header("Authorization") String token, @Body Run run);
}
