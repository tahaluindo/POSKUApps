package com.postkudigital.app.fragment.pos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.PesananAdapter;
import com.postkudigital.app.json.GetCartResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout lempty;
    private PesananAdapter adapter;
    private TextView caption;
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);
        context = this;
        sessionManager = new SessionManager(context);
        recyclerView = findViewById(R.id.rec_pesanan);
        progressBar = findViewById(R.id.progressBar);
        lempty = findViewById(R.id.layout_empty);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);

        caption.setText("Pesanan tersimpan");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        getdata();

    }

    private void getdata(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lempty.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getCart(sessionManager.getIdToko()).enqueue(new Callback<GetCartResponse>() {
            @Override
            public void onResponse(Call<GetCartResponse> call, Response<GetCartResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getCartList().size() > 0){
                            if(response.body().getCartList().size() > 0){
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter = new PesananAdapter(context, response.body().getCartList());
                                recyclerView.setAdapter(adapter);
                            }else {
                                lempty.setVisibility(View.VISIBLE);
                            }

                        }else {
                            lempty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCartResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}