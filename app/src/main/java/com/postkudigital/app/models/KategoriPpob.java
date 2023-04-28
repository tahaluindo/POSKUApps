package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KategoriPpob implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("category_ppob_name")
    private String catName;

    @Expose
    @SerializedName("category_ppob_key")
    private String catKey;

    @Expose
    @SerializedName("category_ppob_image")
    private String catImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatKey() {
        return catKey;
    }

    public void setCatKey(String catKey) {
        this.catKey = catKey;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
