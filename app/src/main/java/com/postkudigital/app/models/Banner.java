package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class Banner implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    @SerializedName("body")
    private String body;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
