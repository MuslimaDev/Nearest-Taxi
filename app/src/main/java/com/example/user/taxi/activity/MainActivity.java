package com.example.user.taxi.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.taxi.R;
import com.example.user.taxi.Taxi;
import com.example.user.taxi.models.Company;
import com.example.user.taxi.models.Driver;
import com.example.user.taxi.models.Example;
import com.example.user.taxi.network.RetrofitService;
import com.example.user.taxi.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {
    private MapView mapView;
    private MapboxMap map;
    private Marker marker;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        ImageView myLocation = findViewById(R.id.myLocationButton);
        ImageView callButton = findViewById(R.id.callButton);
        initMap(savedInstanceState);
        retrofitService = Taxi.get(getApplicationContext()).getRetrofitService();
        myLocation.setOnClickListener(this);

        if (PermissionUtils.Companion.isLocationEnable(this)) {
            getCurrentLocation();
        }

        getDriversLocation();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        TextView title = new TextView(this);
        title.setText("SELECT TAXI SERVICE");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        TextView msg = new TextView(this);
        msg.setText("\nWith tapping a button you can make a call \n to taxi service that was chosen by you.");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NAMBA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String phone = "0559976000";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "SMS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String number = "0551061236";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                startActivity(intent);
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.BLUE);
        cancelBT.setLayoutParams(negBtnLP);
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.map = mapboxMap;
        mapboxMap.setStyle(Style.DARK);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(42.87922298, 74.61795241))
                .title("Current Location"));
    }

    private void getDriversLocation() {
        retrofitService.getDriverLocation(42.8792502, 74.6178904)
                .enqueue(new Callback<Example>() {
                    @SuppressLint("LogNotTimber")
                    @Override
                    public void onResponse(@NotNull Call<Example> call, @NotNull Response<Example> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (Company company : response.body().getCompanies()) {
                                for (Driver driver : company.getDrivers()) {
                                    Icon icon = IconFactory.getInstance(MainActivity.this).fromResource(R.drawable.taxi_icon);
                                    LatLng cars = new LatLng(driver.getLat(), driver.getLon());

                                    marker = map.addMarker(new MarkerOptions()
                                            .setTitle(company.getName())
                                            .position(cars)
                                            .setSnippet("TEL: " + company.getContacts().get(1).getContact() + "\n" + "SMS: " + company.getContacts().get(0).getContact())
                                            .icon(icon));

                                    map.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {

                                        @NotNull
                                        @Override
                                        public View getInfoWindow(@NonNull Marker marker) {
                                            View view = getLayoutInflater().inflate(R.layout.callout_window, null);
                                            TextView title = view.findViewById(R.id.title);
                                            TextView sms = view.findViewById(R.id.sms);
                                            title.setText(marker.getTitle());
                                            sms.setText(marker.getSnippet());
                                            return view;
                                        }
                                    });
                                    MapboxMap.InfoWindowAdapter infoWindowAdapter = marker -> {
                                        return null;
                                    };
                                    map.setInfoWindowAdapter(infoWindowAdapter);
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Example> call, @NotNull Throwable t) {
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

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void cameraUpdate(double lat, double lng) {
        if (map != null) {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(lat - 0.01, lng))
                    .bearing(0)
                    .zoom(13).tilt(15).build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        }
    }

    @Override
    public void onClick(View v) {
        cameraUpdate(42.87922298, 74.61795241);
    }
}