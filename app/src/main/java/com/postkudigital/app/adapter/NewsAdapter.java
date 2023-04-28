package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.DetailWebActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Artikel;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {
    private Context context;
    private List<Artikel> artikelList;

    public NewsAdapter(Context context, List<Artikel> artikelList){
        this.context = context;
        this.artikelList = artikelList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Artikel a = artikelList.get(position);
        Glide.with(context)
                .load(a.getImage())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imgNews);
        holder.title.setText(a.getTitle());
        holder.count.setText("" + a.getCountSeen());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailWebActivity.class);
                intent.putExtra(Constants.ID, a.getId());
                intent.putExtra(Constants.METHOD, Constants.ARTICLE);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artikelList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private ImageView imgNews;
        private TextView title, count;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            imgNews = view.findViewById(R.id.img_news);
            title = view.findViewById(R.id.text_title);
            count = view.findViewById(R.id.count);
        }
    }
}
