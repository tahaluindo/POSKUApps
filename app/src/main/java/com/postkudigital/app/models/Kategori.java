package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kategori implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("label")
    private String label;

    @Expose
    @SerializedName("is_active")
    private boolean isActive;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("toko")
    private int toko;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getToko() {
        return toko;
    }

    public void setToko(int toko) {
        this.toko = toko;
    }
}
