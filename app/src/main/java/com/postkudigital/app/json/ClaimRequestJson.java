package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClaimRequestJson {
    @Expose
    @SerializedName("data")
    private List<Integer> data;

    @Expose
    @SerializedName("toko")
    private int toko;

    @Expose
    @SerializedName("total")
    private int total;

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public int getToko() {
        return toko;
    }

    public void setToko(int toko) {
        this.toko = toko;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
