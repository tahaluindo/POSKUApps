package com.postkudigital.app.fragment.pos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Kategori;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryTabAdapter extends FragmentStatePagerAdapter {
    private List<Kategori> categories;
    int mNumOfTabs;

    public CategoryTabAdapter(FragmentManager fm, List<Kategori>categoryList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.categories = categoryList;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        ListProdukFragment fragment = new ListProdukFragment();
        Kategori k = categories.get(position);
        Bundle p = new Bundle();
        p.putInt(Constants.ID_KATEGORI, k.getId());
        p.putString(Constants.NAMA, k.getLabel());
        fragment.setArguments(p);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}
