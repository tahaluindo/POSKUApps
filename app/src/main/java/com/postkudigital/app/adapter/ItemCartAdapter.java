package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.helpers.OnCartItemClickListener;
import com.postkudigital.app.models.ItemCart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.VH> {
    private Context context;
    private List<ItemCart> itemCartList;
    private OnCartItemClickListener onItemClickListener;
    private boolean isEditable;

    public ItemCartAdapter(Context context, List<ItemCart> itemCartList, OnCartItemClickListener onItemClickListener, boolean isEditable){
        this.context = context;
        this.itemCartList = itemCartList;
        this.onItemClickListener = onItemClickListener;
        this.isEditable = isEditable;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final ItemCart itemCart = itemCartList.get(position);
        holder.nama.setText(itemCart.getMenuName().getNama());
        holder.harga.setText("Rp" + DHelper.toformatRupiah(String.valueOf(itemCart.getMenuName().getHarga())) + " x " + itemCart.getQty());
        double grandTotal = Math.round(itemCart.getGrandTotalPrice());
        double disc = Math.round(itemCart.getTotalDisc());
        holder.total.setText("Rp" + DHelper.toformatRupiah(String.valueOf(grandTotal)));
        if(disc > 0){
            holder.diskon.setText("-Rp" + DHelper.toformatRupiah(String.valueOf(disc)));
            holder.diskon.setVisibility(View.VISIBLE);
        }else {
            holder.diskon.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(itemCart.getId(), itemCart.getQty(), itemCart.getMenuName().getNama(), itemCart.getMenuName().getHarga(), 2);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(itemCart.getId(), itemCart.getQty(), itemCart.getMenuName().getNama(), itemCart.getMenuName().getHarga(), 1);
            }
        });

        if(isEditable){
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }else {
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return itemCartList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama, harga, total, diskon;
        private ImageView edit, delete;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);
            harga = view.findViewById(R.id.text_price);
            total = view.findViewById(R.id.text_total);
            edit = view.findViewById(R.id.img_edit);
            delete = view.findViewById(R.id.img_delete);
            diskon = view.findViewById(R.id.text_diskon);
        }
    }
}
