package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdCartItemRequest {
    @Expose
    @SerializedName("id_cart_item")
    private int idcartitem;

    @Expose
    @SerializedName("discount")
    private int discount;

    @Expose
    @SerializedName("qty")
    private int qty;

    public int getIdcartitem() {
        return idcartitem;
    }

    public void setIdcartitem(int idcartitem) {
        this.idcartitem = idcartitem;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
