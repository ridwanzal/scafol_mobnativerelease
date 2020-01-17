package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponsePAS {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<PaketDashboardSuper> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<PaketDashboardSuper> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
