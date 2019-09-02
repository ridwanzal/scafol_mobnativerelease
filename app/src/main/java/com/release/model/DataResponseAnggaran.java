package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseAnggaran {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Anggaran> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Anggaran> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
