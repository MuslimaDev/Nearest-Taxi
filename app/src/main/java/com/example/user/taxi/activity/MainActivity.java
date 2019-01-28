package com.example.user.taxi.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap mapbox;
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
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.mapbox = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(42.8700000, 74.5900000))
                .title("Bishkek")
        );
        getDriversLocation();
    }

    private void getDriversLocation() {
        retrofitService.getDriverLocation(longitude, latitude)
                .enqueue(new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (Company company :
                                    response.body().getCompanies()) {
                              //  tel = company.getContacts().get(1).getContact();
                                for (Driver driver :
                                        company.getDrivers()) {
                                    Icon icon = IconFactory.getInstance(MainActivity.this).fromResource(R.drawable.taxi_icon);
                                    LatLng cars = new LatLng(driver.getLat(), driver.getLon());
                                    mapbox.addMarker(new MarkerOptions()
                                            .position(new LatLng(42.874886, 74.597305))
                                            .title("taxi")
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