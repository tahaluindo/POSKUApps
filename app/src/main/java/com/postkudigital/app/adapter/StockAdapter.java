package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.product.stok.DetailStockActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Stock;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.VH> implements Filterable {
    private Context context;
    private List<Stock> stockList;
    private List<Stock> filteredList;

    public StockAdapter(Context context, List<Stock> stockList){
        this.context = context;
        this.stockList = stockList;
        this.filteredList = stockList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = stockList;
                }else {
                    List<Stock> list = new ArrayList<>();
                    for(Stock row : stockList){
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
                filteredList = (ArrayList<Stock>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Stock stock = filteredList.get(position);
        holder.label.setText(stock.getNama().toString());
        holder.qty.setText(stock.getCurrentStock() + "");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailStockActivity.class);
                intent.putExtra(Constants.ID, stock.getId());
                intent.putExtra(Constants.NAMA, stock.getNama());
                intent.putExtra(Constants.METHOD, stock.getCurrentStock());
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
        private TextView label, qty;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            label = view.findViewById(R.id.text_nama);
            qty = view.findViewById(R.id.text_quantity);

        }
    }
}
