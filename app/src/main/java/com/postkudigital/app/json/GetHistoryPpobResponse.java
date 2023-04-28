package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.TransPpob;

import java.util.List;

public class GetHistoryPpobResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("total_transaksi")
    private int totalTransaksi;

    @Expose
    @SerializedName("jumlah_transaksi")
    private int jumlahTransaksi;

    @Expose
    @SerializedName("data")
    private List<TransPpob> transPpobList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(int totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(int jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public List<TransPpob> getTransPpobList() {
        return transPpobList;
    }

    public void setTransPpobList(List<TransPpob> transPpobList) {
        this.transPpobList = transPpobList;
    }
}
