package com.postkudigital.app.models.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kelurahan implements Serializable {
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("district_id")
    private String idKecamatan;

    @Expose
    @SerializedName("name")
    private String nama;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKecamatan() {
        return idKecamatan;
    }

    public void setIdKecamatan(String idKecamatan) {
        this.idKecamatan = idKecamatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
