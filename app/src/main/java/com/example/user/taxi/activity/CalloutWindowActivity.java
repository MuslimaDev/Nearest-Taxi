package com.example.user.taxi.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.taxi.R;
import com.example.user.taxi.models.Company;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class CalloutWindowActivity implements MapboxMap.InfoWindowAdapter {
    private Context context;
    private ImageView image;
    private TextView title, sms, phone;
    private Button button;

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.callout_window, null);
        image = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        sms = view.findViewById(R.id.sms);

        Company company = new Company();

        if (company != null) {
            title.setText("Taxi:" + company.getName());
            sms.setText("SMS:" + company.getContacts().get(0).getContact());
            phone.setText("Tel:" + company.getContacts().get(1).getContact());
        }
        return view;
    }
}
