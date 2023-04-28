package com.postkudigital.app.actvity.ppob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CalbackPpobResponse;
import com.postkudigital.app.json.GetDetailTransPpobResponse;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultTransaksiActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private Toolbar toolbar;
    private TextView invoice, custNumber, kodeProduk,namaProduk, catProduk, brandProduk, tanggal,
            harga, status, btnCheck, statusDesc;
    private String reffId;
    private LinearLayout lcontent;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipe;
    private boolean isRefund = true;
    private int wallet=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_transaksi);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        toolbar = findViewById(R.id.toolbar2);
        invoice = findViewById(R.id.text_invoice);
        custNumber = findViewById(R.id.text_customer_nomor);
        kodeProduk = findViewById(R.id.text_kode_produk);
        namaProduk = findViewById(R.id.text_nama_produk);
        catProduk = findViewById(R.id.text_kategori_produk);
        brandProduk = findViewById(R.id.text_brand);
        tanggal = findViewById(R.id.text_tanggal);
        harga = findViewById(R.id.text_harga);
        status = findViewById(R.id.textView70);
        btnCheck = findViewById(R.id.textView71);
        lcontent = findViewById(R.id.lcontent);
        progressBar = findViewById(R.id.progressBar);
        statusDesc = findViewById(R.id.textView28);
        swipe = findViewById(R.id.swipe);

        setSupportActionBar(toolbar);

        reffId = getIntent().getStringExtra(Constants.ID);
        detail(reffId);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRefund){
                    refund(reffId, String.valueOf(wallet));
                }else {
                    callback(reffId);
                }
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detail(reffId);
                swipe.setRefreshing(false);
            }
        });
    }

    private void detail(String inv){
        progressBar.setVisibility(View.VISIBLE);
        lcontent.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailTransPpob(inv).enqueue(new Callback<GetDetailTransPpobResponse>() {
            @Override
            public void onResponse(Call<GetDetailTransPpobResponse> call, Response<GetDetailTransPpobResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        lcontent.setVisibility(View.VISIBLE);
                        invoice.setText(response.body().getTransPpob().getReffId());
                        custNumber.setText(response.body().getTransPpob().getCustomerNo());
                        kodeProduk.setText(response.body().getTransPpob().getBuyerSkuCode());
                        namaProduk.setText(response.body().getTransPpob().getProductName());
                        catProduk.setText(response.body().getTransPpob().getCategory());
                        brandProduk.setText(response.body().getTransPpob().getBrand());
                        tanggal.setText(DHelper.strTodatetime(response.body().getTransPpob().getCreatedAt()));
                        harga.setText("Rp" + DHelper.toformatRupiah(String.valueOf(response.body().getTransPpob().getPricePostku())));

                        wallet = response.body().getTransPpob().getWalletId();

                        if(response.body().getTransPpob().getStatus().equalsIgnoreCase("Pending")){
                            isRefund = false;
                            statusDesc.setVisibility(View.GONE);
                            status.setText("Transaksi Berlangsung");
                            status.setTextColor(context.getResources().getColor(R.color.colorBlueSoft));
                            btnCheck.setVisibility(View.VISIBLE);
                            btnCheck.setText("Check Status Transaksi");
                        }else if(response.body().getTransPpob().getStatus().equalsIgnoreCase("Sukses")){
                            isRefund = false;
                            statusDesc.setVisibility(View.GONE);
                            status.setText("Transaksi Berhasil");
                            status.setTextColor(context.getResources().getColor(R.color.colorGreen));
                            btnCheck.setVisibility(View.GONE);
                        }else {
                            if(response.body().getTransPpob().isRefunded()){
                                isRefund = false;
                                statusDesc.setVisibility(View.VISIBLE);
                                status.setText("Dana Sudah dikembalikan");
                                status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                btnCheck.setVisibility(View.GONE);
                            }else {
                                isRefund = true;
                                statusDesc.setVisibility(View.GONE);
                                status.setText("Transaksi Gagal");
                                status.setTextColor(context.getResources().getColor(R.color.colorRed));
                                btnCheck.setVisibility(View.VISIBLE);
                                btnCheck.setText("Refund Dana Wallet");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDetailTransPpobResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void refund(String inv, String walletId){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("ref_id", createPartFromString(inv));
        map.put("wallet_id", createPartFromString(walletId));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.refundPpob(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        if(object.getString("status_code").equalsIgnoreCase("200")){
                            DHelper.pesan(context, object.getString("msg"));
                            detail(inv);
                        }else {
                            DHelper.pesan(context, object.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void callback(String inv){
        progressBar.setVisibility(View.VISIBLE);
        lcontent.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.callback(inv).enqueue(new Callback<CalbackPpobResponse>() {
            @Override
            public void onResponse(Call<CalbackPpobResponse> call, Response<CalbackPpobResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        lcontent.setVisibility(View.VISIBLE);
                        detail(inv);
                    }
                }
            }

            @Override
            public void onFailure(Call<CalbackPpobResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

}