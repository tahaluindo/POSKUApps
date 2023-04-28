package com.postkudigital.app.actvity.ppob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.ProductPpobAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetProdukPponResponse;
import com.postkudigital.app.json.TransppobResponseJson;
import com.postkudigital.app.models.ProductPpob;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class TransPpobActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private ImageView backButton, logo;
    private TextView caption, namaProduk;
    private RecyclerView recyclerView;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private EditText nomor;
    private String category, brand, urlLogo;
    private LinearLayout lempty;
    private ProductPpobAdapter adapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_ppob);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        backButton = findViewById(R.id.back_button);
        logo = findViewById(R.id.img_logo);
        caption = findViewById(R.id.text_caption);
        namaProduk = findViewById(R.id.text_nama);
        nomor = findViewById(R.id.edt_nomor);
        recyclerView = findViewById(R.id.rec_product);
        lempty = findViewById(R.id.lempty);
        progressBar = findViewById(R.id.progressBar);
        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        caption.setText("Pilih Produk");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        category = getIntent().getStringExtra(Constants.METHOD);
        brand = getIntent().getStringExtra(Constants.NAMA);
        urlLogo = getIntent().getStringExtra(Constants.ADD);

        namaProduk.setText(brand);
        Glide.with(context)
                .load(urlLogo)
                .placeholder(R.drawable.image_placeholder)
                .into(logo);
        nomor.setText("0" + user.getPhone());
        nomor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(nomor.getText().toString().length() > 0){
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        if(motionEvent.getRawX() >= (nomor.getRight() - nomor
                                .getCompoundDrawables()[DRAWABLE_RIGHT]
                                .getBounds()
                                .width())){
                            nomor.setText("");
                            return true;
                        }
                    }
                }
                return false;
            }
        });


        getData(category, brand);

    }

    private void getData(String cat, String merk) {
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.ppob(cat, merk).enqueue(new Callback<GetProdukPponResponse>() {
            @Override
            public void onResponse(Call<GetProdukPponResponse> call, Response<GetProdukPponResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getProductPpobList().isEmpty()){
                            lempty.setVisibility(View.VISIBLE);
                        }else {
                            adapter = new ProductPpobAdapter(context, response.body().getProductPpobList(), TransPpobActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetProdukPponResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showDetailProduct(ProductPpob ppob){
        if(nomor.getText().toString().isEmpty()){
            nomor.setError("Masukkan nomor");
            nomor.requestFocus();
            return;
        }
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") final View mDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_ppob, null);
        TextView textNomor = mDialog.findViewById(R.id.text_nomor);
        TextView textKategori = mDialog.findViewById(R.id.text_kategori);
        TextView textBrand = mDialog.findViewById(R.id.text_brand);
        TextView textSaldo = mDialog.findViewById(R.id.text_saldo);
        TextView textHarga = mDialog.findViewById(R.id.text_harga);
        TextView textBiaya = mDialog.findViewById(R.id.text_biaya);
        TextView textTotal = mDialog.findViewById(R.id.text_total);
        Button ubah = mDialog.findViewById(R.id.btn_submit);
        Button bayar = mDialog.findViewById(R.id.btn_submit2);

        int harga = 0;
        int biaya = 0;
        int total = 0;

        harga = ppob.getPricePostku();
        total = harga + biaya;
        textNomor.setText(nomor.getText().toString());
        textKategori.setText(ppob.getCategory());
        textBrand.setText(ppob.getBrand());
        textHarga.setText(DHelper.toformatRupiah(String.valueOf(ppob.getPricePostku())));
        textBiaya.setText(DHelper.toformatRupiah(String.valueOf(biaya)));
        textTotal.setText(DHelper.toformatRupiah(String.valueOf(total)));
        textSaldo.setText(DHelper.toformatRupiah(sessionManager.getSaldoWallet()));

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrans(nomor.getText().toString(), ppob.getBuyerSkuCode());
                mBottomSheetDialog.dismiss();

            }
        });

        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(mDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(mBottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    private void startTrans(String nomor, String sku){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("customer_no", createPartFromString(nomor));
        map.put("buyer_sku_code", createPartFromString(sku));
        map.put("wallet", createPartFromString(sessionManager.getIdWallet()));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.startTrans(map).enqueue(new Callback<TransppobResponseJson>() {
            @Override
            public void onResponse(Call<TransppobResponseJson> call, Response<TransppobResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        dialogSuccess(response.body().getMessage(), true);
                    }else {
                        dialogSuccess(response.body().getMessage(), false);
                    }
                }else {
                    dialogSuccess("Error connection", false);
                }
            }

            @Override
            public void onFailure(Call<TransppobResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    private void dialogSuccess(String msg, boolean isSuccess){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(true);

        final TextView textView = dialog.findViewById(R.id.text_message);
        final TextView title = dialog.findViewById(R.id.text_message2);
        final ImageView img = dialog.findViewById(R.id.imageView10);

        if(isSuccess){
            textView.setText("Success");
            title.setText("Silahkan lihat status transaksi di menu riwayat PPOB");
            img.setImageDrawable(context.getDrawable(R.drawable.img_success));
        }else {
            textView.setText("Gagal");
            title.setText(msg);
            img.setImageDrawable(context.getDrawable(R.drawable.img_success));
        }



        dialog.show();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                // Close dialog after 1000ms
//                dialog.cancel();
//
//            }
//        }, 1000);
    }
}