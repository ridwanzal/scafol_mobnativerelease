package com.release.model;

import com.google.gson.annotations.SerializedName;

public class Rencana {
    @SerializedName("re_id")
    private String reId;

    @SerializedName("pa_id")
    private String paId;

    @SerializedName("re_tanggal")
    private String reTanggal;

    @SerializedName("re_progress")
    private String reProgress;

    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("date_updated")
    private String dateUpdated;

    public Rencana(String reId, String paId, String reTanggal, String reProgress, String dateCreated, String dateUpdated) {
        this.reId = reId;
        this.paId = paId;
        this.reTanggal = reTanggal;
        this.reProgress = reProgress;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public String getReId() {
        return reId;
    }

    public void setReId(String reId) {
        this.reId = reId;
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getReTanggal() {
        return reTanggal;
    }

    public void setReTanggal(String reTanggal) {
        this.reTanggal = reTanggal;
    }

    public String getReProgress() {
        return reProgress;
    }

    public void setReProgress(String reProgress) {
        this.reProgress = reProgress;
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
