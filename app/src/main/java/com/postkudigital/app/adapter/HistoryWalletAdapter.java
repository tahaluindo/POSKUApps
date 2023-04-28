package com.postkudigital.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.actvity.wallet.RiwayatPayActivity;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.HistoryWallet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryWalletAdapter extends RecyclerView.Adapter<HistoryWalletAdapter.VH> {
    private Context context;
    private List<HistoryWallet> historyWalletList;
    private RiwayatPayActivity activity;

    public HistoryWalletAdapter(Context context, List<HistoryWallet> historyWalletList, RiwayatPayActivity activity){
        this.context = context;
        this.historyWalletList = historyWalletList;
        this.activity = activity;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_pay, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final HistoryWallet wallet = historyWalletList.get(position);
        if(wallet.getType() == 1){
            holder.tipe.setText("Topup");
            holder.nominal.setText("+ Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
        }else if(wallet.getType() == 2){
            holder.tipe.setText("Pembayaran");
            holder.nominal.setText("- Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
        }else {
            holder.tipe.setText("Refund");
            holder.nominal.setText("+ Rp" + DHelper.toformatRupiah(String.valueOf(wallet.getBalance())));
        }
        holder.tanggal.setText(DHelper.strTodatetime(wallet.getCreatedAt()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDetail(wallet);
            }
        });
    }

    public void showDialogDetail(HistoryWallet historyStock){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_detail_stock, null);
        builder.setView(dialogView);

        final TextView mtipe = dialogView.findViewById(R.id.tipe);
        final TextView mqty = dialogView.findViewById(R.id.qty);
        final TextView mnote = dialogView.findViewById(R.id.note);


        if(historyStock.getType() == 1){
            mtipe.setText("Topup");
            mtipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_green));
        }else if(historyStock.getType() == 2){
            mtipe.setText("Pembayaran");
            mtipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_red));
        }else {
            mtipe.setText("Refund");
            mtipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_green));
        }
        mqty.setText(historyStock.getReffId());
        mnote.setText(historyStock.getNote());

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public int getItemCount() {
        return historyWalletList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView tipe, nominal, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            tipe = view.findViewById(R.id.text_invoice);
            nominal = view.findViewById(R.id.text_total);
            tanggal = view.findViewById(R.id.text_tanggal);
        }
    }
}
