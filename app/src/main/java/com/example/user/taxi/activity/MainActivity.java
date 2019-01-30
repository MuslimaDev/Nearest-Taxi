package com.example.user.taxi.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.user.taxi.R;
import com.example.user.taxi.Taxi;
import com.example.user.taxi.models.Company;
import com.example.user.taxi.models.Driver;
import com.example.user.taxi.models.Example;
import com.example.user.taxi.network.RetrofitService;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private MapboxMap map;
    private Marker marker;
    private RetrofitService retrofitService;
    private double longitude, latitude;
    private String tel;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        initMap(savedInstanceState);
        retrofitService = Taxi.get(getApplicationContext()).getRetrofitService();

        getDriversLocation();
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.map = mapboxMap;
        mapboxMap.setStyle(Style.DARK);

    }

    private void getDriversLocation() {
        retrofitService.getDriverLocation(42.8792502, 74.6178904)
                .enqueue(new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (Company company : response.body().getCompanies()) {
                                for (Driver driver : company.getDrivers()) {
                                    Icon icon = IconFactory.getInstance(MainActivity.this).fromResource(R.drawable.taxi_icon);
                                    LatLng cars = new LatLng(driver.getLat(), driver.getLon());

                                    Log.d("cars", String.valueOf(cars.getLatitude() + cars.getLongitude()));

                                    map.addMarker(new MarkerOptions()
                                            .position(cars)
                                            .title("Taxi")
                                            .icon(icon));
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}