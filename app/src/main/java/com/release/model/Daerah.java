package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Daerah implements Serializable {
    @SerializedName("daerah_id")
    private String daerahId;
    @SerializedName("daerah_nama")
    private String daerahNama;
    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("date_updated")
    private String dateUpdated;
    @SerializedName("date_deleted")
    private String dateDeleted;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("status")
    private String status;
    @SerializedName("zoom_level")
    private String zoom_level;
    @SerializedName("web_link")
    private String web_link;

    public Daerah(String daerahId, String daerahNama, String dateCreated, String dateUpdated, String dateDeleted, String longitude, String latitude, String status, String zoom_level, String web_link) {
        this.daerahId = daerahId;
        this.daerahNama = daerahNama;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDeleted = dateDeleted;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.zoom_level = zoom_level;
        this.web_link = web_link;
    }

    @Override
    public String toString() {
        return "Daerah{" +
                "daerahId='" + daerahId + '\'' +
                ", daerahNama='" + daerahNama + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                ", dateDeleted='" + dateDeleted + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", status='" + status + '\'' +
                ", zoom_level='" + zoom_level + '\'' +
                ", web_link='" + web_link + '\'' +
                '}';
    }

    public String getDaerahId() {
        return daerahId;
    }

    public void setDaerahId(String daerahId) {
        this.daerahId = daerahId;
    }

    public String getDaerahNama() {
        return daerahNama;
    }

    public void setDaerahNama(String daerahNama) {
        this.daerahNama = daerahNama;
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

    public String getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(String dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getZoom_level() {
        return zoom_level;
    }

    public void setZoom_level(String zoom_level) {
        this.zoom_level = zoom_level;
    }

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }
}
