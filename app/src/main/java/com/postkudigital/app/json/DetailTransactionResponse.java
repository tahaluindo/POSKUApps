package com.postkudigital.app.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.postkudigital.app.models.Cart;
import com.postkudigital.app.models.Customer;
import com.postkudigital.app.models.ItemCart;
import com.postkudigital.app.models.LabelOrder;
import com.postkudigital.app.models.Meja;
import com.postkudigital.app.models.Pajak;
import com.postkudigital.app.models.ServiceFee;
import com.postkudigital.app.models.TipeOrder;
import com.postkudigital.app.models.Toko;
import com.postkudigital.app.models.Transaction;
import com.postkudigital.app.models.User;

import java.util.List;

public class DetailTransactionResponse {
    @Expose
    @SerializedName("msg")
    private String message;

    @Expose
    @SerializedName("status_code")
    private int statusCode;

    @Expose
    @SerializedName("data_transaksi")
    private Transaction transaction;

    @Expose
    @SerializedName("data_cart")
    private Cart cart;

    @Expose
    @SerializedName("data_cart_items")
    private List<ItemCart> itemCarts;

    @Expose
    @SerializedName("data_pegawai")
    private User user;

    @Expose
    @SerializedName("data_toko")
    private Toko toko;

    @Expose
    @SerializedName("data_pajak")
    private Pajak pajak;

    @Expose
    @SerializedName("data_service_fee")
    private List<ServiceFee> serviceFeeList;

    @Expose
    @SerializedName("data_tipe_order")
    private TipeOrder tipeOrder;

    @Expose
    @SerializedName("data_label_order")
    private LabelOrder labelOrder;

    @Expose
    @SerializedName("data_table")
    private Meja meja;

    @Expose
    @SerializedName("data_pelanggan")
    private Customer customer;

    public Pajak getPajak() {
        return pajak;
    }

    public void setPajak(Pajak pajak) {
        this.pajak = pajak;
    }

    public List<ServiceFee> getServiceFeeList() {
        return serviceFeeList;
    }

    public void setServiceFeeList(List<ServiceFee> serviceFeeList) {
        this.serviceFeeList = serviceFeeList;
    }

    public TipeOrder getTipeOrder() {
        return tipeOrder;
    }

    public void setTipeOrder(TipeOrder tipeOrder) {
        this.tipeOrder = tipeOrder;
    }

    public LabelOrder getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(LabelOrder labelOrder) {
        this.labelOrder = labelOrder;
    }

    public Meja getMeja() {
        return meja;
    }

    public void setMeja(Meja meja) {
        this.meja = meja;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<ItemCart> getItemCarts() {
        return itemCarts;
    }

    public void setItemCarts(List<ItemCart> itemCarts) {
        this.itemCarts = itemCarts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Toko getToko() {
        return toko;
    }

    public void setToko(Toko toko) {
        this.toko = toko;
    }
}
