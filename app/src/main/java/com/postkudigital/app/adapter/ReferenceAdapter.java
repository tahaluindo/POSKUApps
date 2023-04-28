package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.ClickInterface;
import com.postkudigital.app.models.location.Reference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.VH> implements Filterable {
    private Context context;
    private List<Reference> referenceList;
    private List<Reference> filteredList;
    private ClickInterface clickInterface;

    public ReferenceAdapter(Context context, List<Reference> referenceList, ClickInterface clickInterface){
        this.context = context;
        this.referenceList = referenceList;
        this.filteredList = referenceList;
        this.clickInterface = clickInterface;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reference, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Reference reff = filteredList.get(position);
        holder.nama.setText(reff.getNama().toString());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemSelected(reff.getId(), reff.getNama());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = referenceList;
                }else {
                    List<Reference> list = new ArrayList<>();
                    for(Reference row : referenceList){
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
                filteredList = (ArrayList<Reference>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    class VH extends RecyclerView.ViewHolder{
        private LinearLayout item;
        private TextView nama;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);
        }
    }
}
