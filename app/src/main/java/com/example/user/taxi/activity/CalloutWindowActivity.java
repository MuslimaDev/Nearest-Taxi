package com.example.user.taxi.activity;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.user.taxi.R;
import com.example.user.taxi.models.Company;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class CalloutWindowActivity implements MapboxMap.InfoWindowAdapter {
    private Activity context;

    CalloutWindowActivity(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.callout_window, null);

        TextView title = view.findViewById(R.id.title);
        TextView sms = view.findViewById(R.id.sms);
        TextView phone = view.findViewById(R.id.phone);

        Company model = new Company();

        title.setText(model.getName());
        sms.setText(model.getContacts().get(0).getContact());
        phone.setText(model.getContacts().get(1).getContact());

        return view;
    }
}