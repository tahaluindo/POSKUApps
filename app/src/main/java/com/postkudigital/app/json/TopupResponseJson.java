package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopupResponseJson {
    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("data")
    private int data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
