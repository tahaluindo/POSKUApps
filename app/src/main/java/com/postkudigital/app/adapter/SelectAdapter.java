package com.postkudigital.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.AddOnClickListener;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.ServiceAdd;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.postkudigital.app.helpers.Constants.TAG;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.VH> {
    private List<ServiceAdd> dataList;
    private Context mContext;
    private int rowLayout;
    private boolean isMulti;
    private AddOnClickListener clickInterface;
    private int lastSelectedPosition = -1;
    private String metode="";
    private SessionManager sessionManager;
    private boolean isActive = true;

    public SelectAdapter(Context context, List<ServiceAdd> dataList, int rowLayout, AddOnClickListener clickInterface, boolean isMulti, String metode) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.isMulti = isMulti;
        this.clickInterface = clickInterface;
        this.metode = metode;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  VH holder, int position) {
        sessionManager = new SessionManager(mContext);

        final ServiceAdd service = dataList.get(position);
        holder.text.setText(service.getNama());
        if(isMulti){
            if(metode.equalsIgnoreCase(Constants.SERVICE_CHARGE)){
                if(sessionManager.getSeviceList() != null && sessionManager.getSeviceList().size() >0){
                    for(Integer row : sessionManager.getSeviceList()){
                        Log.e(TAG, "row---" + row + " id---" + service.getId());
                        if(row == service.getId()){
                            service.setChecked(true);
                            holder.checkBox.setChecked(true);
                        }
                    }

                }
            }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        service.setChecked(true);
                    }else {
                        service.setChecked(false);
                    }
                }
            });
        }else {

            holder.check.setChecked(lastSelectedPosition == position);
            holder.check.setTag(position);
            holder.select.setTag(position);
            holder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeActive();
                    itemCheckChange(v);
                }
            });

            if(service.isChecked()){
                holder.select.setBackground(mContext.getResources().getDrawable(R.drawable.bg_curve_left_grey));
            }else {
                if(holder.check.isChecked()){
                    holder.select.setBackground(mContext.getResources().getDrawable(R.drawable.bg_curve_left_grey));
                }else {
                    holder.select.setBackground(mContext.getResources().getDrawable(R.drawable.bg_curve_left));
                }
            }

        }

    }

    private void removeActive(){
        for(int i = 0;i < dataList.size();i++){
            ServiceAdd serviceAdd = dataList.get(i);
            serviceAdd.setChecked(false);
        }
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    private void itemCheckChange(View view) {
        Log.e(TAG, "lastpos:--" + lastSelectedPosition);
        lastSelectedPosition =(Integer)view.getTag();
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView text, select;
        CheckBox checkBox;
        RadioButton check;
        public VH(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            select = itemView.findViewById(R.id.select);
            check = itemView.findViewById(R.id.check);
            text = itemView.findViewById(R.id.text_nama);
            if(!isMulti){

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check.setChecked(lastSelectedPosition == getAdapterPosition());
                    }
                });
            }

        }
    }

    public int getSelectedItem() {
        if (lastSelectedPosition >= 0) {
            ServiceAdd serviceAdd = dataList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return serviceAdd.getId();
        }
        return 0;
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(dataList.size());
        for (int i = 0; i < dataList.size(); ++i) {
            if(dataList.get(i).isChecked()){
                items.add(dataList.get(i).getId());
            }
        }
        return items;
    }

    public int getTotalServiceFee(){
        int total = 0;
        for (int i = 0; i < dataList.size(); ++i) {
            if(dataList.get(i).isChecked()){
                total += dataList.get(i).getNominal();
            }
        }
        return total;
    }

    public String getSelectedName(){
        if (lastSelectedPosition >= 0) {
            ServiceAdd serviceAdd = dataList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return serviceAdd.getNama();
        }
        return "";
    }

    public int getSelectedType(){
        if (lastSelectedPosition >= 0) {
            ServiceAdd serviceAdd = dataList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return serviceAdd.getType();
        }
        return 0;
    }

    public int getSelectedValue(){
        if (lastSelectedPosition >= 0) {
            ServiceAdd serviceAdd = dataList.get(lastSelectedPosition);
//            Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
            return serviceAdd.getNominal();
        }
        return 0;
    }

}
