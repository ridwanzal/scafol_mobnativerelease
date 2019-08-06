package com.example.scafolmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NominatimReverseMap {
    @SerializedName("place_id")
    private Integer place_id;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lon")
    private Double lon;
    @SerializedName("display_name")
    private String display_name;

    public Integer getPlace_id() {
        return place_id;
    }

    public void setPlace_id(Integer place_id) {
        this.place_id = place_id;
    }

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

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    @Override
    public String toString() {
        return "NominatimReverseMap{" +
                "place_id=" + place_id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", display_name='" + display_name + '\'' +
                '}';
    }
}
