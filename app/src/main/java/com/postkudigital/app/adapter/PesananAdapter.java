package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.pos.DetailOrderActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.Cart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.VH> {
    private Context context;
    private List<Cart> cartList;


    public PesananAdapter(Context context, List<Cart> cartList){
        this.context = context;
        this.cartList = cartList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanan, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Cart cart = cartList.get(position);
        if(cart.getNama() != null){
            holder.invoice.setText(cart.getNama().toString());
        }else {
            holder.invoice.setText(cart.getCode().toString());
        }
        double total = Math.round(cart.getGrandTotal());
        holder.total.setText(DHelper.formatRupiah(total));
        holder.tanggal.setText(DHelper.strTodatetime(cart.getCreatedAt()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.ID, cart.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView invoice, total, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            invoice = view.findViewById(R.id.text_invoice);
            total = view.findViewById(R.id.text_total);
            tanggal = view.findViewById(R.id.text_tanggal);
        }
    }
}
