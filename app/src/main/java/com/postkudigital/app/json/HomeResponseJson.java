package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Artikel;
import com.postkudigital.app.models.Banner;

import java.util.List;

public class HomeResponseJson {
    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("wallet")
    private String wallet;

    @Expose
    @SerializedName("qris")
    private String qris;

    @Expose
    @SerializedName("total_trx")
    private String totaltrx;

    @Expose
    @SerializedName("pendapatan")
    private String pendapatan;

    @Expose
    @SerializedName("trx_tunai")
    private String trxTunai;

    @Expose
    @SerializedName("trx_qris")
    private String trxQris;

    @Expose
    @SerializedName("menu_terjual")
    private String menuTerjual;

    @Expose
    @SerializedName("menu_terlaris")
    private String menuTerlaris;

    @Expose
    @SerializedName("status_subs")
    private boolean statusSubs;

    @Expose
    @SerializedName("status_rekening")
    private boolean statusRekening;

    @Expose
    @SerializedName("data_banner")
    private List<Banner> bannerList;

    @Expose
    @SerializedName("data_article")
    private List<Artikel> artikelList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getQris() {
        return qris;
    }

    public void setQris(String qris) {
        this.qris = qris;
    }

    public String getTotaltrx() {
        return totaltrx;
    }

    public void setTotaltrx(String totaltrx) {
        this.totaltrx = totaltrx;
    }

    public String getPendapatan() {
        return pendapatan;
    }

    public void setPendapatan(String pendapatan) {
        this.pendapatan = pendapatan;
    }

    public String getTrxTunai() {
        return trxTunai;
    }

    public void setTrxTunai(String trxTunai) {
        this.trxTunai = trxTunai;
    }

    public String getTrxQris() {
        return trxQris;
    }

    public void setTrxQris(String trxQris) {
        this.trxQris = trxQris;
    }

    public String getMenuTerjual() {
        return menuTerjual;
    }

    public void setMenuTerjual(String menuTerjual) {
        this.menuTerjual = menuTerjual;
    }

    public String getMenuTerlaris() {
        return menuTerlaris;
    }

    public void setMenuTerlaris(String menuTerlaris) {
        this.menuTerlaris = menuTerlaris;
    }

    public boolean isStatusSubs() {
        return statusSubs;
    }

    public void setStatusSubs(boolean statusSubs) {
        this.statusSubs = statusSubs;
    }

    public boolean isStatusRekening() {
        return statusRekening;
    }

    public void setStatusRekening(boolean statusRekening) {
        this.statusRekening = statusRekening;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    public List<Artikel> getArtikelList() {
        return artikelList;
    }

    public void setArtikelList(List<Artikel> artikelList) {
        this.artikelList = artikelList;
    }
}
