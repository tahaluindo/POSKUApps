package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stock implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("current_stock")
    private int currentStock;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("menu")
    private int menu;

    @Expose
    @SerializedName("toko")
    private int toko;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public int getToko() {
        return toko;
    }

    public void setToko(int toko) {
        this.toko = toko;
    }
}
