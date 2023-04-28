package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.ServiceFee;

public class ServicePostResponseJson {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status")
    private int status;

    @Expose
    @SerializedName("data")
    private ServiceFee serviceFee;

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

    public ServiceFee getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(ServiceFee serviceFee) {
        this.serviceFee = serviceFee;
    }
}
