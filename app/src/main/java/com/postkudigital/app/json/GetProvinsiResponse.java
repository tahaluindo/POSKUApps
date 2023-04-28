package com.postkudigital.app.json;

import com.postkudigital.app.models.location.Provinsi;

import java.util.List;

public class GetProvinsiResponse {
    private List<Provinsi> provinsiList;

    public List<Provinsi> getProvinsiList() {
        return provinsiList;
    }

    public void setProvinsiList(List<Provinsi> provinsiList) {
        this.provinsiList = provinsiList;
    }
}
