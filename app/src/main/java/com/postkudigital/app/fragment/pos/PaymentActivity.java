package com.postkudigital.app.fragment.pos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CreateQrisResponse;
import com.postkudigital.app.json.TransactionResponse;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class PaymentActivity extends AppCompatActivity {
    private Context context;
    private User user;
    private SessionManager sessionManager;
    private TextView totalTagihan, totalBayar;
    private Button one, two, three, four, five, six, seven, eight, nine, nol, clear;
    private RelativeLayout next;
    private String inputNumber = "";
    double tagihan;
    private LinearLayout lpas;
    private RadioGroup payMethod;
    private RadioButton rbTunai, rbQris;
    private int metode = 1;
    private ProgressBar progressBar;
    private ImageView backButton;
    private TextView caption;
    private int countFail = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        totalTagihan = findViewById(R.id.text_total_tagihan);
        totalBayar = findViewById(R.id.text_bayar);
        one = findViewById(R.id.btn_one);
        two = findViewById(R.id.btn_two);
        three = findViewById(R.id.btn_three);
        four = findViewById(R.id.btn_four);
        five = findViewById(R.id.btn_five);
        six = findViewById(R.id.btn_six);
        seven = findViewById(R.id.btn_seven);
        eight = findViewById(R.id.btn_eight);
        nine = findViewById(R.id.btn_nine);
        nol = findViewById(R.id.btn_multiple);
        clear = findViewById(R.id.btn_clear);
        next = findViewById(R.id.btn_next);
        lpas = findViewById(R.id.lpas);
        payMethod = findViewById(R.id.method);
        rbTunai = findViewById(R.id.rbTunai);
        rbQris = findViewById(R.id.rbQris);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);

        caption.setText("Detail Pembayaran");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tagihan = getIntent().getDoubleExtra(Constants.ADD, 0);

        totalTagihan.setText(DHelper.formatRupiah(tagihan));

        payMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbTunai:
                        metode = 1;
                        disabledKeyNum(false);
                        break;
                    case R.id.rbQris:
                        if(tagihan < 1500){
                            rbTunai.setChecked(true);
                            DHelper.pesan(context, "Untuk menggunakan Qris total pembayaran harus lebih dari Rp1.500");
                        }else {
                            metode = 2;
                            disabledKeyNum(true);
                            inputNumber = String.format("%.0f",tagihan);
                            Log.e("NOMINAL", inputNumber);
                            totalBayar.setText(DHelper.toformatRupiah(inputNumber));
                        }
                        break;
                }
            }
        });

        rbTunai.setChecked(true);

        lpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = String.format("%.0f",tagihan);
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

//        totalBayar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length() > 0){
//                    if(Integer.parseInt(inputNumber) > tagihan){
//                        next.setEnabled(true);
//                    }else {
//                        next.setEnabled(false);
//                    }
//                }
//            }
//        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "1";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "2";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "3";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "4";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "5";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "6";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "7";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "8";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "9";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        nol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = inputNumber + "0";
                totalBayar.setText(DHelper.toformatRupiah(inputNumber));
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNumber = "";
                totalBayar.setText("");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Id Cart:" + getIntent().getIntExtra(Constants.ID, 0) + " metode:" + metode);
                if(inputNumber.equalsIgnoreCase("") || inputNumber.equalsIgnoreCase("0")){
                    DHelper.pesan(context, "Pembayaran harus diisi");
                    return;
                }
                if(rbTunai.isChecked() && Integer.parseInt(inputNumber.replaceAll(",","").replaceAll("\\.","")) < tagihan){
                    DHelper.pesan(context, "Pembayaran kurang");
                    return;
                }
                if(metode == 1){
                    pay(getIntent().getIntExtra(Constants.ID, 0), metode, inputNumber.replaceAll(",","").replaceAll("\\.",""));
                }else{
                    payQris(getIntent().getStringExtra(Constants.INVOICE), inputNumber);
                }

            }
        });


    }

    private void disabledKeyNum(boolean isDisbaled){
        if(isDisbaled){
            lpas.setVisibility(View.GONE);
            nol.setEnabled(false);
            one.setEnabled(false);
            two.setEnabled(false);
            three.setEnabled(false);
            four.setEnabled(false);
            five.setEnabled(false);
            six.setEnabled(false);
            seven.setEnabled(false);
            eight.setEnabled(false);
            nine.setEnabled(false);
            clear.setEnabled(false);
        }else{
            lpas.setVisibility(View.VISIBLE);
            nol.setEnabled(true);
            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            five.setEnabled(true);
            six.setEnabled(true);
            seven.setEnabled(true);
            eight.setEnabled(true);
            nine.setEnabled(true);
            clear.setEnabled(true);
        }
    }

    private void pay(int idCart,int metode, String nominal){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("cart", createPartFromString(String.valueOf(idCart)));
        map.put("payment_type", createPartFromString(String.valueOf(metode)));
        map.put("uang_bayar", createPartFromString(inputNumber.replaceAll(",","").replaceAll("\\.","")));
        map.put("pegawai", createPartFromString(user.getId()));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.createTransaction(map).enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        Intent intent = new Intent(context, ResultTransactionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(Constants.ID, response.body().getTransaction().getId());
                        intent.putExtra(Constants.INVOICE, response.body().getTransaction().getReffCode());
                        startActivity(intent);
                        finish();
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void payQris(String invoice, String nominal){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("cart_code", createPartFromString(invoice));
        map.put("amount", createPartFromString(inputNumber.replaceAll(",","").replaceAll("\\.","")));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.payQithQris(map).enqueue(new Callback<CreateQrisResponse>() {
            @Override
            public void onResponse(Call<CreateQrisResponse> call, Response<CreateQrisResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        Intent intent = new Intent(context, QrisActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.ID, getIntent().getIntExtra(Constants.ID,0));
                        intent.putExtra(Constants.INVOICE, invoice);
                        startActivity(intent);
                        finish();
                    }else {
                        if(response.body().getStatusCode() == 400){
                            countFail+=1;
                            String inv = getIntent().getStringExtra(Constants.INVOICE);
                            inv = inv + "-" + countFail;
                            payQris(inv, inputNumber);
                        }else {
                            DHelper.pesan(context, response.body().getMessage());
                        }

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<CreateQrisResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
}