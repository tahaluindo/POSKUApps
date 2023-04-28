package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Toko;

import java.util.List;

public class GetOutletResponseJson {
    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("status_code")
    private String statusCode;

    @Expose
    @SerializedName("data")
    private List<Toko> tokoList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<Toko> getTokoList() {
        return tokoList;
    }

    public void setTokoList(List<Toko> tokoList) {
        this.tokoList = tokoList;
    }
}
