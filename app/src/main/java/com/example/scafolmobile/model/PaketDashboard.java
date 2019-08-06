package com.example.scafolmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaketDashboard {
    @SerializedName("paket_all")
    private String paketAll;
    @SerializedName("paket_progress")
    private String paketProgress;
    @SerializedName("paket_belum_mulai")
    private String paketBelumMulai;
    @SerializedName("paket_selesai")
    private String paketSelesai;
    @SerializedName("total_pagu_pptk")
    private String totalPaguPPTK;
    @SerializedName("total_real_pptk")
    private String totalRealPPTK;

    public String getPaketProgress() {
        return paketProgress;
    }

    public void setPaketProgress(String paketProgress) {
        this.paketProgress = paketProgress;
    }

    public String getTotalPaguPPTK() {
        return totalPaguPPTK;
    }

    public void setTotalPaguPPTK(String totalPaguPPTK) {
        this.totalPaguPPTK = totalPaguPPTK;
    }

    public String getTotalRealPPTK() {
        return totalRealPPTK;
    }

    public void setTotalRealPPTK(String totalRealPPTK) {
        this.totalRealPPTK = totalRealPPTK;
    }

    public String getPaketAll() {
        return paketAll;
    }

    public void setPaketAll(String paketAll) {
        this.paketAll = paketAll;
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
}
