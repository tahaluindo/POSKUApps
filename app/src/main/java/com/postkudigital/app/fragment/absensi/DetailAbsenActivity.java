package com.postkudigital.app.fragment.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetDetailAbseResponse;
import com.postkudigital.app.models.Absensi;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAbsenActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton, imgUser, imgMasuk, imgPulang;
    private TextView nama, email, phone, time1, time2, caption;
    private LinearLayout main;
    private ProgressBar progressBar;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_absen);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        imgUser = findViewById(R.id.img_user);
        imgMasuk = findViewById(R.id.img_masuk);
        imgPulang = findViewById(R.id.img_pulang);
        nama = findViewById(R.id.text_nama);
        email = findViewById(R.id.text_email);
        phone = findViewById(R.id.text_phone);
        time1 = findViewById(R.id.text_absen_masuk);
        time2 = findViewById(R.id.text_absen_pulang);
        caption = findViewById(R.id.text_caption);
        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.main);

        id = getIntent().getIntExtra(Constants.ID, 0);

        caption.setText("Detail absen");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

    }
    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailAbsen(String.valueOf(id)).enqueue(new Callback<GetDetailAbseResponse>() {
            @Override
            public void onResponse(Call<GetDetailAbseResponse> call, @NotNull Response<GetDetailAbseResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                   if(response.body().getStatusCode() == 200){
                       main.setVisibility(View.VISIBLE);
                       final Absensi absensi = response.body().getAbsensi();
                       nama.setText(absensi.getNama());
                       email.setText(absensi.getEmail());
                       phone.setText(absensi.getPhone());
                       Glide.with(context)
                               .load(absensi.getPic1())
                               .placeholder(R.drawable.image_placeholder)
                               .into(imgMasuk);
                       Glide.with(context)
                               .load(absensi.getPic2())
                               .placeholder(R.drawable.image_placeholder)
                               .into(imgPulang);
                       time1.setText(DHelper.strTodatetime(absensi.getTime1()));
                       if(absensi.getTime2() != null){
                           time2.setText(DHelper.strTodatetime(absensi.getTime2()));
                       }else {
                           time2.setText("-");
                       }

                   }else {
                       DHelper.pesan(context, response.body().getMessage());
                   }
                }
            }

            @Override
            public void onFailure(Call<GetDetailAbseResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}