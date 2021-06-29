package com.example.cuacasekarang.api;

import com.example.cuacasekarang.model.ModelCuaca;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpoint {
    @GET("data/2.5/weather")
    Call<ModelCuaca> getData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String apikey,
            @Query("lang") String lang
    );
}
