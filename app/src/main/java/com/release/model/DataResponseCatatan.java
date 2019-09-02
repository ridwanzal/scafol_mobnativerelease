package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseCatatan {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Catatan> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Catatan> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
