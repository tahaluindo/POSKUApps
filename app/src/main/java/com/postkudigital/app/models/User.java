package com.postkudigital.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject implements Serializable {
    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("nama")
    private String nama;

    @Expose
    @SerializedName("phone")
    private String phone;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("is_owner")
    private boolean isOwner;

    @Expose
    @SerializedName("is_subs")
    private boolean isSubs;

    @Expose
    @SerializedName("subs_date")
    private String subsDate;

    @Expose
    @SerializedName("no_rekening")
    private String noRekening;

    @Expose
    @SerializedName("jenis_bank")
    private String jenisBank;

    @Expose
    @SerializedName("profile_pic")
    private String profilePic;

    @Expose
    @SerializedName("rekening_book_pic")
    private String rekeningBookPic;

    @Expose
    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("user")
    private int users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isSubs() {
        return isSubs;
    }

    public void setSubs(boolean subs) {
        isSubs = subs;
    }

    public String getSubsDate() {
        return subsDate;
    }

    public void setSubsDate(String subsDate) {
        this.subsDate = subsDate;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getJenisBank() {
        return jenisBank;
    }

    public void setJenisBank(String jenisBank) {
        this.jenisBank = jenisBank;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRekeningBookPic() {
        return rekeningBookPic;
    }

    public void setRekeningBookPic(String rekeningBookPic) {
        this.rekeningBookPic = rekeningBookPic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
