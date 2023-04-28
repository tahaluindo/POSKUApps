package com.postkudigital.app.models.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kota implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("province_id")
    private String idProvinsi;

    @Expose
    @SerializedName("name")
    private String nama;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProvinsi() {
        return idProvinsi;
    }

    public void setIdProvinsi(String idProvinsi) {
        this.idProvinsi = idProvinsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
