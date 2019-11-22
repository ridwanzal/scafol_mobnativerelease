package com.release.model;

import com.google.gson.annotations.SerializedName;

public class Kontraktor {
    // linier dengan table sc_kontraktor
    @SerializedName("ko_id")
    private String koId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("ko_nama")
    private String koNama;
    @SerializedName("ko_siup")
    private String koSiup;
    @SerializedName("ko_tdp")
    private String koTdp;
    @SerializedName("ko_npwp")
    private String koNpwp;
    @SerializedName("ko_gambar")
    private String koGambar;
    @SerializedName("ko_nama_perusahaan")
    private String koNamaPerusahaan;
    @SerializedName("ko_alamat_perusahaan")
    private String koAlamatPerusahaan;
    @SerializedName("ko_telp_kantor")
    private String koTelpkantor;

    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("date_updated")
    private String dateUpdated;
    @SerializedName("is_deleted")
    private String isDeleted;

    public String getKoId() {
        return koId;
    }

    public void setKoId(String koId) {
        this.koId = koId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKoNama() {
        return koNama;
    }

    public void setKoNama(String koNama) {
        this.koNama = koNama;
    }

    public String getKoSiup() {
        return koSiup;
    }

    public void setKoSiup(String koSiup) {
        this.koSiup = koSiup;
    }

    public String getKoTdp() {
        return koTdp;
    }

    public void setKoTdp(String koTdp) {
        this.koTdp = koTdp;
    }

    public String getKoNpwp() {
        return koNpwp;
    }

    public void setKoNpwp(String koNpwp) {
        this.koNpwp = koNpwp;
    }

    public String getKoGambar() {
        return koGambar;
    }

    public void setKoGambar(String koGambar) {
        this.koGambar = koGambar;
    }

    public String getKoNamaPerusahaan() {
        return koNamaPerusahaan;
    }

    public void setKoNamaPerusahaan(String koNamaPerusahaan) {
        this.koNamaPerusahaan = koNamaPerusahaan;
    }

    public String getKoAlamatPerusahaan() {
        return koAlamatPerusahaan;
    }

    public void setKoAlamatPerusahaan(String koAlamatPerusahaan) {
        this.koAlamatPerusahaan = koAlamatPerusahaan;
    }

    public String getKoTelpkantor() {
        return koTelpkantor;
    }

    public void setKoTelpkantor(String koTelpkantor) {
        this.koTelpkantor = koTelpkantor;
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
        return "Kontraktor{" +
                "koId='" + koId + '\'' +
                ", userId='" + userId + '\'' +
                ", koNama='" + koNama + '\'' +
                ", koSiup='" + koSiup + '\'' +
                ", koTdp='" + koTdp + '\'' +
                ", koNpwp='" + koNpwp + '\'' +
                ", koGambar='" + koGambar + '\'' +
                ", koNamaPerusahaan='" + koNamaPerusahaan + '\'' +
                ", koAlamatPerusahaan='" + koAlamatPerusahaan + '\'' +
                ", koTelpkantor='" + koTelpkantor + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                '}';
    }
}
