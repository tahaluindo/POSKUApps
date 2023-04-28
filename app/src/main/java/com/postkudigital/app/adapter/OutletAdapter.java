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
import com.postkudigital.app.models.Toko;

import java.util.List;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.VH> {
    List<Toko> tokoList;
    private Context context;
    private int lastSelectedPosition = 0;

    public OutletAdapter(Context context, List<Toko> tokoList){
        this.context = context;
        this.tokoList = tokoList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outlet, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutletAdapter.VH holder, int position) {
        final Toko toko = tokoList.get(position);
        holder.nama.setText(toko.getNama().toString());
        holder.check.setChecked(lastSelectedPosition == position);
        holder.check.setTag(position);
        holder.item.setTag(position);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChange(v);
            }
        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChange(v);
            }
        });

        if(holder.check.isChecked()){
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.custom_button_outline));
            holder.nama.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
            holder.nama.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return tokoList.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama;
        private RadioButton check;
        public VH(View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.text_nama_outlet);
            check = itemView.findViewById(R.id.check);
            item = itemView.findViewById(R.id.item);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }

    private void itemCheckChange(View view) {
        lastSelectedPosition =(Integer)view.getTag();
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        if (lastSelectedPosition >= 0) {
            Toko v = tokoList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return v.getId();
        }
        return 0;
    }

    public String getNamaToko(){
        if (lastSelectedPosition >= 0) {
            Toko v = tokoList.get(lastSelectedPosition);
            return v.getNama();
        }
        return "";
    }

    public String getAlamatToko(){
        if (lastSelectedPosition >= 0) {
            Toko v = tokoList.get(lastSelectedPosition);
            return v.getAlamat();
        }
        return "";
    }

    public String getLogoToko(){
        if (lastSelectedPosition >= 0) {
            Toko v = tokoList.get(lastSelectedPosition);
            return v.getLogo();
        }
        return "";
    }

    public String getKategoriToko(){
        if (lastSelectedPosition >= 0) {
            Toko v = tokoList.get(lastSelectedPosition);
            return v.getKategori();
        }
        return "";
    }
}

