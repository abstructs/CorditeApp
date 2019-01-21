package com.cordite.cordite.Api;

import com.cordite.cordite.Entities.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("users/signUp")
    Call<JsonObject> signUp(@Body User user);

    @POST("users/login")
    Call<JsonObject> login(@Body User user);

    @POST("users/emailTaken")
    Call<JsonObject> emailTaken(@Body User user);
}