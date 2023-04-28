package com.postkudigital.app.fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mazenrashed.printooth.Printooth;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;

public class SettingFragment extends Fragment {
    private Context context;
    private RelativeLayout rlPrinter, rlScanner;
    private TextView namaPrinter;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        context = getActivity();
        rlPrinter = view.findViewById(R.id.rlprinter);
        rlScanner = view.findViewById(R.id.rlscanner);
        namaPrinter = view.findViewById(R.id.text_printer_status);

        if (Printooth.INSTANCE.getPairedPrinter()!=null){
            namaPrinter.setText(Printooth.INSTANCE.getPairedPrinter().getName());
        }else {
            namaPrinter.setText("Belum terhubung");
        }

        rlPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrinterActivity.class);
                startActivity(intent);
            }
        });

        rlScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DHelper.pesan(context, "Sorry, This feature is not available, we will update coming soon" +
                        "");
            }
        });
        return view;
    }
}