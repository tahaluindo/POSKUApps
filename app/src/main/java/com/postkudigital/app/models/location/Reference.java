package com.postkudigital.app.models.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reference implements Serializable {
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("nama")
    private String nama;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
