package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PpobResponse implements Serializable {
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
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("rc")
    private String rc;

    @Expose
    @SerializedName("buyer_last_saldo")
    private int buyerLastSaldo;

    @Expose
    @SerializedName("sn")
    private String sn;

    @Expose
    @SerializedName("price")
    private int price;

    @Expose
    @SerializedName("tele")
    private String tele;

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

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public int getBuyerLastSaldo() {
        return buyerLastSaldo;
    }

    public void setBuyerLastSaldo(int buyerLastSaldo) {
        this.buyerLastSaldo = buyerLastSaldo;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }
}
