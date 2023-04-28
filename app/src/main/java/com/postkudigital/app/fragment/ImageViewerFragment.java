package com.postkudigital.app.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;

public class ImageViewerFragment extends DialogFragment {
    private Context context;
    private ImageView image, close;
    public ImageViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        context = getActivity();
        image = view.findViewById(R.id.imageView51);
        close = view.findViewById(R.id.imageView52);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Glide.with(context)
                .load(getArguments().getString(Constants.INTENT_DATA))
                .placeholder(R.drawable.image_placeholder)
                .into(image);

        return view;
    }
}