package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class Transaction implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("reff_code")
    private String reffCode;

    @Expose
    @SerializedName("total")
    private double total;

    @Expose
    @SerializedName("pajak")
    private double pajak;

    @Expose
    @SerializedName("grand_total")
    private double grandTotal;

    @Expose
    @SerializedName("uang_bayar")
    private double bayar;

    @Expose
    @SerializedName("uang_kembalian")
    private double kembalian;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    @Expose
    @SerializedName("payment_type")
    private int paymentType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReffCode() {
        return reffCode;
    }

    public void setReffCode(String reffCode) {
        this.reffCode = reffCode;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPajak() {
        return pajak;
    }

    public void setPajak(double pajak) {
        this.pajak = pajak;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getBayar() {
        return bayar;
    }

    public void setBayar(double bayar) {
        this.bayar = bayar;
    }

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }
}
