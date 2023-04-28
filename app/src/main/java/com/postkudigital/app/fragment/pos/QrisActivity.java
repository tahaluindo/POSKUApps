package com.postkudigital.app.fragment.pos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CallbackQrisResponse;
import com.postkudigital.app.json.CreateQrisResponse;
import com.postkudigital.app.json.TransactionResponse;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class QrisActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView caption, invoice, amount, textTimer;
    private ImageView backButton, imgQr;
    private String nominal;
    private static final String FORMAT = "%02d:%02d";
    private CountDownTimer countDownTimer;
    private User user;
    private int idCart;
    private String reffCode="";
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qris);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        caption = findViewById(R.id.text_caption);
        invoice = findViewById(R.id.text_invoice);
        amount = findViewById(R.id.text_amount);
        backButton = findViewById(R.id.back_button);
        imgQr = findViewById(R.id.img_qrcode);
        textTimer = findViewById(R.id.text_timer);
        progressBar = findViewById(R.id.progressBar9);

        caption.setText("QRIS");
        idCart = getIntent().getIntExtra(Constants.ID, 0);
        reffCode = getIntent().getStringExtra(Constants.INVOICE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showData(reffCode);
    }

    private void showData(String code){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.checkQris(code).enqueue(new Callback<CreateQrisResponse>() {
            @Override
            public void onResponse(Call<CreateQrisResponse> call, Response<CreateQrisResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        invoice.setText(response.body().getData().getExternalId());
                        amount.setText(DHelper.formatRupiah(response.body().getData().getAmount()));
                        nominal = String.valueOf(Math.round(response.body().getData().getAmount()));
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                        hints.put(EncodeHintType.MARGIN, 2);
                        hints.put(EncodeHintType.QR_VERSION, 10);
                        String code = response.body().getData().getQrString().toString();
                        try {
                            Bitmap bitmap = barcodeEncoder.encodeBitmap(code
                                    , BarcodeFormat.QR_CODE, 400, 400, hints);
                            imgQr.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        startTimer();

                    }
                }
            }

            @Override
            public void onFailure(Call<CreateQrisResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void confirmExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_exit_qris, null);
        builder.setView(dialogView);

        final Button cancel = dialogView.findViewById(R.id.btn_submit);
        final Button dismiss = dialogView.findViewById(R.id.btn_submit2);

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                countDownTimer.cancel();
                finish();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private void checkCallback(String code, String amount){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.callbackQris(code, String.valueOf(amount)).enqueue(new Callback<CallbackQrisResponse>() {
            @Override
            public void onResponse(Call<CallbackQrisResponse> call, Response<CallbackQrisResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getData() != null){
                            if(response.body().getData().getStatus().equalsIgnoreCase("COMPLETED")){
                                countDownTimer.cancel();
                                pay(idCart, amount);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<CallbackQrisResponse> call, Throwable t) {

            }
        });
    }

    private void pay(int idCart, String amount){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("cart", createPartFromString(String.valueOf(idCart)));
        map.put("payment_type", createPartFromString("2"));
        map.put("uang_bayar", createPartFromString(amount));
        map.put("pegawai", createPartFromString(user.getId()));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.createTransaction(map).enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        Intent intent = new Intent(context, ResultTransactionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void startTimer(){
        countDownTimer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textTimer.setVisibility(View.VISIBLE);
                textTimer.setText(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkCallback(getIntent().getStringExtra(Constants.INVOICE), nominal);
                    }
                }, 5000);
            }

            @Override
            public void onFinish() {
                DHelper.pesan(context, "Waktu tunggu habis, silahkan create ulang QR Code");
                imgQr.setImageDrawable(context.getDrawable(R.drawable.img_qrreload));
                textTimer.setVisibility(View.GONE);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }
}