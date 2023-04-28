package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class TransPpob implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("ref_id")
    private String reffId;

    @Expose
    @SerializedName("customer_no")
    private String customerNo;

    @Expose
    @SerializedName("buyer_sku_code")
    private String buyerSkuCode;

    @Expose
    @SerializedName("product_name")
    private String productName;

    @Expose
    @SerializedName("category")
    private String category;

    @Expose
    @SerializedName("brand")
    private String brand;

    @Expose
    @SerializedName("desc")
    private String desc;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("buyer_last_saldo")
    private int buyerLastSaldo;

    @Expose
    @SerializedName("price")
    private int price;

    @Expose
    @SerializedName("price_postku")
    private int pricePostku;

    @Expose
    @SerializedName("wallet")
    private int walletId;

    @Expose
    @SerializedName("is_refunded")
    private boolean isRefunded;

    @Expose
    @SerializedName("created_at")
    private Date createdAt;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReffId() {
        return reffId;
    }

    public void setReffId(String reffId) {
        this.reffId = reffId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getBuyerSkuCode() {
        return buyerSkuCode;
    }

    public void setBuyerSkuCode(String buyerSkuCode) {
        this.buyerSkuCode = buyerSkuCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBuyerLastSaldo() {
        return buyerLastSaldo;
    }

    public void setBuyerLastSaldo(int buyerLastSaldo) {
        this.buyerLastSaldo = buyerLastSaldo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPricePostku() {
        return pricePostku;
    }

    public void setPricePostku(int pricePostku) {
        this.pricePostku = pricePostku;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void setRefunded(boolean refunded) {
        isRefunded = refunded;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
