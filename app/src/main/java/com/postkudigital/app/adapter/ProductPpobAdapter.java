package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.actvity.ppob.TransPpobActivity;
import com.postkudigital.app.models.ProductPpob;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductPpobAdapter extends RecyclerView.Adapter<ProductPpobAdapter.VH> {
    private Context context;
    private List<ProductPpob> productPpobList;
    private TransPpobActivity activity;
    private int lastSelectedPosition = 0;

    public ProductPpobAdapter(Context context, List<ProductPpob> productPpobList, TransPpobActivity activity){
        this.context = context;
        this.productPpobList = productPpobList;
        this.activity = activity;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pulsa, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final ProductPpob ppob = productPpobList.get(position);
        holder.nama.setText(ppob.getName());
        holder.check.setChecked(lastSelectedPosition == position);
        holder.check.setTag(position);
        holder.item.setTag(position);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChange(v);
                activity.showDetailProduct(ppob);
            }
        });

        if(holder.check.isChecked()){
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_border));
            holder.nama.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        }else {
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
            holder.nama.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }
    }

    @Override
    public int getItemCount() {
        return productPpobList.size();
    }

    private void itemCheckChange(View view) {
        lastSelectedPosition =(Integer)view.getTag();
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama;
        private RadioButton check;
        public VH(View itemView){
            super(itemView);
            item = itemView.findViewById(R.id.item);
            nama = itemView.findViewById(R.id.text_nama);
            check = itemView.findViewById(R.id.check);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check.setChecked(lastSelectedPosition == getAdapterPosition());
                }
            });
        }
    }
}
