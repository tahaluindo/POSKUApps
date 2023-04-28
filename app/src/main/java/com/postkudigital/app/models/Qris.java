package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class Qris implements Serializable {
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("external_id")
    private String externalId;

    @Expose
    @SerializedName("amount")
    private double amount;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("qr_string")
    private String qrString;

    @Expose
    @SerializedName("callback_url")
    private String callbackUrl;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("created")
    private Date created;

    @Expose
    @SerializedName("updated")
    private Date updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
