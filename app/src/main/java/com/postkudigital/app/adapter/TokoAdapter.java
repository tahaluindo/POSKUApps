package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.fragment.toko.DetailTokoActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Toko;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TokoAdapter extends RecyclerView.Adapter<TokoAdapter.VH> implements Filterable {
    private Context context;
    private List<Toko> tokoList;
    private List<Toko> filteredList;

    public TokoAdapter(Context context, List<Toko> tokoList){
        this.context = context;
        this.tokoList = tokoList;
        this.filteredList = tokoList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = tokoList;
                }else {
                    List<Toko> list = new ArrayList<>();
                    for(Toko row : tokoList){
                        if(row.getNama().toLowerCase().contains(nama.toLowerCase())){
                            list.add(row);
                        }
                    }
                    filteredList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Toko>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toko, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        final Toko toko = filteredList.get(position);
        Glide.with(context)
                .load(toko.getLogo())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        holder.nama.setText(toko.getNama().toString());
        holder.alamat.setText(toko.getKelDes() + ", " + toko.getKecamatan()
                + ", " + toko.getKabKota() + "," + toko.getProvinsi());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTokoActivity.class);
                intent.putExtra(Constants.ID, toko.getId());
                intent.putExtra(Constants.METHOD, Constants.EDIT);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private ImageView imageView;
        private TextView nama, alamat;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            imageView = view.findViewById(R.id.img_toko);
            nama = view.findViewById(R.id.text_nama);
            alamat = view.findViewById(R.id.text_alamat);
        }
    }
}
