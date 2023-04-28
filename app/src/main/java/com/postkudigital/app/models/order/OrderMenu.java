package com.postkudigital.app.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OrderMenu extends RealmObject implements Serializable {
    @PrimaryKey
    @Expose
    @SerializedName("idmenu")
    private int idMenu;

    @Expose
    @SerializedName("qty")
    private int qty;

    @Expose
    @SerializedName("disc")
    private int disc;

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

    public int getDisc() {
        return disc;
    }

    public void setDisc(int disc) {
        this.disc = disc;
    }
}
