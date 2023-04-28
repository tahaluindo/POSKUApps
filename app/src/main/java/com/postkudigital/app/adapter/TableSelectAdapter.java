package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.ClickInterface;
import com.postkudigital.app.models.Meja;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TableSelectAdapter extends RecyclerView.Adapter<TableSelectAdapter.VH> {
    private Context context;
    private List<Meja> mejaList;
    private ClickInterface clickInterface;
    private int lastSelectedPosition = -1;

    public TableSelectAdapter(Context context, List<Meja> mejaList, ClickInterface clickInterface){
        this.context = context;
        this.mejaList = mejaList;
        this.clickInterface = clickInterface;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_booking, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Meja meja = mejaList.get(position);
        holder.nama.setText(meja.getNama());
        holder.check.setChecked(lastSelectedPosition == position);
        holder.check.setTag(position);
        holder.item.setTag(position);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!meja.isBooked()){
                    itemCheckChange(v);
                }

            }
        });

        if(meja.isBooked()){
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_red));
        }else {
            if(holder.check.isChecked()){
                holder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green));
            }else {
                holder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_green_soft));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mejaList.size();
    }

    private void itemCheckChange(View view) {
        lastSelectedPosition =(Integer)view.getTag();
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        if (lastSelectedPosition >= 0) {
            Meja meja = mejaList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return meja.getId();
        }
        return 0;
    }

    public boolean isBooked(){
        if (lastSelectedPosition >= 0) {
            Meja meja = mejaList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return meja.isBooked();
        }
        return true;
    }

    public String getName(){
        if (lastSelectedPosition >= 0) {
            Meja meja = mejaList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return meja.getNama();
        }
        return "Null";
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama;
        private RadioButton check;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_meja);
            check = view.findViewById(R.id.check);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check.setChecked(lastSelectedPosition == getAdapterPosition());
                }
            });
        }
    }
}
