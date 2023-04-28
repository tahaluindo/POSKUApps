package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Meja;

import java.util.List;

public class GetTableResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int status;

    @Expose
    @SerializedName("data")
    private List<Meja> mejaList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Meja> getMejaList() {
        return mejaList;
    }

    public void setMejaList(List<Meja> mejaList) {
        this.mejaList = mejaList;
    }
}
