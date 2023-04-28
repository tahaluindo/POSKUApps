package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Toko implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("alamat")
    private String alamat;

    @Expose
    @SerializedName("add_provinsi")
    private String provinsi;

    @Expose
    @SerializedName("add_kab_kot")
    private String kabKota;

    @Expose
    @SerializedName("add_kecamatan")
    private String kecamatan;

    @Expose
    @SerializedName("add_kel_des")
    private String kelDes;

    @Expose
    @SerializedName("logo")
    private String logo;

    @Expose
    @SerializedName("kategori")
    private String kategori;

    @Expose
    @SerializedName("is_active")
    private boolean isActive;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabKota() {
        return kabKota;
    }

    public void setKabKota(String kabKota) {
        this.kabKota = kabKota;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKelDes() {
        return kelDes;
    }

    public void setKelDes(String kelDes) {
        this.kelDes = kelDes;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
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
}
