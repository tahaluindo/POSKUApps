package com.postkudigital.app.actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.LoginResponseJson;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.NetworkUtils;
import com.postkudigital.app.utils.SessionManager;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private TextView register, caption, resetPassword;
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private ImageView imgBack;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        sessionManager = new SessionManager(context);
        register = findViewById(R.id.text_register);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        caption = findViewById(R.id.text_caption);
        imgBack = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        resetPassword = findViewById(R.id.text_reset);
//        initialize app bar title
        caption.setText(context.getString(R.string.label_btn_login));
//        action back button
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        action register text
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
//        action button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUsername.getText().toString().isEmpty()){
                    edtUsername.setError(context.getString(R.string.error_empty));
                    edtUsername.requestFocus();
                    return;
                }else if(edtPassword.getText().toString().isEmpty()){
                    edtPassword.setError(context.getString(R.string.error_empty));
                    edtPassword.requestFocus();
                    return;
                }
//                check internet connection
                if(NetworkUtils.isConnected(context)){
                    login(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WebViewActivity.class);
                i.putExtra("url", "https://dashboard.postku.site/reset_password/");
                i.setData(Uri.parse("https://dashboard.postku.site/reset_password/"));
                startActivity(i);
            }
        });

    }

    private void login(String username, String password){
        progressBar.setVisibility(View.VISIBLE);
        RequestBody userBody = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody passwdBody = RequestBody.create(MediaType.parse("text/plain"), password);
        UserService service = ServiceGenerator.createService(UserService.class);
        service.login(userBody, passwdBody).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(Call<LoginResponseJson> call, Response<LoginResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMsg());
                        dialogSuccess(response.body().getMsg());
                        sessionManager.createSessionLogin(response.body().getToken());
                        User user = response.body().getUser();
                        saveUser(user);

                    }else {
                        DHelper.pesan(context, response.body().getMsg());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<LoginResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(Constants.TAG, t.getMessage());
            }
        });
    }

    private void dialogSuccess(String msg){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(true);

        final TextView textView = dialog.findViewById(R.id.text_message);

        textView.setText(msg);

        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // Close dialog after 1000ms
                dialog.cancel();
                Intent intent = new Intent(context, SelectOutletActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(LoginActivity.this).setLoginUser(user);
    }
}