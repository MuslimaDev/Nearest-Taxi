package com.example.user.taxi.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.user.taxi.R;
import com.google.android.gms.location.LocationListener;

public class CurrentLocationActivity extends AppCompatActivity {
    private LocationManager locationManager;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            showLocation(location);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null)
            locationManager.removeUpdates((android.location.LocationListener) locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            showLocation(location);

        }

        @SuppressLint("MissingPermission")
        private void onProviderEnabled(String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            showLocation(location);
        }
    }

    @SuppressLint("MissingPermission")
    public void showLocation(Location location) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, (android.location.LocationListener) locationListener);

        if (location != null) {
            Intent intent = new Intent(CurrentLocationActivity.this, MainActivity.class);
            intent.putExtra("locationOne", location.getLatitude());
            intent.putExtra("locationTwo", location.getLongitude());
            startActivity(intent);
            finish();
        }
    }
}
