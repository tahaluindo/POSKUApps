package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Brand implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("brand_ppob_name")
    private String name;

    @Expose
    @SerializedName("brand_ppob_key")
    private String key;

    @Expose
    @SerializedName("brand_ppob_image")
    private String image;

    @Expose
    @SerializedName("category_ppob")
    private String categoryPpob;

    public String getCategoryPpob() {
        return categoryPpob;
    }

    public void setCategoryPpob(String categoryPpob) {
        this.categoryPpob = categoryPpob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
