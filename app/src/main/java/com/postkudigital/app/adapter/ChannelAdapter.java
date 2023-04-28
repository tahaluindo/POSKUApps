package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.models.Channel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.VH> {
    List<Channel> channelList;
    private Context context;
    private int lastSelectedPosition = 0;

    public ChannelAdapter(Context context, List<Channel> channelList){
        this.context = context;
        this.channelList = channelList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Channel channel = channelList.get(position);
        holder.jenis.setText(channel.getJenisPembayaran());
        holder.nomor.setText(channel.getNomer());
        holder.nama.setText(channel.getNama());
        holder.check.setChecked(lastSelectedPosition == position);
        holder.check.setTag(position);
        holder.item.setTag(position);

        Glide.with(context)
                .load(channel.getLogo())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.logo);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChange(v);
            }
        });

        if(holder.check.isChecked()){
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.custom_button_outline));
            holder.nama.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.nomor.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.item.setBackground(context.getResources().getDrawable(R.drawable.custom_curve));
            holder.nama.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.nomor.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    private void itemCheckChange(View view) {
        lastSelectedPosition =(Integer)view.getTag();
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        if (lastSelectedPosition >= 0) {
            Channel v = channelList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return v.getId();
        }
        return 0;
    }

    public String getChannelName() {
        if (lastSelectedPosition >= 0) {
            Channel v = channelList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return v.getJenisPembayaran();
        }
        return "Null";
    }

    public class VH extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private TextView nama, jenis, nomor;
        private CircleImageView logo;
        private RadioButton check;
        public VH(View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.text_nama);
            jenis = itemView.findViewById(R.id.text_jenis);
            nomor = itemView.findViewById(R.id.text_nomor);
            item = itemView.findViewById(R.id.item);
            check = itemView.findViewById(R.id.check);
            logo = itemView.findViewById(R.id.logo);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check.setChecked(lastSelectedPosition == getAdapterPosition());
                }
            });
        }
    }
}
