package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.User;

public class RegisterResponseJson {
    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("status_code")
    private String statusCode;

    @Expose
    @SerializedName("token")
    private String token;

    @Expose
    @SerializedName("user")
    private User user;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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
}
