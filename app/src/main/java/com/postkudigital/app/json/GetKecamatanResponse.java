package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.location.Kecamatan;


import java.util.List;

public class GetKecamatanResponse {
    @Expose
    @SerializedName("kecamatan")
    private List<Kecamatan> kecamatanList;

    public List<Kecamatan> getKecamatanList() {
        return kecamatanList;
    }

    public void setKecamatanList(List<Kecamatan> kecamatanList) {
        this.kecamatanList = kecamatanList;
    }
}
