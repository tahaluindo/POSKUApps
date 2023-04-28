package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetContactUsResponse {
    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<Kontak> kontakList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Kontak> getKontakList() {
        return kontakList;
    }

    public void setKontakList(List<Kontak> kontakList) {
        this.kontakList = kontakList;
    }
}
