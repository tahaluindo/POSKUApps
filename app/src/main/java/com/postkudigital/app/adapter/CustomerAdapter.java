package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.customer.CustomerFragment;
import com.postkudigital.app.models.Customer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.VH> implements Filterable {
    private Context context;
    private List<Customer> customerList;
    private List<Customer> filteredList;
    private CustomerFragment fragment;

    public CustomerAdapter(Context context, List<Customer> customerList, CustomerFragment fragment){
        this.context = context;
        this.customerList = customerList;
        this.filteredList = customerList;
        this.fragment = fragment;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String nama = constraint.toString();
                if(nama.isEmpty()){
                    filteredList = customerList;
                }else {
                    List<Customer> list = new ArrayList<>();
                    for(Customer row : customerList){
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
                filteredList = (ArrayList<Customer>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        final Customer cust = filteredList.get(position);
        holder.nama.setText(cust.getNama());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showDialog(true, String.valueOf(cust.getId()), cust.getNama(), cust.getPhone(), cust.getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class VH extends RecyclerView.ViewHolder{
        private RelativeLayout item;
        private TextView nama;
        public VH(View view){
            super(view);
            item = view.findViewById(R.id.item);
            nama = view.findViewById(R.id.text_nama);

        }
    }
}
