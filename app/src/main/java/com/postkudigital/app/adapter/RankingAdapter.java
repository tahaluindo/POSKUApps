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
import com.postkudigital.app.models.Ranking;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.VH> {
    private Context context;
    private List<Ranking> rankingList;

    public RankingAdapter(Context context, List<Ranking> rankingList){
        this.context = context;
        this.rankingList = rankingList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Ranking ranking = rankingList.get(position);
        holder.nama.setText(ranking.getNama());
        holder.qty.setText("" + ranking.getQty());
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama, qty;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);
            qty = view.findViewById(R.id.text_qty);
        }
    }
}
