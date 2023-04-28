package com.postkudigital.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.absensi.DetailAbsenActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.models.Absensi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.VH> implements Filterable {
    private Context context;
    private List<Absensi> absensiList;
    private List<Absensi> filteredList;

    public AbsensiAdapter(Context context, List<Absensi> absensiList){
        this.context = context;
        this.absensiList = absensiList;
        this.filteredList = absensiList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = absensiList;
                }else {
                    List<Absensi> list = new ArrayList<>();
                    for(Absensi row : absensiList){
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
                filteredList = (ArrayList<Absensi>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absensi, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Absensi absensi = filteredList.get(position);
        holder.nama.setText(absensi.getNama());
        holder.phone.setText(absensi.getPhone());
        holder.tanggal.setText(DHelper.strTodatetime(absensi.getCreatedAt()));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailAbsenActivity.class);
                intent.putExtra(Constants.ID, absensi.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RelativeLayout item;
        private TextView nama, phone, tanggal;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);
            phone = view.findViewById(R.id.text_phone);
            tanggal = view.findViewById(R.id.text_phone2);

        }
    }
}
