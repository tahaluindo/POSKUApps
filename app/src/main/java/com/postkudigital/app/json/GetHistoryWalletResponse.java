package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.HistoryWallet;

import java.util.List;

public class GetHistoryWalletResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<HistoryWallet> historyWalletList;

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

    public List<HistoryWallet> getHistoryWalletList() {
        return historyWalletList;
    }

    public void setHistoryWalletList(List<HistoryWallet> historyWalletList) {
        this.historyWalletList = historyWalletList;
    }
}
