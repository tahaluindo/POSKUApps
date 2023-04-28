package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.HistoryWalletAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetHistoryWalletResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class RiwayatPayActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption;
    private RecyclerView recyclerView;
    private LinearLayout lempty;
    private ProgressBar progressBar;
    private HistoryWalletAdapter adapter;
    private SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pay);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        recyclerView = findViewById(R.id.rec_riwayat);
        lempty = findViewById(R.id.lempty);
        progressBar = findViewById(R.id.progressBar);
        swipe = findViewById(R.id.swipe);

        caption.setText("Riwayat Postkupay");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        Log.e("WALLET", "walll:" + getIntent().getIntExtra(Constants.ID, 0));
        getData(getIntent().getIntExtra(Constants.ID, 0));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(getIntent().getIntExtra(Constants.ID, 0));
                swipe.setRefreshing(false);
            }
        });
    }

    private void getData(int walletId) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lempty.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.historyWallet(String.valueOf(walletId)).enqueue(new Callback<GetHistoryWalletResponse>() {
            @Override
            public void onResponse(Call<GetHistoryWalletResponse> call, Response<GetHistoryWalletResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getHistoryWalletList().isEmpty()){
                            lempty.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new HistoryWalletAdapter(context, response.body().getHistoryWalletList(), RiwayatPayActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetHistoryWalletResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}