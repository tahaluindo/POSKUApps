package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Pajak;

public class TaxPostResponseJson {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status")
    private int status;

    @Expose
    @SerializedName("data")
    private Pajak pajak;

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

    public Pajak getPajak() {
        return pajak;
    }

    public void setPajak(Pajak pajak) {
        this.pajak = pajak;
    }
}
