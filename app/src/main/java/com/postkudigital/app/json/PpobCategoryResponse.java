package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.KategoriPpob;

import java.util.List;

public class PpobCategoryResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<KategoriPpob> kategoriPpobList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<KategoriPpob> getKategoriPpobList() {
        return kategoriPpobList;
    }

    public void setKategoriPpobList(List<KategoriPpob> kategoriPpobList) {
        this.kategoriPpobList = kategoriPpobList;
    }
}
