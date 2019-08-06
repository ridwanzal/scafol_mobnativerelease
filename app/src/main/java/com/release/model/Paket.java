package com.release.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Paket implements Serializable {
    @SerializedName("pa_id")
    private String paId;
    @SerializedName("ke_id")
    private String keId;
    @SerializedName("pekerja_id")
    private String pekerjaId;
    @SerializedName("pptk_id")
    private String pptkId;
    @SerializedName("pa_judul")
    private String paJudul;
    @SerializedName("pa_jenis")
    private String paJenis;
    @SerializedName("pa_volume")
    private String paVolume;
    @SerializedName("pa_satuan")
    private String paSatuan;
    @SerializedName("pa_tahun")
    private String paTahun;
    @SerializedName("pa_pagu")
    private String paPagu;
    @SerializedName("pa_lokasi")
    private String paLokasi;
    @SerializedName("pa_loc_latitude")
    private String paLocLatitude;
    @SerializedName("pa_loc_longitude")
    private String paLongitude;

    @SerializedName("pa_nomor_kontrak")
    private String paNomorKontrak;

    @SerializedName("pa_nilai_kontrak")
    private String paNilaiKontrak;
    @SerializedName("pa_rencana_kontrak")
    private String paRencanKonrak;
    @SerializedName("pa_awal_kontrak")
    private String paAwalKontrak;
    @SerializedName("pa_akhir_kontrak")
    private String paAkhirKontrak;
    @SerializedName("status")
    private String status;
    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("date_updated")
    private String dateUpdated;
    public Paket(String paId, String keId, String pekerjaId, String pptkId, String paJudul, String paJenis, String paVolume, String paSatuan, String paTahun, String paPagu, String paLokasi, String paLocLatitude, String paNomorKontrak, String paNilaiKontrak, String paRencanKonrak, String paAwalKontrak, String paAkhirKontrak, String status, String dateCreated, String dateUpdated) {
        this.paId = paId;
        this.keId = keId;
        this.pekerjaId = pekerjaId;
        this.pptkId = pptkId;
        this.paJudul = paJudul;
        this.paJenis = paJenis;
        this.paVolume = paVolume;
        this.paSatuan = paSatuan;
        this.paTahun = paTahun;
        this.paPagu = paPagu;
        this.paLokasi = paLokasi;
        this.paLocLatitude = paLocLatitude;
        this.paLocLatitude = paLocLatitude;
        this.paNomorKontrak = paNomorKontrak;
        this.paNilaiKontrak = paNilaiKontrak;
        this.paRencanKonrak = paRencanKonrak;
        this.paAwalKontrak = paAwalKontrak;
        this.paAkhirKontrak = paAkhirKontrak;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public String getPaLongitude() {
        return paLongitude;
    }

    public void setPaLongitude(String paLongitude) {
        this.paLongitude = paLongitude;
    }

    @Override
    public String toString() {
        return "Paket{" +
                "paId='" + paId + '\'' +
                ", keId='" + keId + '\'' +
                ", pekerjaId='" + pekerjaId + '\'' +
                ", pptkId='" + pptkId + '\'' +
                ", paJudul='" + paJudul + '\'' +
                ", paJenis='" + paJenis + '\'' +
                ", paVolume='" + paVolume + '\'' +
                ", paSatuan='" + paSatuan + '\'' +
                ", paTahun='" + paTahun + '\'' +
                ", paPagu='" + paPagu + '\'' +
                ", paLokasi='" + paLokasi + '\'' +
                ", paLocLatitude='" + paLocLatitude + '\'' +
                ", paNomorKontrak='" + paNomorKontrak + '\'' +
                ", paNilaiKontrak='" + paNilaiKontrak + '\'' +
                ", paRencanKonrak='" + paRencanKonrak + '\'' +
                ", paAwalKontrak='" + paAwalKontrak + '\'' +
                ", paAkhirKontrak='" + paAkhirKontrak + '\'' +
                ", status='" + status + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateUpdated='" + dateUpdated + '\'' +
                '}';
    }

    public String getPaId() {
        return paId;
    }

    public void setPaId(String paId) {
        this.paId = paId;
    }

    public String getKeId() {
        return keId;
    }

    public void setKeId(String keId) {
        this.keId = keId;
    }

    public String getPekerjaId() {
        return pekerjaId;
    }

    public void setPekerjaId(String pekerjaId) {
        this.pekerjaId = pekerjaId;
    }

    public String getPptkId() {
        return pptkId;
    }

    public void setPptkId(String pptkId) {
        this.pptkId = pptkId;
    }

    public String getPaJudul() {
        return paJudul;
    }

    public void setPaJudul(String paJudul) {
        this.paJudul = paJudul;
    }

    public String getPaJenis() {
        return paJenis;
    }

    public void setPaJenis(String paJenis) {
        this.paJenis = paJenis;
    }

    public String getPaVolume() {
        return paVolume;
    }

    public void setPaVolume(String paVolume) {
        this.paVolume = paVolume;
    }

    public String getPaSatuan() {
        return paSatuan;
    }

    public void setPaSatuan(String paSatuan) {
        this.paSatuan = paSatuan;
    }

    public String getPaTahun() {
        return paTahun;
    }

    public void setPaTahun(String paTahun) {
        this.paTahun = paTahun;
    }

    public String getPaPagu() {
        return paPagu;
    }

    public void setPaPagu(String paPagu) {
        this.paPagu = paPagu;
    }

    public String getPaLokasi() {
        return paLokasi;
    }

    public void setPaLokasi(String paLokasi) {
        this.paLokasi = paLokasi;
    }

    public String getPaLocLatitude() {
        return paLocLatitude;
    }

    public void setPaLocLatitude(String paLocLatitude) {
        this.paLocLatitude = paLocLatitude;
    }

    public String getPaNomorKontrak() {
        return paNomorKontrak;
    }

    public void setPaNomorKontrak(String paNomorKontrak) {
        this.paNomorKontrak = paNomorKontrak;
    }

    public String getPaNilaiKontrak() {
        return paNilaiKontrak;
    }

    public void setPaNilaiKontrak(String paNilaiKontrak) {
        this.paNilaiKontrak = paNilaiKontrak;
    }

    public String getPaRencanKonrak() {
        return paRencanKonrak;
    }

    public void setPaRencanKonrak(String paRencanKonrak) {
        this.paRencanKonrak = paRencanKonrak;
    }

    public String getPaAwalKontrak() {
        return paAwalKontrak;
    }

    public void setPaAwalKontrak(String paAwalKontrak) {
        this.paAwalKontrak = paAwalKontrak;
    }

    public String getPaAkhirKontrak() {
        return paAkhirKontrak;
    }

    public void setPaAkhirKontrak(String paAkhirKontrak) {
        this.paAkhirKontrak = paAkhirKontrak;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
