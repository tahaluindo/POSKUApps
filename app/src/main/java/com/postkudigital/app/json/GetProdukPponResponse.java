package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.ProductPpob;

import java.util.List;

public class GetProdukPponResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data")
    private List<ProductPpob> productPpobList;

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

    public List<ProductPpob> getProductPpobList() {
        return productPpobList;
    }

    public void setProductPpobList(List<ProductPpob> productPpobList) {
        this.productPpobList = productPpobList;
    }
}
