package com.postkudigital.app.actvity.qris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.postkudigital.app.R;
import com.postkudigital.app.utils.SessionManager;

public class HistoryClaimActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_claim);
        context = this;
        sessionManager = new SessionManager(context);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TabClaimAdapter adapter = new TabClaimAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}