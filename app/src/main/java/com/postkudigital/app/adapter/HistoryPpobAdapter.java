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
import com.postkudigital.app.actvity.ppob.ResultTransaksiActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.TransPpob;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryPpobAdapter extends RecyclerView.Adapter<HistoryPpobAdapter.VH> implements Filterable {
    private Context context;
    private List<TransPpob> ppobList;
    private List<TransPpob> filteredList;

    public HistoryPpobAdapter(Context context, List<TransPpob> ppobList){
        this.context = context;
        this.ppobList = ppobList;
        this.filteredList = ppobList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaksi_ppob, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final TransPpob ppob = filteredList.get(position);
        holder.invoice.setText(ppob.getReffId());
        holder.tanggal.setText(DHelper.strTodatetime(ppob.getCreatedAt()));
        holder.nominal.setText("Rp" + DHelper.toformatRupiah(String.valueOf(ppob.getPricePostku())));

        if(ppob.getStatus().equalsIgnoreCase("Pending")){
            holder.viewx.setBackground(context.getResources().getDrawable(R.drawable.custom_curve_blue));
        }else if (ppob.getStatus().equalsIgnoreCase("Sukses")){
            holder.viewx.setBackground(context.getResources().getDrawable(R.drawable.custom_curve_rigth_color));
        }else {
            if(ppob.isRefunded()){
                holder.viewx.setBackground(context.getResources().getDrawable(R.drawable.custom_curve_right));
            }else {
                holder.viewx.setBackground(context.getResources().getDrawable(R.drawable.custom_curve_red));
            }
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResultTransaksiActivity.class);
                intent.putExtra(Constants.ID, ppob.getReffId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = ppobList;
                }else {
                    List<TransPpob> list = new ArrayList<>();
                    for(TransPpob row : ppobList){
                        if(row.getReffId().toLowerCase().contains(nama.toLowerCase())){
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
                filteredList = (ArrayList<TransPpob>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView invoice, nominal, tanggal;
        private View viewx;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            invoice = view.findViewById(R.id.text_invoice);
            nominal = view.findViewById(R.id.text_total);
            tanggal = view.findViewById(R.id.text_payment);
            viewx = view.findViewById(R.id.view3);
        }
    }
}
