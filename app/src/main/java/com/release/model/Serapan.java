package com.release.model;

import com.google.gson.annotations.SerializedName;

public class Serapan {
    @SerializedName("se_id")
    private String seId;

    @SerializedName("an_id")
    private String anId;

    @SerializedName("se_daya_serap")
    private String seDayaSerap;

    @SerializedName("se_sisa")
    private String seSisa;

    @SerializedName("se_tanggal")
    private String seTanggal;

    @SerializedName("se_keterangan")
    private String seKeterangan;

    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("date_udpated")
    private String dateUpdated;

    @SerializedName("status")
    private String status;

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getSeDayaSerap() {
        return seDayaSerap;
    }

    public void setSeDayaSerap(String seDayaSerap) {
        this.seDayaSerap = seDayaSerap;
    }

    public String getSeSisa() {
        return seSisa;
    }

    public void setSeSisa(String seSisa) {
        this.seSisa = seSisa;
    }

    public String getSeTanggal() {
        return seTanggal;
    }

    public void setSeTanggal(String seTanggal) {
        this.seTanggal = seTanggal;
    }

    public String getSeKeterangan() {
        return seKeterangan;
    }

    public void setSeKeterangan(String seKeterangan) {
        this.seKeterangan = seKeterangan;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
