package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.wallet.topup.TabTopupAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import static com.postkudigital.app.helpers.Constants.TAG;

public class RiwayatTopupActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    public int walletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_topup);
        context = this;
        sessionManager = new SessionManager(context);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        walletId = getIntent().getIntExtra(Constants.ID, 0);

        Log.e(TAG, "walletL" + walletId);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        TabTopupAdapter adapter = new TabTopupAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


//    class TabPagerAdapter extends FragmentStatePagerAdapter  {
//        public TabPagerAdapter(FragmentManager fm){
//            super(fm);
//        }
//
//        @NotNull
//        @Override
//        public Fragment getItem(int position) {
//            Fragment fragment = new TopupProsesFragment();
//            Bundle args = new Bundle();
//            args.putString(Constants.ID, String.valueOf(walletId));
//            args.putInt(Constants.METHOD, position + 1);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            String title;
//            switch (position){
//                case 0:
//                    title = "Proses";
//                    break;
//                case 1:
//                    title = "Sukses";
//                    break;
//                case 2:
//                    title = "Batal";
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + position);
//            }
//            return title;
//        }
//    }
}