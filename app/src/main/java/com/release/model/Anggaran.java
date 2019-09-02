package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Anggaran implements Serializable {
    @SerializedName("an_id")
    private String anId;
    @SerializedName("ke_id")
    private String keId;
    @SerializedName("an_nama")
    private String anNama;
    @SerializedName("an_norekening")
    private String anNorekening;
    @SerializedName("an_tahunanggaran")
    private String anTahunanggaran;
    @SerializedName("anp_pagu")
    private String anpPagu;
    @SerializedName("an_nomor_kontrak")
    private String anNomorkontrak;
    @SerializedName("an_nilai_kontrak")
    private String anNilaikontrak;
    @SerializedName("an_awal_kontrak")
    private String anAwalkontrak;
    @SerializedName("an_akhir_kontrak")
    private String anAkhirkontrak;
    @SerializedName("status")
    private String status;
    @SerializedName("pekerja_id")
    private String pakerjaId;
    @SerializedName("pptk_id")
    private String pptkId;
    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("date_updated")
    private String dateUpdated;

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getKeId() {
        return keId;
    }

    public void setKeId(String keId) {
        this.keId = keId;
    }

    public String getAnNama() {
        return anNama;
    }

    public void setAnNama(String anNama) {
        this.anNama = anNama;
    }

    public String getAnNorekening() {
        return anNorekening;
    }

    public void setAnNorekening(String anNorekening) {
        this.anNorekening = anNorekening;
    }

    public String getAnTahunanggaran() {
        return anTahunanggaran;
    }

    public void setAnTahunanggaran(String anTahunanggaran) {
        this.anTahunanggaran = anTahunanggaran;
    }

    public String getAnpPagu() {
        return anpPagu;
    }

    public void setAnpPagu(String anpPagu) {
        this.anpPagu = anpPagu;
    }

    public String getAnNomorkontrak() {
        return anNomorkontrak;
    }

    public void setAnNomorkontrak(String anNomorkontrak) {
        this.anNomorkontrak = anNomorkontrak;
    }

    public String getAnNilaikontrak() {
        return anNilaikontrak;
    }

    public void setAnNilaikontrak(String anNilaikontrak) {
        this.anNilaikontrak = anNilaikontrak;
    }

    public String getAnAwalkontrak() {
        return anAwalkontrak;
    }

    public void setAnAwalkontrak(String anAwalkontrak) {
        this.anAwalkontrak = anAwalkontrak;
    }

    public String getAnAkhirkontrak() {
        return anAkhirkontrak;
    }

    public void setAnAkhirkontrak(String anAkhirkontrak) {
        this.anAkhirkontrak = anAkhirkontrak;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPakerjaId() {
        return pakerjaId;
    }

    public void setPakerjaId(String pakerjaId) {
        this.pakerjaId = pakerjaId;
    }

    public String getPptkId() {
        return pptkId;
    }

    public void setPptkId(String pptkId) {
        this.pptkId = pptkId;
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
