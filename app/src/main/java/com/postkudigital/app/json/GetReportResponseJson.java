package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetReportResponseJson {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int code;

    @Expose
    @SerializedName("data_penjualan_kotor")
    private double dataPenjualanKotor;

    @Expose
    @SerializedName("data_pajak")
    private double dataPajak;

    @Expose
    @SerializedName("data_service_fee")
    private double dataServiceFee;

    @Expose
    @SerializedName("data_disc")
    private double dataDiscount;

    @Expose
    @SerializedName("data_hpp")
    private double dataHpp;

    @Expose
    @SerializedName("data_total_item")
    private int dataTotalItem;

    @Expose
    @SerializedName("data_cancel_trx")
    private int dataCancelTrx;

    @Expose
    @SerializedName("data_laba_rugi")
    private double dataLabaRugi;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getDataPenjualanKotor() {
        return dataPenjualanKotor;
    }

    public void setDataPenjualanKotor(double dataPenjualanKotor) {
        this.dataPenjualanKotor = dataPenjualanKotor;
    }

    public double getDataPajak() {
        return dataPajak;
    }

    public void setDataPajak(double dataPajak) {
        this.dataPajak = dataPajak;
    }

    public double getDataDiscount() {
        return dataDiscount;
    }

    public void setDataDiscount(double dataDiscount) {
        this.dataDiscount = dataDiscount;
    }

    public double getDataHpp() {
        return dataHpp;
    }

    public void setDataHpp(double dataHpp) {
        this.dataHpp = dataHpp;
    }

    public int getDataTotalItem() {
        return dataTotalItem;
    }

    public void setDataTotalItem(int dataTotalItem) {
        this.dataTotalItem = dataTotalItem;
    }

    public int getDataCancelTrx() {
        return dataCancelTrx;
    }

    public void setDataCancelTrx(int dataCancelTrx) {
        this.dataCancelTrx = dataCancelTrx;
    }

    public double getDataLabaRugi() {
        return dataLabaRugi;
    }

    public void setDataLabaRugi(double dataLabaRugi) {
        this.dataLabaRugi = dataLabaRugi;
    }

    public double getDataServiceFee() {
        return dataServiceFee;
    }

    public void setDataServiceFee(double dataServiceFee) {
        this.dataServiceFee = dataServiceFee;
    }
}
