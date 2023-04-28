package com.postkudigital.app.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class OrderCart extends RealmObject implements Serializable {
    @Expose
    @SerializedName("user")
    private int user;

    @Expose
    @SerializedName("toko")
    private int toko;

    @Expose
    @SerializedName("discount")
    private int discount;

    @Expose
    @SerializedName("table")
    private int table;

    @Expose
    @SerializedName("pajak")
    private int pajak;

    @Expose
    @SerializedName("pelanggan")
    private int pelanggan;

    @Expose
    @SerializedName("tipe_order")
    private int tipeOrder;

    @Expose
    @SerializedName("label_order")
    private int labelOrder;

    @Expose
    @SerializedName("service_fee")
    private RealmList<Integer> serviceFee;

    @Expose
    @SerializedName("menu")
    private RealmList<OrderMenu> orderMenus;

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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public int getPajak() {
        return pajak;
    }

    public void setPajak(int pajak) {
        this.pajak = pajak;
    }

    public int getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(int pelanggan) {
        this.pelanggan = pelanggan;
    }

    public int getTipeOrder() {
        return tipeOrder;
    }

    public void setTipeOrder(int tipeOrder) {
        this.tipeOrder = tipeOrder;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(int labelOrder) {
        this.labelOrder = labelOrder;
    }

    public List<Integer> getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(RealmList<Integer> serviceFee) {
        this.serviceFee = serviceFee;
    }

    public RealmList<OrderMenu> getOrderMenus() {
        return orderMenus;
    }

    public void setOrderMenus(RealmList<OrderMenu> orderMenus) {
        this.orderMenus = orderMenus;
    }
}
