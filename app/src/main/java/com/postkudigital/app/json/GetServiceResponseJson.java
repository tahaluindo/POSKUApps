package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.ServiceFee;

import java.util.List;

public class GetServiceResponseJson {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int status;

    @Expose
    @SerializedName("data")
    private List<ServiceFee> serviceFees;

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

    public List<ServiceFee> getServiceFees() {
        return serviceFees;
    }

    public void setServiceFees(List<ServiceFee> serviceFees) {
        this.serviceFees = serviceFees;
    }
}
