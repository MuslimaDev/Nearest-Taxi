package com.example.user.taxi.network;

import com.example.user.taxi.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("/nearest/{lat}/{lon}")
    Call<Example> getDriverLocation(@Path("lat") double lat,
                                    @Path("lon") double lon);
}
