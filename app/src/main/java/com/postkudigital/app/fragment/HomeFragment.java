package com.postkudigital.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.ArtikelActivity;
import com.postkudigital.app.actvity.MainActivity;
import com.postkudigital.app.actvity.plus.PostkuPlusActivity;
import com.postkudigital.app.actvity.profil.ProfileActivity;
import com.postkudigital.app.actvity.qris.HomeQrisActivity;
import com.postkudigital.app.actvity.wallet.ActivatedWalletActivity;
import com.postkudigital.app.actvity.wallet.WalletActivity;
import com.postkudigital.app.adapter.BannerAdapter;
import com.postkudigital.app.adapter.NewsAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.HomeResponseJson;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.models.User;
import com.postkudigital.app.models.Wallet;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private Context context;
    private TextView headerText, saldoWallet, saldoQris, totalTrx, totalEarning, totalTrxCash,
    totalTrxQris, totalSold, menuFavorit;
    private ViewPager pager;
    private CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    private LinearLayout lslider, lsaldo, lqris;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 4000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private TextView notFoundNews, readMore, marquetext;
    private SessionManager sessionManager;
    private Button toPos;
    private int saldo = 0;
    private int id;
    private User user;
    private SwipeRefreshLayout swipe;
    private LinearLayout lsubs;
    private Button upgrade;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        lslider = view.findViewById(R.id.lbanner);
        pager = view.findViewById(R.id.pager);
        circleIndicator = view.findViewById(R.id.circle_indicator);
        saldoWallet = view.findViewById(R.id.text_saldo);
        saldoQris = view.findViewById(R.id.text_qris);
        marquetext = view.findViewById(R.id.marqueeText);
        totalTrx = view.findViewById(R.id.text_total_transaksi);
        totalEarning = view.findViewById(R.id.text_pendapatan);
        totalTrxCash = view.findViewById(R.id.text_trx_tunai);
        totalTrxQris = view.findViewById(R.id.text_trx_qris);
        totalSold = view.findViewById(R.id.text_menu_terjual);
        menuFavorit = view.findViewById(R.id.text_menu_terlaris);
        notFoundNews = view.findViewById(R.id.text_not_found);
        readMore = view.findViewById(R.id.text_read_more);
        recyclerView = view.findViewById(R.id.rec_artikel);
        toPos = view.findViewById(R.id.btn_start);
        lsaldo = view.findViewById(R.id.lsaldo);
        lqris = view.findViewById(R.id.lqris);
        swipe = view.findViewById(R.id.swipe);
        lsubs = view.findViewById(R.id.lsubs);
        upgrade = view.findViewById(R.id.button);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHome(sessionManager.getIdToko());
                swipe.setRefreshing(false);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        marquetext.setSelected(true);
        marquetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArtikelActivity.class);
                startActivity(intent);
            }
        });

        toPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constants.METHOD, Constants.RESET);
                startActivity(intent);
            }
        });

        lsaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkStatusDeposit();
            }
        });

        lqris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeQrisActivity.class);
                startActivity(intent);
            }
        });

        if(user.isSubs()){
            lsubs.setVisibility(View.GONE);
        }else {
            lsubs.setVisibility(View.VISIBLE);
        }

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostkuPlusActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHome(sessionManager.getIdToko());
    }

    private void getHome(String idtoko){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.home(idtoko).enqueue(new Callback<HomeResponseJson>() {
            @Override
            public void onResponse(Call<HomeResponseJson> call, Response<HomeResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){

                        if(response.body().getWallet().equalsIgnoreCase("Wallet Belum Aktif")){
                            saldoWallet.setText("Aktifkan Wallet");
                        }else {
                            saldoWallet.setText(DHelper.toformatRupiah(response.body().getWallet()));
                        }

                        if(response.body().getQris() == null){
                            saldoQris.setText("0");
                        }else {
                            saldoQris.setText(DHelper.toformatRupiah(response.body().getQris()));
                        }
                        totalTrx.setText(String.valueOf(response.body().getTotaltrx()));
                        totalEarning.setText(DHelper.toformatRupiah(response.body().getPendapatan()));
                        totalTrxCash.setText(DHelper.toformatRupiah(response.body().getTrxTunai()));
                        totalTrxQris.setText(DHelper.toformatRupiah(response.body().getTrxQris()));
                        totalSold.setText(DHelper.toformatRupiah(response.body().getMenuTerjual()));
                        menuFavorit.setText(response.body().getMenuTerlaris());

                        if(response.body().getBannerList().isEmpty()){
                            Log.e(Constants.TAG, "sini");
                            lslider.setVisibility(View.GONE);
                        }else {
                            Log.e(Constants.TAG, "sinidong");
                            lslider.setVisibility(View.VISIBLE);
                            final int NUM_PAGES = response.body().getBannerList().size();
                            bannerAdapter = new BannerAdapter(context, response.body().getBannerList());
                            pager.setAdapter(bannerAdapter);
                            circleIndicator.setViewPager(pager);
                            pager.setPadding(0,0,0,0);

                            final Handler handler = new Handler();
                            final  Runnable update = new Runnable() {
                                @Override
                                public void run() {
                                    if(currentPage == NUM_PAGES){
                                        currentPage = 0;
                                    }
                                    pager.setCurrentItem(currentPage++, true);
                                }
                            };
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                        }

                        if(response.body().getArtikelList().isEmpty()){
                            notFoundNews.setVisibility(View.VISIBLE);
                            readMore.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            notFoundNews.setVisibility(View.GONE);
                            readMore.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new NewsAdapter(context, response.body().getArtikelList());
                            recyclerView.setAdapter(adapter);

                        }

                        if(response.body().isStatusRekening()){
                            marquetext.setVisibility(View.GONE);
                        }else {
                            marquetext.setVisibility(View.VISIBLE);
                        }

                    }else{
                        DHelper.pesan(context, response.body().getMsg());
                    }

                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<HomeResponseJson> call, Throwable t) {
                t.printStackTrace();
                Log.e(Constants.TAG, t.getMessage());
            }
        });
    }

    private void checkStatusDeposit(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailWallet(sessionManager.getIdToko()).enqueue(new Callback<WalletResponseJson>() {
            @Override
            public void onResponse(Call<WalletResponseJson> call, Response<WalletResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Wallet wallet = response.body().getWallet();
                        Intent intent = new Intent(context, WalletActivity.class);
                        intent.putExtra(Constants.NOMINAL, wallet.getBalance());
                        intent.putExtra(Constants.ID, wallet.getId());
                        startActivity(intent);
                    }else if(response.body().getStatusCode() == 404) {
                        Intent intent = new Intent(context, ActivatedWalletActivity.class);
                        startActivity(intent);
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
}