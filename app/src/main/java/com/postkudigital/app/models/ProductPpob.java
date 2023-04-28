package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class ProductPpob implements Serializable {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("product_name")
    private String name;

    @Expose
    @SerializedName("category")
    private String category;

    @Expose
    @SerializedName("brand")
    private String brand;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("seller_name")
    private String sellerName;

    @Expose
    @SerializedName("buyer_sku_code")
    private String buyerSkuCode;

    @Expose
    @SerializedName("start_cut_off")
    private String startCutOff;

    @Expose
    @SerializedName("desc")
    private String desc;

    @Expose
    @SerializedName("buyer_product_status")
    private boolean isBuyerProductStatus;

    @Expose
    @SerializedName("seller_product_status")
    private boolean isSellerProductStatus;

    @Expose
    @SerializedName("unlimited_stock")
    private boolean isUnlimitedStock;

    @Expose
    @SerializedName("multi")
    private boolean isMulti;

    @Expose
    @SerializedName("price")
    private int price;

    @Expose
    @SerializedName("price_postku")
    private int pricePostku;

    @Expose
    @SerializedName("stock")
    private int stock;

    @Expose
    @SerializedName("last_sync_at")
    private Date lastSyncAt;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBuyerSkuCode() {
        return buyerSkuCode;
    }

    public void setBuyerSkuCode(String buyerSkuCode) {
        this.buyerSkuCode = buyerSkuCode;
    }

    public String getStartCutOff() {
        return startCutOff;
    }

    public void setStartCutOff(String startCutOff) {
        this.startCutOff = startCutOff;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isBuyerProductStatus() {
        return isBuyerProductStatus;
    }

    public void setBuyerProductStatus(boolean buyerProductStatus) {
        isBuyerProductStatus = buyerProductStatus;
    }

    public boolean isSellerProductStatus() {
        return isSellerProductStatus;
    }

    public void setSellerProductStatus(boolean sellerProductStatus) {
        isSellerProductStatus = sellerProductStatus;
    }

    public boolean isUnlimitedStock() {
        return isUnlimitedStock;
    }

    public void setUnlimitedStock(boolean unlimitedStock) {
        isUnlimitedStock = unlimitedStock;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(Date lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}
