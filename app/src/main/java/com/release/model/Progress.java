package com.release.model;

import com.google.gson.annotations.SerializedName;

public class Progress {
    @SerializedName("pr_id")
    private String pr_id;

    @SerializedName("ke_id")
    private String ke_id;

    @SerializedName("pa_id")
    private String pa_id;

    @SerializedName("pr_tanggal")
    private String pr_tanggal;

    @SerializedName("pr_target")
    private String pr_target;

    @SerializedName("pr_real")
    private String pr_real;

    @SerializedName("pr_deviasi")
    private String pr_deviasi;

    @SerializedName("pr_daya_serap_kontrak")
    private String pr_daya_serap_kontrak;

    @SerializedName("pr_sisa_anggaran")
    private String pr_sisa_anggaran;

    @SerializedName("pr_sisa_kontrak")
    private String pr_sisa_kontrak;

    @SerializedName("pr_keterangan")
    private String pr_keterangan;

    @SerializedName("pr_jenis")
    private String pr_jenis;

    @SerializedName("date_created")
    private String date_created;

    @SerializedName("date_updated")
    private String date_updated;

    @SerializedName("jumlah")
    private String jumlah;

    @Override
    public String toString() {
        return "Progress{" +
                "pr_id='" + pr_id + '\'' +
                ", ke_id='" + ke_id + '\'' +
                ", pa_id='" + pa_id + '\'' +
                ", pr_tanggal='" + pr_tanggal + '\'' +
                ", pr_target='" + pr_target + '\'' +
                ", pr_real='" + pr_real + '\'' +
                ", pr_deviasi='" + pr_deviasi + '\'' +
                ", pr_daya_serap_kontrak='" + pr_daya_serap_kontrak + '\'' +
                ", pr_sisa_anggaran='" + pr_sisa_anggaran + '\'' +
                ", pr_sisa_kontrak='" + pr_sisa_kontrak + '\'' +
                ", pr_keterangan='" + pr_keterangan + '\'' +
                ", pr_jenis='" + pr_jenis + '\'' +
                ", date_created='" + date_created + '\'' +
                ", date_updated='" + date_updated + '\'' +
                '}';
    }

    public String getPr_id() {
        return pr_id;
    }

    public void setPr_id(String pr_id) {
        this.pr_id = pr_id;
    }

    public String getKe_id() {
        return ke_id;
    }

    public void setKe_id(String ke_id) {
        this.ke_id = ke_id;
    }

    public String getPa_id() {
        return pa_id;
    }

    public void setPa_id(String pa_id) {
        this.pa_id = pa_id;
    }

    public String getPr_tanggal() {
        return pr_tanggal;
    }

    public void setPr_tanggal(String pr_tanggal) {
        this.pr_tanggal = pr_tanggal;
    }

    public String getPr_target() {
        return pr_target;
    }

    public void setPr_target(String pr_target) {
        this.pr_target = pr_target;
    }

    public String getPr_real() {
        return pr_real;
    }

    public void setPr_real(String pr_real) {
        this.pr_real = pr_real;
    }

    public String getPr_deviasi() {
        return pr_deviasi;
    }

    public void setPr_deviasi(String pr_deviasi) {
        this.pr_deviasi = pr_deviasi;
    }

    public String getPr_daya_serap_kontrak() {
        return pr_daya_serap_kontrak;
    }

    public void setPr_daya_serap_kontrak(String pr_daya_serap_kontrak) {
        this.pr_daya_serap_kontrak = pr_daya_serap_kontrak;
    }

    public String getPr_sisa_anggaran() {
        return pr_sisa_anggaran;
    }

    public void setPr_sisa_anggaran(String pr_sisa_anggaran) {
        this.pr_sisa_anggaran = pr_sisa_anggaran;
    }

    public String getPr_sisa_kontrak() {
        return pr_sisa_kontrak;
    }

    public void setPr_sisa_kontrak(String pr_sisa_kontrak) {
        this.pr_sisa_kontrak = pr_sisa_kontrak;
    }

    public String getPr_keterangan() {
        return pr_keterangan;
    }

    public void setPr_keterangan(String pr_keterangan) {
        this.pr_keterangan = pr_keterangan;
    }

    public String getPr_jenis() {
        return pr_jenis;
    }

    public void setPr_jenis(String pr_jenis) {
        this.pr_jenis = pr_jenis;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
