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
import com.postkudigital.app.fragment.diskon.fee.ServiceFeeFragment;
import com.postkudigital.app.models.ServiceFee;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServiceFeeAdapter extends RecyclerView.Adapter<ServiceFeeAdapter.VH> implements Filterable {
    private Context context;
    private List<ServiceFee> serviceFeeList;
    private List<ServiceFee> filteredList;
    private ServiceFeeFragment fragment;

    public ServiceFeeAdapter(Context context, List<ServiceFee> serviceFeeList, ServiceFeeFragment fragment){
        this.context = context;
        this.serviceFeeList = serviceFeeList;
        this.filteredList = serviceFeeList;
        this.fragment = fragment;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = serviceFeeList;
                }else {
                    List<ServiceFee> list = new ArrayList<>();
                    for(ServiceFee row : serviceFeeList){
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
                filteredList = (ArrayList<ServiceFee>)results.values;
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
        final ServiceFee fee = filteredList.get(position);
        holder.label.setText(fee.getNama());
        holder.type.setVisibility(View.GONE);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showDialog(true, String.valueOf(fee.getId()), fee.getNama(), String.valueOf(fee.getNominal()));
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
