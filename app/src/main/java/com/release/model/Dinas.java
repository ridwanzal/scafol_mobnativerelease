package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Dinas implements Serializable {
    @SerializedName("dinas_id")
    private String dinasId;

    @SerializedName("dinas_nama")
    private String dinasNama;

    @SerializedName("dinas_pptk")
    private String dinasPptk;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("zoom_level")
    private String zoomLevel;

    @Override
    public String toString() {
        return "Dinas{" +
                "dinasId='" + dinasId + '\'' +
                ", dinasNama='" + dinasNama + '\'' +
                ", dinasPptk='" + dinasPptk + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", zoomLevel='" + zoomLevel + '\'' +
                ", webLink='" + webLink + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                ", dateDeleted='" + dateDeleted + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @SerializedName("web_link")
    private String webLink;

    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("date_updated")
    private String dateUpdated;

    @SerializedName("date_deleted")
    private String dateDeleted;

    @SerializedName("status")
    private String status;

    public String getDinasId() {
        return dinasId;
    }

    public void setDinasId(String dinasId) {
        this.dinasId = dinasId;
    }

    public String getDinasNama() {
        return dinasNama;
    }

    public void setDinasNama(String dinasNama) {
        this.dinasNama = dinasNama;
    }

    public String getDinasPptk() {
        return dinasPptk;
    }

    public void setDinasPptk(String dinasPptk) {
        this.dinasPptk = dinasPptk;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(String zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
