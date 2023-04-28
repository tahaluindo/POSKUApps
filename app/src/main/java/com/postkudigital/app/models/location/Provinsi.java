package com.postkudigital.app.models.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Provinsi implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String nama;

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
}

