package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class HistoryTopup implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("wallet_code")
    private String walletCode;

    @Expose
    @SerializedName("reason")
    private String reason;

    @Expose
    @SerializedName("status_confirm")
    private int statusConfirm;

    @Expose
    @SerializedName("balance")
    private int balance;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    @Expose
    @SerializedName("channel")
    private int channel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatusConfirm() {
        return statusConfirm;
    }

    public void setStatusConfirm(int statusConfirm) {
        this.statusConfirm = statusConfirm;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
