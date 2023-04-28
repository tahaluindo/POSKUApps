package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.location.Kelurahan;


import java.util.List;

public class GetDesaResponse {
    @Expose
    @SerializedName("kelurahan")
    private List<Kelurahan> kelurahanList;

    public List<Kelurahan> getKelurahanList() {
        return kelurahanList;
    }

    public void setKelurahanList(List<Kelurahan> kelurahanList) {
        this.kelurahanList = kelurahanList;
    }
}
