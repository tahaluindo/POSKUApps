package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class ClaimSaldo implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("data_trx")
    private List<Transaction> transactionList;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("status_settelement")
    private boolean statusSettlement;

    @Expose
    @SerializedName("total")
    private int total;

    @Expose
    @SerializedName("pic_claim")
    private String image;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatusSettlement() {
        return statusSettlement;
    }

    public void setStatusSettlement(boolean statusSettlement) {
        this.statusSettlement = statusSettlement;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
