package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.ppob.TransPpobActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Brand;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.VH> {
    private Context context;
    private List<Brand> brandList;
    private String category;

    public BrandAdapter(Context context, List<Brand> brandList, String category){
        this.context = context;
        this.brandList = brandList;
        this.category = category;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ppob_product, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Brand brand = brandList.get(position);
        Glide.with(context)
                .load(brand.getImage())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.logo);
        holder.nama.setText(brand.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransPpobActivity.class);
                intent.putExtra(Constants.NAMA, brand.getKey());
                intent.putExtra(Constants.METHOD, category);
                intent.putExtra(Constants.ADD, brand.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }
    public class VH extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private TextView nama;
        private ImageView logo;
        public VH(View itemView){
            super(itemView);
            item = itemView.findViewById(R.id.item);
            nama = itemView.findViewById(R.id.text_nama);
            logo = itemView.findViewById(R.id.img_logo);
        }
    }
}
