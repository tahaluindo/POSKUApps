package com.postkudigital.app.actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetDetailArtikelResponse;
import com.postkudigital.app.json.GetDetailBannerResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailWebActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView title, tanggal, description, caption;
    private ImageView backButton, img;
    private LinearLayout main;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        context = this;
        sessionManager = new SessionManager(context);
        title = findViewById(R.id.text_title);
        tanggal = findViewById(R.id.text_title2);
        description = findViewById(R.id.text_title3);
        caption = findViewById(R.id.text_caption);
        backButton = findViewById(R.id.back_button);
        img = findViewById(R.id.imageView18);
        main = findViewById(R.id.main);
        progressBar = findViewById(R.id.progressBar);

        caption.setText(getIntent().getStringExtra(Constants.METHOD));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ARTICLE)){
            detailArtikel(getIntent().getIntExtra(Constants.ID, 0));
        }else{
            detailbanner(getIntent().getIntExtra(Constants.ID, 0));
        }

    }
    private void detailArtikel(int id){
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailArtikel(String.valueOf(id)).enqueue(new Callback<GetDetailArtikelResponse>() {
            @Override
            public void onResponse(Call<GetDetailArtikelResponse> call, Response<GetDetailArtikelResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        main.setVisibility(View.VISIBLE);
                        title.setText(response.body().getArtikel().getTitle());
                        tanggal.setText(DHelper.strTodatetime(response.body().getArtikel().getCreatedAt()));
                        Glide.with(context)
                                .load(response.body().getArtikel().getImage())
                                .placeholder(R.drawable.image_placeholder)
                                .into(img);
                        description.setText(Html.fromHtml(response.body().getArtikel().getBody()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDetailArtikelResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void detailbanner(int id){
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailbanner(String.valueOf(id)).enqueue(new Callback<GetDetailBannerResponse>() {
            @Override
            public void onResponse(Call<GetDetailBannerResponse> call, Response<GetDetailBannerResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        main.setVisibility(View.VISIBLE);
                        title.setText(response.body().getBanner().getTitle());
                        tanggal.setText(DHelper.strTodatetime(response.body().getBanner().getCreatedAt()));
                        Glide.with(context)
                                .load(response.body().getBanner().getImage())
                                .placeholder(R.drawable.image_placeholder)
                                .into(img);
                        description.setText(Html.fromHtml(response.body().getBanner().getBody()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDetailBannerResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}