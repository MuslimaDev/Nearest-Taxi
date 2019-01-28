package com.example.user.taxi;

import android.app.Application;
import android.content.Context;

import com.example.user.taxi.network.NetworkBuilder;
import com.example.user.taxi.network.RetrofitService;

public class Taxi extends Application {
    private RetrofitService retrofitService;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofitService = NetworkBuilder.service();
    }

    public static Taxi get(Context context) {
        return (Taxi) context.getApplicationContext();
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }


}
