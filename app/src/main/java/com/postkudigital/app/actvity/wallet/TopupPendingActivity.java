package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.models.Wallet;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopupPendingActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private Button close;
    private ImageView backButton;
    private TextView caption;
    private SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_pending);
        context = this;
        sessionManager = new SessionManager(context);
        close = findViewById(R.id.btn_activated);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        swipe = findViewById(R.id.swipe);
        caption.setText("Topup Saldo");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                            intent.putExtra(Constants.NOMINAL, wallet.getBalanceReq());
                            intent.putExtra(Constants.METHOD, wallet.getStatusReqDepo());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}