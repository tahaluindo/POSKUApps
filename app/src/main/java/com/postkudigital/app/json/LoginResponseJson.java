package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.User;

public class LoginResponseJson {
    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("token")
    private String token;

    @Expose
    @SerializedName("data_akun")
    private User user;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
