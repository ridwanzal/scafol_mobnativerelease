package com.example.scafolmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseKegiatan {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Kegiatan> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Kegiatan> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
