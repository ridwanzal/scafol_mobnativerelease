package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseSerapan {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Serapan> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Serapan> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
