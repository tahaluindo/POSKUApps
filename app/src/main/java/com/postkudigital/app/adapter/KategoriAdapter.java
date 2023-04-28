package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.product.kategori.KategoriFragment;
import com.postkudigital.app.models.Kategori;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.VH> implements Filterable {
    private Context context;
    private List<Kategori> kategoriList;
    private List<Kategori> filteredList;
    private KategoriFragment fragment;

    public KategoriAdapter(Context context, List<Kategori> kategoriList, KategoriFragment fragment){
        this.context = context;
        this.kategoriList = kategoriList;
        this.filteredList = kategoriList;
        this.fragment = fragment;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = kategoriList;
                }else {
                    List<Kategori> list = new ArrayList<>();
                    for(Kategori row : kategoriList){
                        if(row.getLabel().toLowerCase().contains(nama.toLowerCase())){
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
                filteredList = (ArrayList<Kategori>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder( @NotNull VH holder, int position) {
        final Kategori kategori = filteredList.get(position);
        holder.label.setText(kategori.getLabel());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragment.showDialog(true, String.valueOf(kategori.getId()), kategori.getLabel());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView label;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            label = view.findViewById(R.id.text_nama);
        }
    }


}
