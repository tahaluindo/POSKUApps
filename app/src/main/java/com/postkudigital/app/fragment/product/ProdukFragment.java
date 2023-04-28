package com.postkudigital.app.fragment.product;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.postkudigital.app.R;

public class ProdukFragment extends Fragment {
    private Context c;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabLabel = {
            R.string.title_tab_menu,
            R.string.title_tab_kategori,
            R.string.title_tab_stock
    };
    public ProdukFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produk, container, false);
        c = getActivity();
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

}