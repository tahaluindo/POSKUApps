package com.postkudigital.app.fragment.report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.User;
import com.postkudigital.app.utils.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportRankingActivity extends AppCompatActivity {
    private Context context;
    private User user;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption;
    private CircleImageView circleImageView;
    private RelativeLayout lapMenu, lapStaff, lapKategori,
            lapDiskon, lapMeja, lapCustomer, lapTipe, lapLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_ranking);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        circleImageView = findViewById(R.id.userphoto);
        lapMenu = findViewById(R.id.rlmenu);
        lapStaff = findViewById(R.id.rlpegawai);
        lapKategori = findViewById(R.id.rlkategori);
        lapDiskon = findViewById(R.id.rldiscount);
        lapMeja = findViewById(R.id.rlmeja);
        lapCustomer = findViewById(R.id.rlcustomer);
        lapTipe = findViewById(R.id.rltipe);
        lapLabel = findViewById(R.id.rllabel);

        caption.setText("Ranking Reports");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.image_placeholder)
                .into(circleImageView);

        lapMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_MENU);
                startActivity(intent);
            }
        });

        lapStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_STAFF);
                startActivity(intent);
            }
        });

        lapKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_KATEGORI);
                startActivity(intent);
            }
        });

        lapDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReportDiskonActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_DISCOUNT);
                startActivity(intent);
            }
        });

        lapMeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_MEJA);
                startActivity(intent);
            }
        });

        lapCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_PELANGGAN);
                startActivity(intent);
            }
        });

        lapTipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_TIPE_ORDER);
                startActivity(intent);
            }
        });

        lapLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankingActivity.class);
                intent.putExtra(Constants.METHOD, Constants.LAP_LABEL_ORDER);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}