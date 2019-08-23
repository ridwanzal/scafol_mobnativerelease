package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bidang implements Serializable {
    @SerializedName("bi_id")
    private String biId;
    @SerializedName("dinas_id")
    private String dinasId;
    @SerializedName("bi_nama")
    private String biNama;
    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("date_updated")
    private String dateUpdated;

    @SerializedName("is_deleted")
    private String isDeleted;

    public Bidang(String biId, String dinasId, String biNama, String dateCreated, String dateUpdated, String isDeleted) {
        this.biId = biId;
        this.dinasId = dinasId;
        this.biNama = biNama;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.isDeleted = isDeleted;
    }

    public String getBiId() {
        return biId;
    }

    public void setBiId(String biId) {
        this.biId = biId;
    }

    public String getDinasId() {
        return dinasId;
    }

    public void setDinasId(String dinasId) {
        this.dinasId = dinasId;
    }

    public String getBiNama() {
        return biNama;
    }

    public void setBiNama(String biNama) {
        this.biNama = biNama;
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

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Bidang{" +
                "biId='" + biId + '\'' +
                ", dinasId='" + dinasId + '\'' +
                ", biNama='" + biNama + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                '}';
    }
}
