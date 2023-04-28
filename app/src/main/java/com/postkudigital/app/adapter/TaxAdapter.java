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
import com.postkudigital.app.fragment.diskon.tax.TaxFragment;
import com.postkudigital.app.models.Pajak;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.VH> implements Filterable {
    private Context context;
    private List<Pajak> pajakList;
    private List<Pajak> filteredList;
    private TaxFragment fragment;

    public TaxAdapter(Context context, List<Pajak> pajakList, TaxFragment fragment){
        this.context = context;
        this.pajakList = pajakList;
        this.filteredList = pajakList;
        this.fragment = fragment;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = pajakList;
                }else {
                    List<Pajak> list = new ArrayList<>();
                    for(Pajak row : pajakList){
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
                filteredList = (ArrayList<Pajak>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Pajak pajak = filteredList.get(position);
        holder.label.setText(pajak.getNama());
        if(pajak.getType() == 1){
            holder.type.setText("Fixed");
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green));
            holder.type.setTextColor(context.getResources().getColor(R.color.colorGreen));

        }else {
            holder.type.setText("Percentage");
            holder.type.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_border));
            holder.type.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showDialog(true, String.valueOf(pajak.getId()), pajak.getNama(), String.valueOf(pajak.getNominal()), pajak.getType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView label, type;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            label = view.findViewById(R.id.text_nama);
            type = view.findViewById(R.id.text_type);
        }
    }
}
