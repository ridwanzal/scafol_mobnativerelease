package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Kegiatan implements Serializable {
    @SerializedName("ke_id")
    private String keId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("dinas_id")
    private String dinasId;
    @SerializedName("ke_judul")
    private String keJudul;
    @SerializedName("ke_norekening")
    private String keNoRekening;
    @SerializedName("ke_tahun")
    private String keTahun;
    @SerializedName("status")
    private String status;
    @SerializedName("bi_id")
    private String biId;
    @SerializedName("program_id")
    private String programId;
    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("date_updated")
    private String dateUpdated;


    public Kegiatan(String keId, String userId, String dinasId, String keJudul, String keNoRekening, String keTahun,
                    String status, String biId, String programId, String dateCreated, String dateUpdated){
        this.keId = keId;
        this.keJudul = keJudul;
        this.keNoRekening = keNoRekening;
        this.userId = userId;
        this.dinasId = dinasId;
        this.keId = keId;
        this.keTahun = keTahun;
        this.status = status;
        this.biId = biId;
        this.programId = programId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public String getKeId() {
        return keId;
    }

    public void setKeId(String keId) {
        this.keId = keId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDinasId() {
        return dinasId;
    }

    public void setDinasId(String dinasId) {
        this.dinasId = dinasId;
    }

    public String getKeJudul() {
        return keJudul;
    }

    public void setKeJudul(String keJudul) {
        this.keJudul = keJudul;
    }

    public String getKeNoRekening() {
        return keNoRekening;
    }

    public void setKeNoRekening(String keNoRekening) {
        this.keNoRekening = keNoRekening;
    }

    public String getKeTahun() {
        return keTahun;
    }

    public void setKeTahun(String keTahun) {
        this.keTahun = keTahun;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBiId() {
        return biId;
    }

    public void setBiId(String biId) {
        this.biId = biId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Kegiatan{" +
                "keId='" + keId + '\'' +
                ", userId='" + userId + '\'' +
                ", dinasId='" + dinasId + '\'' +
                ", keJudul='" + keJudul + '\'' +
                ", keNoRekening='" + keNoRekening + '\'' +
                ", keTahun='" + keTahun + '\'' +
                ", status='" + status + '\'' +
                ", biId='" + biId + '\'' +
                ", programId='" + programId + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                '}';
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
