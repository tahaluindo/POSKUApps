package com.postkudigital.app.actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.postkudigital.app.R;

public class BeginActivity extends AppCompatActivity {
    private int[] images = {R.drawable.banner_1,
            R.drawable.banner_2, R.drawable.banner_3};
    private Button login, register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        CarouselView carouselView = findViewById(R.id.carouselView);
        carouselView.setSize(images.length);
        carouselView.setResource(R.layout.image_carousel_item);
        carouselView.setAutoPlay(true);
        carouselView.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        carouselView.setCarouselOffset(OffsetType.CENTER);
        carouselView.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, int position) {
                // Example here is setting up a full image carousel
                ImageView imageView = view.findViewById(R.id.imageView);
                imageView.setImageDrawable(getResources().getDrawable(images[position]));
            }
        });
        carouselView.show();
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}