package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Catatan implements Serializable {
    @SerializedName("pa_id")
    private String paId;
    @SerializedName("ca_id")
    private String caId;
    @SerializedName("ca_catatan")
    private String caCatatan;
    @SerializedName("date_created")
    private String dateCreated;

    public String getCaCatatan() {
        return caCatatan;
    }

    public void setCaCatatan(String caCatatan) {
        this.caCatatan = caCatatan;
    }

    @SerializedName("date_updated")
    private String dateUpdated;

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
