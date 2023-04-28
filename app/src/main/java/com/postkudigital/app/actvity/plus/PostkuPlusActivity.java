package com.postkudigital.app.actvity.plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.SplashActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CheckSubsResponse;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class PostkuPlusActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private ImageView backButton;
    private TextView caption, textmessage, tglAktif;
    private LinearLayout laktif, lsubs;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private Button submit;
    private int saldo = 0;
    private int walletid;
    int total = 0;
    private SwipeRefreshLayout swipe;
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postku_plus);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        submit = findViewById(R.id.button7);
        textmessage = findViewById(R.id.text_message);
        tglAktif = findViewById(R.id.text_tanggal_aktif);
        laktif = findViewById(R.id.laktif);
        lsubs = findViewById(R.id.footer);
        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        swipe = findViewById(R.id.swipe);

        check();
        caption.setText("Postkuplus");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saldo = getIntent().getIntExtra(Constants.NOMINAL, 0);
        walletid = getIntent().getIntExtra(Constants.ID, 0);

        caption.setText("Postku Plus");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check();
                swipe.setRefreshing(false);
            }
        });
    }

    private void check(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.checkSubs(sessionManager.getIdToko()).enqueue(new Callback<CheckSubsResponse>() {
            @Override
            public void onResponse(Call<CheckSubsResponse> call, Response<CheckSubsResponse> response) {
               if(response.isSuccessful()){
                   if(response.body().getStatusCode() == 200){
                       if(response.body().isStatusSub()){
                           laktif.setVisibility(View.VISIBLE);
                           textmessage.setVisibility(View.GONE);
                           SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                           String finalDate = timeFormat.format(response.body().getActiveUntil());
                           tglAktif.setText(finalDate);
                           lsubs.setVisibility(View.GONE);
                           Realm realm = BaseApp.getInstance(context).getRealmInstance();
                           realm.beginTransaction();
                           user.setSubs(true);
                           realm.commitTransaction();
                       }else {
                           textmessage.setVisibility(View.VISIBLE);
                           laktif.setVisibility(View.GONE);
                           lsubs.setVisibility(View.VISIBLE);
                           Realm realm = BaseApp.getInstance(context).getRealmInstance();
                           realm.beginTransaction();
                           user.setSubs(false);
                           realm.commitTransaction();
                       }
                   }else {
                       textmessage.setVisibility(View.VISIBLE);
                       laktif.setVisibility(View.GONE);
                       lsubs.setVisibility(View.VISIBLE);
                       Realm realm = BaseApp.getInstance(context).getRealmInstance();
                       realm.beginTransaction();
                       user.setSubs(false);
                       realm.commitTransaction();
                   }
               }
            }

            @Override
            public void onFailure(Call<CheckSubsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showPopUp(){

        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") final View mDialog = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_subs, null);
        TextView textSaldo = mDialog.findViewById(R.id.text_nomor);
        EditText inputJumlah = mDialog.findViewById(R.id.edt_jumlah);
        Button submit = mDialog.findViewById(R.id.button7);

        textSaldo.setText("Rp" + DHelper.toformatRupiah(String.valueOf(saldo)));
        inputJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    total = 1000 * Integer.parseInt(inputJumlah.getText().toString());
                    submit.setText("Total = " + "Rp" + DHelper.toformatRupiah(String.valueOf(total)));
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(Integer.parseInt(inputJumlah.getText().toString()));
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

    private void subscribe(int jml){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("account_id", createPartFromString(user.getId()));
        map.put("wallet_id", createPartFromString(String.valueOf(walletid)));
        map.put("date_subs", createPartFromString(String.valueOf(jml)));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.subs(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String statusCode = object.getString("status_code");
                        String message = object.getString("msg");
                        if(statusCode.equalsIgnoreCase("200")){
                            dialogSuccess(message, true);
                        }else {
                           dialogSuccess(message, false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    private void dialogSuccess(String msg, boolean isSuccess){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(true);

        final TextView textView = dialog.findViewById(R.id.text_message);
        final TextView title = dialog.findViewById(R.id.text_message2);
        final ImageView img = dialog.findViewById(R.id.imageView10);

        if(isSuccess){
            textView.setText("Success");
            img.setImageDrawable(context.getDrawable(R.drawable.img_success));
            Realm realm = BaseApp.getInstance(context).getRealmInstance();
            realm.beginTransaction();
            user.setSubs(true);
            realm.commitTransaction();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    System.exit(0);
                }
            }, 2000L);


        }else {
            textView.setText("Gagal");
            img.setImageDrawable(context.getDrawable(R.drawable.img_failed));
        }
        title.setText(msg);
        dialog.show();

    }
}