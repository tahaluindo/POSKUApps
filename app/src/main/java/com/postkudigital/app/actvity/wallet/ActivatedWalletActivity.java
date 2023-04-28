package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.actvity.MainActivity;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivatedWalletActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption;
    private Button submit;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activated_wallet);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        submit = findViewById(R.id.btn_activated);
        progressBar = findViewById(R.id.progressBar5);

        caption.setText("Wallet");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activated();
            }
        });
    }

    private void activated(){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.activeWallet(sessionManager.getIdToko()).enqueue(new Callback<WalletResponseJson>() {
            @Override
            public void onResponse(Call<WalletResponseJson> call, Response<WalletResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200) {
                        DHelper.pesan(context, response.body().getMessage());
                        Intent intent = new Intent(context, MainActivity.class);
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
            public void onFailure(Call<WalletResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}