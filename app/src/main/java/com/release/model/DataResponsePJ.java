package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponsePJ {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Kontraktor> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Kontraktor> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
