package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.ppob.PpobKategoriActivity;
import com.postkudigital.app.actvity.ppob.RiwayatPpobActivity;
import com.postkudigital.app.actvity.qris.HomeQrisActivity;
import com.postkudigital.app.adapter.CategoryPpobAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.PpobCategoryResponse;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.models.KategoriPpob;
import com.postkudigital.app.models.User;
import com.postkudigital.app.models.Wallet;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class WalletActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private LinearLayout topup, qris, ppob, pulsa, games, emoney, ewallet, pulsadata, voucher, pln, lainnya, lmenu;
    private RelativeLayout rlriwayatpostku, rlriwayattopup, rlriwayatppob;
    private TextView textSaldo;
    private int walletid;
    private User user;
    private SwipeRefreshLayout swipe;
    private ProgressBar progressBar;
    private CategoryPpobAdapter adapter;
    private RecyclerView recyclerView;
    private List<KategoriPpob> kategoriPpobList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        textSaldo = findViewById(R.id.text_saldo);
        topup = findViewById(R.id.ltopup);
        qris = findViewById(R.id.lqris);
        ppob = findViewById(R.id.lppob);
        pulsa = findViewById(R.id.lpulsa);
        games = findViewById(R.id.lgames);
        emoney = findViewById(R.id.lemoney);
        ewallet = findViewById(R.id.lewallet);
        pulsadata = findViewById(R.id.ldata);
        voucher = findViewById(R.id.lvoucher);
        pln = findViewById(R.id.lpln);
        lainnya = findViewById(R.id.lmenulain);
        rlriwayatpostku = findViewById(R.id.rl_history_postku);
        rlriwayattopup = findViewById(R.id.rl_history_topup);
        rlriwayatppob = findViewById(R.id.rl_history_ppob);
        swipe = findViewById(R.id.swipe);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.rec_ppob);
        lmenu = findViewById(R.id.lmenu);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        walletid = getIntent().getIntExtra(Constants.ID, 0);


        sessionManager.setIdWallet(String.valueOf(walletid));
        sessionManager.setSaldoWallet(String.valueOf(getIntent().getIntExtra(Constants.NOMINAL,0)));
        textSaldo.setText("Rp" + DHelper.toformatRupiah(String.valueOf(getIntent().getIntExtra(Constants.NOMINAL,0))));

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detailwallet();
                swipe.setRefreshing(false);
            }
        });

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isOwner()){
                   checkStatusDeposit();
                }else {
                    DHelper.pesan(context, "Maaf, kamu tidak punya wewenang akses menu ini");
                }

            }
        });

        qris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isOwner()){
                    Intent intent = new Intent(context, HomeQrisActivity.class);
                    startActivity(intent);
                }else {
                    DHelper.pesan(context, "Maaf, kamu tidak punya wewenang akses menu ini");
                }

            }
        });

        ppob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PpobKategoriActivity.class);
                startActivity(intent);
            }
        });

        rlriwayatpostku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiwayatPayActivity.class);
                intent.putExtra(Constants.ID, walletid);
                startActivity(intent);
            }
        });

        rlriwayattopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiwayatTopupActivity.class);
                intent.putExtra(Constants.ID, walletid);
                startActivity(intent);
            }
        });

        rlriwayatppob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RiwayatPpobActivity.class);
                startActivity(intent);
            }
        });

        lainnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PpobKategoriActivity.class);
                startActivity(intent);
            }
        });

        getPpobKategori();

    }



    private void detailwallet(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailWallet(sessionManager.getIdToko()).enqueue(new Callback<WalletResponseJson>() {
            @Override
            public void onResponse(Call<WalletResponseJson> call, Response<WalletResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Wallet wallet = response.body().getWallet();
                        Log.e("BALANCE", String.valueOf(wallet.getBalance()));
                        textSaldo.setText("Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));

                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<WalletResponseJson> call, Throwable t) {

            }
        });
    }

    private void checkStatusDeposit(){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailWallet(sessionManager.getIdToko()).enqueue(new Callback<WalletResponseJson>() {
            @Override
            public void onResponse(Call<WalletResponseJson> call, Response<WalletResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Wallet wallet = response.body().getWallet();

                        if(wallet.getStatusReqDepo() == 1) {
                            Intent intent = new Intent(context, KonfirmasiTopupActivity.class);
                            intent.putExtra(Constants.ID, wallet.getId());
                            intent.putExtra(Constants.NOMINAL, wallet.getBalanceReq());
                            intent.putExtra(Constants.METHOD, wallet.getStatusReqDepo());
                            startActivity(intent);


                        }else if(wallet.getStatusReqDepo() == 2){
                            Intent intent = new Intent(context, TopupPendingActivity.class);
                            startActivity(intent);

                        }else {
                            Intent intent = new Intent(context, TopUpSaldoActivity.class);
                            intent.putExtra(Constants.ID, wallet.getId());
                            startActivity(intent);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<WalletResponseJson> call, Throwable t) {

            }
        });
    }

    private void getPpobKategori() {
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.kategoriPpob().enqueue(new Callback<PpobCategoryResponse>() {
            @Override
            public void onResponse(Call<PpobCategoryResponse> call, Response<PpobCategoryResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getKategoriPpobList().isEmpty()){
                            lmenu.setVisibility(View.GONE);
                        }else {
                            for (int x=0;x < response.body().getKategoriPpobList().size();x++){
                                KategoriPpob kategoriPpob = new KategoriPpob();
                                kategoriPpob.setId(response.body().getKategoriPpobList().get(x).getId());
                                kategoriPpob.setCatName(response.body().getKategoriPpobList().get(x).getCatName());
                                kategoriPpob.setCatImage(response.body().getKategoriPpobList().get(x).getCatImage());
                                kategoriPpob.setCatKey(response.body().getKategoriPpobList().get(x).getCatKey());
                                kategoriPpobList.add(kategoriPpob);
                            }

                            if(response.body().getKategoriPpobList().size() > 7){
                                KategoriPpob kategoriPpob = new KategoriPpob();
                                kategoriPpob.setId(0);
                                kategoriPpob.setCatKey("lainnya");
                                kategoriPpob.setCatImage("");
                                kategoriPpob.setCatName("Lainnya");
                                kategoriPpobList.add(kategoriPpob);
                            }

                            adapter = new CategoryPpobAdapter(context, kategoriPpobList, R.layout.item_ppob_grid);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<PpobCategoryResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}