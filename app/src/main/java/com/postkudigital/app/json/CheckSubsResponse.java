package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class CheckSubsResponse {
    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("status_subs")
    private boolean statusSub;

    @Expose
    @SerializedName("active_untill")
    private Date activeUntil;

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

    public boolean isStatusSub() {
        return statusSub;
    }

    public void setStatusSub(boolean statusSub) {
        this.statusSub = statusSub;
    }

    public Date getActiveUntil() {
        return activeUntil;
    }

    public void setActiveUntil(Date activeUntil) {
        this.activeUntil = activeUntil;
    }
}
