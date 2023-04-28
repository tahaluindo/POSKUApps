package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.HistoryStock;

import java.util.List;

public class StockTrxResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<HistoryStock> historyStockList;

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

    public List<HistoryStock> getHistoryStockList() {
        return historyStockList;
    }

    public void setHistoryStockList(List<HistoryStock> historyStockList) {
        this.historyStockList = historyStockList;
    }
}
