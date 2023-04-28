package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class Cart implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("cart_code")
    private String code;

    @Expose
    @SerializedName("nama_cart")
    private String nama;

    @Expose
    @SerializedName("ordered")
    private boolean ordered;

    @Expose
    @SerializedName("total_price")
    private double totalPrice;

    @Expose
    @SerializedName("total_disc")
    private double totalDisc;

    @Expose
    @SerializedName("total_pajak")
    private double totalPajak;

    @Expose
    @SerializedName("total_service_fee")
    private double totalServiceFee;

    @Expose
    @SerializedName("grand_total_price")
    private double grandTotal;

    @Expose
    @SerializedName("total_item")
    private int totalItem;

    @Expose
    @SerializedName("is_canceled")
    private boolean isCanceled;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    @Expose
    @SerializedName("user")
    private int user;

    @Expose
    @SerializedName("toko")
    private int toko;

    @Expose
    @SerializedName("discount")
    private String discount;

    @Expose
    @SerializedName("pajak")
    private String pajak;

    @Expose
    @SerializedName("tipe_order")
    private String tipeOrder;

    @Expose
    @SerializedName("label_order")
    private String labelOrder;

    @Expose
    @SerializedName("table")
    private String table;

    @Expose
    @SerializedName("pelanggan")
    private String pelanggan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalDisc() {
        return totalDisc;
    }

    public void setTotalDisc(double totalDisc) {
        this.totalDisc = totalDisc;
    }

    public double getTotalPajak() {
        return totalPajak;
    }

    public void setTotalPajak(double totalPajak) {
        this.totalPajak = totalPajak;
    }

    public double getTotalServiceFee() {
        return totalServiceFee;
    }

    public void setTotalServiceFee(double totalServiceFee) {
        this.totalServiceFee = totalServiceFee;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
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

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getToko() {
        return toko;
    }

    public void setToko(int toko) {
        this.toko = toko;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPajak() {
        return pajak;
    }

    public void setPajak(String pajak) {
        this.pajak = pajak;
    }

    public String getTipeOrder() {
        return tipeOrder;
    }

    public void setTipeOrder(String tipeOrder) {
        this.tipeOrder = tipeOrder;
    }

    public String getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(String labelOrder) {
        this.labelOrder = labelOrder;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }
}
