package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.DetailWebActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Banner;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private List<Banner> modelList;
    private LayoutInflater layoutInflater;
    private Context context;

    public BannerAdapter(Context context, List<Banner> modelList){
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_banner, container, false);

        ImageView imageView;
        RelativeLayout slider;

        imageView = view.findViewById(R.id.image);
        slider = view.findViewById(R.id.slider);

        final Banner bannerModel = modelList.get(position);
        Glide.with(context)
                .load(bannerModel.getImage())
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailWebActivity.class);
                intent.putExtra(Constants.ID, bannerModel.getId());
                intent.putExtra(Constants.METHOD, Constants.BANNER);
                context.startActivity(intent);
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
