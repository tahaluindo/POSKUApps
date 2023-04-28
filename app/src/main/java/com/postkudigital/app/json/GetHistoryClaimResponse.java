package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.ClaimSaldo;

import java.util.List;

public class GetHistoryClaimResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<ClaimSaldo> claimSaldoList;

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

    public List<ClaimSaldo> getClaimSaldoList() {
        return claimSaldoList;
    }

    public void setClaimSaldoList(List<ClaimSaldo> claimSaldoList) {
        this.claimSaldoList = claimSaldoList;
    }
}
