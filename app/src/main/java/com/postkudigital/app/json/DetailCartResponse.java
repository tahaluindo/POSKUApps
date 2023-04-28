package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Cart;
import com.postkudigital.app.models.ItemCart;

import java.util.List;

public class DetailCartResponse {
    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("jumlah_items")
    private int jmlItem;

    @Expose
    @SerializedName("data_cart")
    private Cart cart;

    @Expose
    @SerializedName("data_cart_items")
    private List<ItemCart> itemCartList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getJmlItem() {
        return jmlItem;
    }

    public void setJmlItem(int jmlItem) {
        this.jmlItem = jmlItem;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<ItemCart> getItemCartList() {
        return itemCartList;
    }

    public void setItemCartList(List<ItemCart> itemCartList) {
        this.itemCartList = itemCartList;
    }
}
