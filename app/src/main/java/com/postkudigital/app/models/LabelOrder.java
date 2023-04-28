package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LabelOrder implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("desc")
    private String desc;

    @Expose
    @SerializedName("logo")
    private String logo;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
