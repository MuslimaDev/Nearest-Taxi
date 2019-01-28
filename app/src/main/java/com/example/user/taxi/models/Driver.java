
package com.example.user.taxi.models;

import com.google.gson.annotations.SerializedName;

public class Driver {

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
