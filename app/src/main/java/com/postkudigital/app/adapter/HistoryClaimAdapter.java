package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.actvity.qris.DetailClaimActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.ClaimSaldo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryClaimAdapter extends RecyclerView.Adapter<HistoryClaimAdapter.VH> {
    private Context context;
    private List<ClaimSaldo> claimSaldoList;

    public HistoryClaimAdapter(Context context, List<ClaimSaldo> claimSaldoList){
        this.context = context;
        this.claimSaldoList = claimSaldoList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_claim, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final ClaimSaldo claimSaldo = claimSaldoList.get(position);
        holder.invoice.setText(claimSaldo.getName());
        holder.nominal.setText("Rp" + DHelper.toformatRupiah(String.valueOf(claimSaldo.getTotal())));
        holder.tanggal.setText(DHelper.strTodatetime(claimSaldo.getCreatedAt()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailClaimActivity.class);
                intent.putExtra(Constants.ID, claimSaldo.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return claimSaldoList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView invoice, nominal, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            invoice = view.findViewById(R.id.text_invoice);
            nominal = view.findViewById(R.id.text_total);
            tanggal = view.findViewById(R.id.text_total2);
        }
    }
}
