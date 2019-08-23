package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseBidang {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Bidang> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Bidang> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
