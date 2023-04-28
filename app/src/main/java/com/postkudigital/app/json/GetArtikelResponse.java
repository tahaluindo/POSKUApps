package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Artikel;

import java.util.List;

public class GetArtikelResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("next_link")
    private String nextLink;

    @Expose
    @SerializedName("previous_link")
    private String prevLink;

    @Expose
    @SerializedName("data")
    private List<Artikel> artikelList;

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

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public String getPrevLink() {
        return prevLink;
    }

    public void setPrevLink(String prevLink) {
        this.prevLink = prevLink;
    }

    public List<Artikel> getArtikelList() {
        return artikelList;
    }

    public void setArtikelList(List<Artikel> artikelList) {
        this.artikelList = artikelList;
    }
}
