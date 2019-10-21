package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponseToken {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Token> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Token> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
