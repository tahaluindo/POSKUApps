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
import com.postkudigital.app.fragment.meja.TableFragment;
import com.postkudigital.app.models.Meja;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MejaAdapter extends RecyclerView.Adapter<MejaAdapter.VH> implements Filterable {
    private Context context;
    private List<Meja> mejaList;
    private List<Meja> filteredList;
    private TableFragment tableFragment;

    public MejaAdapter(Context context, List<Meja> mejaList, TableFragment fragment){
        this.context = context;
        this.mejaList = mejaList;
        this.filteredList = mejaList;
        this.tableFragment = fragment;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = mejaList;
                }else {
                    List<Meja> list = new ArrayList<>();
                    for(Meja row : mejaList){
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
                filteredList = (ArrayList<Meja>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meja, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Meja meja = filteredList.get(position);
        holder.nama.setText(meja.getNama());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableFragment.showDialog(true, String.valueOf(meja.getId()), meja.getNama(), meja.getNote());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);

        }
    }
}
