package com.postkudigital.app.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OrderLocal extends RealmObject implements Serializable {
    @PrimaryKey
    @Expose
    @SerializedName("idMenu")
    private int idMenu;

    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("harga")
    private int harga;

    @Expose
    @SerializedName("qty")
    private int qty;

    @Expose
    @SerializedName("disc")
    private int disc;

    @Expose
    @SerializedName("total_harga")
    private int totalHarga;

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getDisc() {
        return disc;
    }

    public void setDisc(int disc) {
        this.disc = disc;
    }
}
