package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseKegiatanAnggaran {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<KegiatanTreeAnggaran> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<KegiatanTreeAnggaran> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
