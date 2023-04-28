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

import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.riwayat.DetailTransaksiActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryTransAdapter extends RecyclerView.Adapter<HistoryTransAdapter.VH> implements Filterable {
    private Context context;
    private List<Transaction> transactionList;
    private List<Transaction> filteredList;

    public HistoryTransAdapter(Context context, List<Transaction> transactionList){
        this.context = context;
        this.transactionList = transactionList;
        this.filteredList = transactionList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = transactionList;
                }else {
                    List<Transaction> list = new ArrayList<>();
                    for(Transaction row : transactionList){
                        if(row.getReffCode().toLowerCase().contains(nama.toLowerCase())){
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
                filteredList = (ArrayList<Transaction>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Transaction trx = filteredList.get(position);
        holder.invoice.setText(trx.getReffCode());
        holder.tanggal.setText(DHelper.strTodatetime(trx.getCreatedAt()));
        holder.total.setText(DHelper.formatRupiah(trx.getGrandTotal()));
        if(trx.getPaymentType() == 1){
            holder.payment.setText("Tunai");
            holder.payment.setTextColor(context.getResources().getColor(R.color.colorGreen));
//            holder.payment.setBackground(context.getResources().getDrawable(R.drawable.button_outline_green));
        }else {
            holder.payment.setText("QRIS");
            holder.payment.setTextColor(context.getResources().getColor(R.color.colorAccent));
//            holder.payment.setBackground(context.getResources().getDrawable(R.drawable.custom_button_outline));
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTransaksiActivity.class);
                intent.putExtra(Constants.ID, trx.getReffCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RelativeLayout item;
        private TextView invoice, payment, total, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            invoice = view.findViewById(R.id.text_invoice);
            payment = view.findViewById(R.id.text_payment);
            total = view.findViewById(R.id.text_total);
            tanggal = view.findViewById(R.id.text_tanggal);

        }
    }
}
