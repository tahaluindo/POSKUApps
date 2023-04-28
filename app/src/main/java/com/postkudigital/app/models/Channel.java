package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Channel implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("jenis_pembayaran")
    private String jenisPembayaran;

    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("nomer")
    private String nomer;

    @Expose
    @SerializedName("logo")
    private String logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJenisPembayaran() {
        return jenisPembayaran;
    }

    public void setJenisPembayaran(String jenisPembayaran) {
        this.jenisPembayaran = jenisPembayaran;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomer() {
        return nomer;
    }

    public void setNomer(String nomer) {
        this.nomer = nomer;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
