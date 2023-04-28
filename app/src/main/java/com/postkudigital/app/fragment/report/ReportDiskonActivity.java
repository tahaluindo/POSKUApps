package com.postkudigital.app.fragment.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.postkudigital.app.R;

public class ReportDiskonActivity extends AppCompatActivity {
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_diskon);
        context = this;
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabDiskonAdapter adapter = new TabDiskonAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}