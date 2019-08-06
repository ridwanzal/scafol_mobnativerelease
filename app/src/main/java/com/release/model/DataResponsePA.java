package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponsePA {


    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<PaketDashboard> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<PaketDashboard> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
