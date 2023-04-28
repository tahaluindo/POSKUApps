package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.helpers.OnItemClickListener;
import com.postkudigital.app.models.Menus;
import com.postkudigital.app.utils.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.realm.Realm;

import static com.postkudigital.app.helpers.Constants.TAG;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.VH> {
    private Context context;
    private List<Menus> menusList;
    private OnItemClickListener onItemClickListener;
    private Realm realm;
    public ProdukAdapter(Context context, List<Menus> menusList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.menusList = menusList;
        this.onItemClickListener = onItemClickListener;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        final Menus menus = menusList.get(position);
        realm = BaseApp.getInstance(context).getRealmInstance();
        Glide.with(context)
                .load(menus.getImage())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        holder.nama.setText(menus.getNama());
        holder.harga.setText("Rp" + DHelper.toformatRupiah(String.valueOf(menus.getHarga())));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "idmenu:" + menus.getId());
                onItemClickListener.onItemClick(menus.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menusList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private ImageView imageView;
        private TextView nama, harga;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            imageView = view.findViewById(R.id.img_produk);
            nama = view.findViewById(R.id.text_nama);
            harga = view.findViewById(R.id.text_harga);
        }
    }
}
