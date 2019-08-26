package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DataResponseDinas{
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Dinas> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Dinas> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
