package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.order.MenuItem;

import java.util.List;

public class UpdateCartRequest {
    @Expose
    @SerializedName("id_cart")
    private Integer idCart;

    @Expose
    @SerializedName("discount")
    private Integer discount;

    @Expose
    @SerializedName("table")
    private Integer table;

    @Expose
    @SerializedName("pajak")
    private Integer pajak;

    @Expose
    @SerializedName("pelanggan")
    private Integer pelanggan;

    @Expose
    @SerializedName("tipe_order")
    private Integer tipeOrder;

    @Expose
    @SerializedName("label_order")
    private Integer labelOrder;

    @Expose
    @SerializedName("service_fee")
    private List<Integer> serviceFee;

    @Expose
    @SerializedName("menu")
    private List<MenuItem> orderMenus;

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(Integer idCart) {
        this.idCart = idCart;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public int getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public int getPajak() {
        return pajak;
    }

    public void setPajak(Integer pajak) {
        this.pajak = pajak;
    }

    public int getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Integer pelanggan) {
        this.pelanggan = pelanggan;
    }

    public int getTipeOrder() {
        return tipeOrder;
    }

    public void setTipeOrder(Integer tipeOrder) {
        this.tipeOrder = tipeOrder;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(Integer labelOrder) {
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
}
