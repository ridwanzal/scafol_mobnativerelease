package com.release.model;

import com.google.gson.annotations.SerializedName;

public class PaketDashboardSuper {
    @SerializedName("total_paket_daerah")
    private String totalPaket;
    @SerializedName("total_paket_sedang_bekerja")
    private String paketProgress;
    @SerializedName("total_paket_belum_mulai")
    private String paketBelumMulai;
    @SerializedName("total_paket_selesai")
    private String paketSelesai;
    @SerializedName("total_pagu_daerah")
    private String totalPaguDaerah;
    @SerializedName("total_real_daerah")
    private String totalRealDaerah;
    @SerializedName("sisa_pagu_daerah")
    private String sisaPaguDaerah;

    public String getTotalPaket() {
        return totalPaket;
    }

    public void setTotalPaket(String totalPaket) {
        this.totalPaket = totalPaket;
    }

    public String getPaketProgress() {
        return paketProgress;
    }

    public void setPaketProgress(String paketProgress) {
        this.paketProgress = paketProgress;
    }

    public String getPaketBelumMulai() {
        return paketBelumMulai;
    }

    public void setPaketBelumMulai(String paketBelumMulai) {
        this.paketBelumMulai = paketBelumMulai;
    }

    public String getPaketSelesai() {
        return paketSelesai;
    }

    public void setPaketSelesai(String paketSelesai) {
        this.paketSelesai = paketSelesai;
    }

    public String getTotalPaguDaerah() {
        return totalPaguDaerah;
    }

    public void setTotalPaguDaerah(String totalPaguDaerah) {
        this.totalPaguDaerah = totalPaguDaerah;
    }

    public String getTotalRealDaerah() {
        return totalRealDaerah;
    }

    public void setTotalRealDaerah(String totalRealDaerah) {
        this.totalRealDaerah = totalRealDaerah;
    }

    public String getSisaPaguDaerah() {
        return sisaPaguDaerah;
    }

    public void setSisaPaguDaerah(String sisaPaguDaerah) {
        this.sisaPaguDaerah = sisaPaguDaerah;
    }
}
