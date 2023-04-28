package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class HistoryWallet implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("wallet_code")
    private String walletCode;

    @Expose
    @SerializedName("reff_id")
    private String reffId;

    @Expose
    @SerializedName("type")
    private int type;

    @Expose
    @SerializedName("adjustment_balance")
    private int balance;

    @Expose
    @SerializedName("note")
    private String note;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

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

    public String getReffId() {
        return reffId;
    }

    public void setReffId(String reffId) {
        this.reffId = reffId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
