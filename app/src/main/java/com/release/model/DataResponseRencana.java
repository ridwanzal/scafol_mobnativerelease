package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseRencana {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Rencana> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Rencana> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
