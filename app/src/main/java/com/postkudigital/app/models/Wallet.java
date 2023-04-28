package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wallet implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("wallet_code")
    private String walletCode;

    @Expose
    @SerializedName("balance")
    private int balance;

    @Expose
    @SerializedName("balance_req")
    private int balanceReq;

    @Expose
    @SerializedName("status_req_deposit")
    private int statusReqDepo;

    public Integer getBalanceReq() {
        return balanceReq;
    }

    public void setBalanceReq(Integer balanceReq) {
        this.balanceReq = balanceReq;
    }

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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getStatusReqDepo() {
        return statusReqDepo;
    }

    public void setStatusReqDepo(int statusReqDepo) {
        this.statusReqDepo = statusReqDepo;
    }
}
