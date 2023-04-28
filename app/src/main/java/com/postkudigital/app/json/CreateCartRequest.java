package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.order.MenuItem;

import java.util.List;

public class CreateCartRequest {
    @Expose
    @SerializedName("user")
    private String user;

    @Expose
    @SerializedName("toko")
    private String toko;

    @Expose
    @SerializedName("id_cart")
    private int idCart;

    @Expose
    @SerializedName("discount")
    private String discount;

    @Expose
    @SerializedName("table")
    private String table;

    @Expose
    @SerializedName("pajak")
    private String pajak;

    @Expose
    @SerializedName("pelanggan")
    private String pelanggan;

    @Expose
    @SerializedName("tipe_order")
    private String tipeOrder;

    @Expose
    @SerializedName("label_order")
    private String labelOrder;

    @Expose
    @SerializedName("service_fee")
    private List<Integer> serviceFee;

    @Expose
    @SerializedName("menu")
    private List<MenuItem> orderMenus;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPajak() {
        return pajak;
    }

    public void setPajak(String pajak) {
        this.pajak = pajak;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
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

    public List<Integer> getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(List<Integer> serviceFee) {
        this.serviceFee = serviceFee;
    }

    public List<MenuItem> getOrderMenus() {
        return orderMenus;
    }

    public void setOrderMenus(List<MenuItem> orderMenus) {
        this.orderMenus = orderMenus;
    }

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(int idCart) {
        this.idCart = idCart;
    }
}
