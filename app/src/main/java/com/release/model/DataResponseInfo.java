package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseInfo {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Info> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Info> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
