package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.fragment.pegawai.ManageStaffActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.VH> implements Filterable {
    private Context context;
    private List<User> userList;
    private List<User> filteredList;

    public StaffAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
        this.filteredList = userList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = userList;
                }else {
                    List<User> list = new ArrayList<>();
                    for(User row : userList){
                        if(row.getNama().toLowerCase().contains(nama.toLowerCase())){
                            list.add(row);
                        }
                    }
                    filteredList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<User>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final User user = filteredList.get(position);
        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        holder.nama.setText(user.getNama());
        holder.ponsel.setText(user.getPhone());
        holder.email.setText(user.getEmail());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManageStaffActivity.class);
                intent.putExtra(Constants.ID, Integer.parseInt(user.getId()));
                intent.putExtra(Constants.METHOD, Constants.EDIT);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private ImageView imageView;
        private TextView nama, ponsel, email;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            imageView = view.findViewById(R.id.img_toko);
            nama = view.findViewById(R.id.text_nama);
            ponsel = view.findViewById(R.id.text_ponsel);
            email = view.findViewById(R.id.text_email);
        }
    }
}
