package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.HistoryTopup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryTopupAdapter extends RecyclerView.Adapter<HistoryTopupAdapter.VH> {
    private Context context;
    private List<HistoryTopup> historyTopupList;

    public HistoryTopupAdapter(Context context, List<HistoryTopup> historyTopupList){
        this.context = context;
        this.historyTopupList = historyTopupList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_topup, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final HistoryTopup topup = historyTopupList.get(position);
        holder.nominal.setText(DHelper.toformatRupiah(String.valueOf(topup.getBalance())));
        holder.tanggal.setText(DHelper.strTodatetime(topup.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return historyTopupList.size();
    }
    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nominal, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nominal = view.findViewById(R.id.text_invoice);
            tanggal = view.findViewById(R.id.text_total);
        }
    }
}
