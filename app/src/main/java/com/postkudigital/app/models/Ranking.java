package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ranking implements Serializable {
    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("qty")
    private int qty;

    @Expose
    @SerializedName("nominal")
    private double nominal;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }
}
