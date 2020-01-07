package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseDaerah {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Daerah> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Daerah> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
