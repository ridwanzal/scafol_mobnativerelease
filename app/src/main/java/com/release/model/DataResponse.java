package com.release.model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<User> data;
    @SerializedName("message")
    private String message;
    public boolean getStatus() {
        return status;
    }

    public ArrayList<User> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
