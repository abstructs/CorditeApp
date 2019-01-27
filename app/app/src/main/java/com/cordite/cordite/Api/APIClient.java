package com.cordite.cordite.Api;

import com.cordite.cordite.Deserializers.RunDeserializer;
import com.cordite.cordite.Entities.Run;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static String baseUrl = "http://192.168.1.174:3000";

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

//    private static GsonConverterFactory gsonConverterFactory() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        GsonBuilder myGson = gsonBuilder.registerTypeAdapter(Run.class, new RunDeserializer());
//
//        return GsonConverterFactory.create(myGson.create());
//
//    }
}
