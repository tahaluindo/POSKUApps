package com.postkudigital.app.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;

public class MenuItem implements Serializable {
    @PrimaryKey
    @Expose
    @SerializedName("idmenu")
    private int idMenu;

    @Expose
    @SerializedName("qty")
    private int qty;

    @Expose
    @SerializedName("disc")
    private Integer disc;

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Integer getDisc() {
        return disc;
    }

    public void setDisc(Integer disc) {
        this.disc = disc;
    }
}
