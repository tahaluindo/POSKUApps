package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemCart implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("menu_name")
    private MenuName menuName;

    @Expose
    @SerializedName("disc_name")
    private DiscountName discName;

    @Expose
    @SerializedName("qty")
    private int qty;

    @Expose
    @SerializedName("ordered")
    private boolean ordered;

    @Expose
    @SerializedName("ordered_at")
    private String orderedAt;

    @Expose
    @SerializedName("price")
    private double price;

    @Expose
    @SerializedName("hpp")
    private double hpp;

    @Expose
    @SerializedName("total_disc")
    private double totalDisc;

    @Expose
    @SerializedName("grand_total_price")
    private double grandTotalPrice;

    @Expose
    @SerializedName("is_canceled")
    private boolean isCanceled;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("cart")
    private int cart;

    @Expose
    @SerializedName("menu")
    private int menu;

    @Expose
    @SerializedName("menu_kategori")
    private String kategori;

    @Expose
    @SerializedName("toko")
    private int toko;

    @Expose
    @SerializedName("discount")
    private Integer discount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MenuName getMenuName() {
        return menuName;
    }

    public void setMenuName(MenuName menuName) {
        this.menuName = menuName;
    }

    public DiscountName getDiscName() {
        return discName;
    }

    public void setDiscName(DiscountName discName) {
        this.discName = discName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public String getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(String orderedAt) {
        this.orderedAt = orderedAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHpp() {
        return hpp;
    }

    public void setHpp(double hpp) {
        this.hpp = hpp;
    }

    public double getTotalDisc() {
        return totalDisc;
    }

    public void setTotalDisc(double totalDisc) {
        this.totalDisc = totalDisc;
    }

    public double getGrandTotalPrice() {
        return grandTotalPrice;
    }

    public void setGrandTotalPrice(double grandTotalPrice) {
        this.grandTotalPrice = grandTotalPrice;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }

    public int getMenu() {
        return menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getToko() {
        return toko;
    }

    public void setToko(int toko) {
        this.toko = toko;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}


