package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.TopupResponseJson;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.models.Wallet;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.NetworkUtils;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUpSaldoActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView text20, text50, text100;
    private Button submit;
    private EditText edtNominal;
    private boolean isSelected;
    private ImageView backButton;
    private TextView caption;
    private LinearLayout ltopup, lpending, lnominal, lbottom;
    private ProgressBar progressBar;
    private int id, nominal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_saldo);
        context = this;
        sessionManager = new SessionManager(context);
        text20 = findViewById(R.id.text_20);
        text50 = findViewById(R.id.text_50);
        text100  = findViewById(R.id.text_100);
        submit = findViewById(R.id.btn_activated);
        edtNominal = findViewById(R.id.edt_search2);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        ltopup = findViewById(R.id.ltopup);
        lbottom = findViewById(R.id.bottom);
        progressBar = findViewById(R.id.progressBar6);
        caption.setText("Topup Saldo");

        id = getIntent().getIntExtra(Constants.ID, 0);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text20.setSelected(false);
        text50.setSelected(false);
        text100.setSelected(false);

        text20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text20.setSelected(true);
                text50.setSelected(false);
                text100.setSelected(false);
                text20.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green));
                text20.setTextColor(context.getResources().getColor(R.color.colorGreen));

                text50.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
                text100.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
                text50.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
                text100.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
                edtNominal.setText("20000");
            }
        });

        text50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text20.setSelected(false);
                text50.setSelected(true);
                text100.setSelected(false);
                text50.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green));
                text50.setTextColor(context.getResources().getColor(R.color.colorGreen));
                text20.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
                text100.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
                text20.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
                text100.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
                edtNominal.setText("50000");
            }
        });

        text100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text20.setSelected(false);
                text50.setSelected(false);
                text100.setSelected(true);
                text100.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green));
                text100.setTextColor(context.getResources().getColor(R.color.colorGreen));
                text20.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
                text50.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
                text20.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
                text50.setTextColor(context.getResources().getColor(android.R.color.secondary_text_light));
                edtNominal.setText("100000");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNominal.getText().toString().isEmpty()){
                    edtNominal.setError(context.getString(R.string.error_empty));
                    edtNominal.requestFocus();
                    return;
                }else if(Integer.parseInt(edtNominal.getText().toString()) < 10000){
                    edtNominal.setError("Minimal deposit Rp10.000");
                    edtNominal.requestFocus();
                    return;
                }

                if(NetworkUtils.isConnected(context)){
                    topup(edtNominal.getText().toString());
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
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

    private void topup(String nominal){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.topup(String.valueOf(id), nominal).enqueue(new Callback<TopupResponseJson>() {
            @Override
            public void onResponse(Call<TopupResponseJson> call, Response<TopupResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMessage());
                        Intent intent = new Intent(context, KonfirmasiTopupActivity.class);
                        intent.putExtra(Constants.NOMINAL, response.body().getData());
                        intent.putExtra(Constants.ID, id);
                        startActivity(intent);
                        finish();
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<TopupResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                DHelper.pesan(context, t.getMessage());
            }
        });
    }

}