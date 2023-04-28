package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Promo;

import java.util.List;

public class GetPromoResponseJson {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int status;

    @Expose
    @SerializedName("data")
    private List<Promo> promoList;

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

    public List<Promo> getPromoList() {
        return promoList;
    }

    public void setPromoList(List<Promo> promoList) {
        this.promoList = promoList;
    }
}
