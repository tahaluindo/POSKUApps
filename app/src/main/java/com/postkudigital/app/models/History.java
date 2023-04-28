package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class History implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("reff_code")
    private String invoice;

    @Expose
    @SerializedName("total")
    private int total;

    @Expose
    @SerializedName("pajak")
    private int pajak;

    @Expose
    @SerializedName("grand_total")
    private int grandTotal;

    @Expose
    @SerializedName("is_settelement")
    private boolean isSettelement;

    @Expose
    @SerializedName("is_canceled")
    private boolean isCanceled;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    @Expose
    @SerializedName("payment_type")
    private int paymentMethod;

    @Expose
    @SerializedName("cart")
    private int cart;

    @Expose
    @SerializedName("pegawai")
    private int pegawai;

    @Expose
    @SerializedName("toko")
    private int toko;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPajak() {
        return pajak;
    }

    public void setPajak(int pajak) {
        this.pajak = pajak;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public boolean isSettelement() {
        return isSettelement;
    }

    public void setSettelement(boolean settelement) {
        isSettelement = settelement;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }

    public int getPegawai() {
        return pegawai;
    }

    public void setPegawai(int pegawai) {
        this.pegawai = pegawai;
    }

    public int getToko() {
        return toko;
    }

    public void setToko(int toko) {
        this.toko = toko;
    }
}
