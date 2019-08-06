package com.example.scafolmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponsePaket {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Paket> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Paket> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
