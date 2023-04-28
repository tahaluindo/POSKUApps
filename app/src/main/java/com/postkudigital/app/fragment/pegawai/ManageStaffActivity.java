package com.postkudigital.app.fragment.pegawai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.RegisterResponseJson;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.NetworkUtils;
import com.postkudigital.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class ManageStaffActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private ImageView backButton, imgStaff;
    private Button submit;
    private TextView caption, delete;
    private EditText username, email, password, nama, phone, alamat;
    private int id= 0;
    private ProgressBar progressBar;
    File imageFileOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_staff);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        imgStaff = findViewById(R.id.img_toko);
        submit = findViewById(R.id.btn_register);
        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        email = findViewById(R.id.edt_email);
        nama = findViewById(R.id.edt_nama_akun);
        phone = findViewById(R.id.edt_phone);
        alamat = findViewById(R.id.edt_alamat);
        progressBar = findViewById(R.id.progressBar);
        delete = findViewById(R.id.delete2);

        if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
            caption.setText("Tambah Pegawai");
        }else {
            caption.setText("Edit Pegawai");
            id = getIntent().getIntExtra(Constants.ID, 0);
            detailAccount(String.valueOf(id));
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ManageStaffActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ManageStaffActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }
                dialogImagePicker(1);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    username.setError(context.getString(R.string.error_empty));
                    username.requestFocus();
                    return;
                }else if(password.getText().toString().isEmpty() &&
                        getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
                    password.setError(context.getString(R.string.error_empty));
                    password.requestFocus();
                    return;
                }else if(email.getText().toString().isEmpty()){
                    email.setError(context.getString(R.string.error_empty));
                    email.requestFocus();
                    return;
                }else if(nama.getText().toString().isEmpty()){
                    nama.setError(context.getString(R.string.error_empty));
                    nama.requestFocus();
                    return;
                }else if(phone.getText().toString().isEmpty()){
                    phone.setError(context.getString(R.string.error_empty));
                    phone.requestFocus();
                    return;
                }else if(alamat.getText().toString().isEmpty()) {
                    alamat.setError(context.getString(R.string.error_empty));
                    alamat.requestFocus();
                    return;
                }else if(imageFileOwner == null &&
                        getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
                    DHelper.pesan(context, "Harap upload foto pegawai");
                    return;
                }

                if(NetworkUtils.isConnected(context)){
                    Log.e(TAG, "completed data");
                    if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
                        createAccount("@" + username.getText().toString(),
                                password.getText().toString(),
                                email.getText().toString());
                    }else{
                        updateAccount(String.valueOf(id));
                    }

                }else{
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStaff(String.valueOf(id));
            }
        });
    }

    private void dialogImagePicker(int sourceFrom){
        String[]items = {"Kamera", "Galery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ambil gambar");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(sourceFrom == 1){
                    switch (which){
                        case 0:
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, Constants.CAMERA_PROFILE_REQUEST);
                            break;
                        case 1:
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivityForResult(intent, Constants.GALERY_PROFILE_REQUEST);
                            break;
                    }
                }else{
                    switch (which){
                        case 0:
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, Constants.CAMERA_TOKO_REQUEST);
                            break;
                        case 1:
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivityForResult(intent, Constants.GALERY_TOKO_REQUEST);
                            break;
                    }
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createAccount(String username, String password, String email){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class);
        service.createUser(username, email, password).enqueue(new Callback<RegisterResponseJson>() {
            @Override
            public void onResponse(Call<RegisterResponseJson> call, Response<RegisterResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode().equalsIgnoreCase("201")){
                        updateAccount(response.body().getUser().getId());
                    }else {
                        DHelper.pesan(context, response.body().getMsg());
                    }
                }else {
                    DHelper.pesan(context, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void updateAccount(String idUser){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_user", createPartFromString(idUser));
        map.put("id_toko", createPartFromString(sessionManager.getIdToko()));
        map.put("nama", createPartFromString(nama.getText().toString().trim()));
        map.put("phone", createPartFromString(phone.getText().toString().trim()));
        map.put("email", createPartFromString(email.getText().toString().trim()));
        map.put("address", createPartFromString(alamat.getText().toString().trim()));
        MultipartBody.Part body = null;
        if(imageFileOwner != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileOwner);
            body = MultipartBody.Part.createFormData("profile_pic", imageFileOwner.getName(), reqFile);
        }

        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updatepegawai(body, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    DHelper.pesan(context, "Success");
                    finish();
                }else {
                    Log.e(TAG, response.errorBody().toString());
                    DHelper.pesan(context, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }

    private void detailAccount(String idUser){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailAccount(idUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int code = Integer.parseInt(object.getString("status_code"));
                        if(code == 200){
                            JSONObject json = object.getJSONObject("data");
                            Glide.with(context)
                                    .load(json.getString("profile_pic"))
                                    .placeholder(R.drawable.image_placeholder)
                                    .into(imgStaff);

                            username.setText(json.getString("username"));
                            nama.setText(json.getString("nama"));
                            phone.setText(json.getString("phone"));
                            email.setText(json.getString("email"));
                            alamat.setText(json.getString("address"));

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

            }
        });
    }

    private void deleteStaff(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_user", createPartFromString(id));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.deletePegawai(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, "success");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.CAMERA_PROFILE_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgStaff.setImageBitmap(bitmap);
                imageFileOwner = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_PROFILE_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    float sizeori = DHelper.getImageSize(context, contentUri);
                    float sizemb = sizeori/(1024f * 1024f);
                    if(sizemb < 5){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                            imgStaff.setImageBitmap(bitmap);
                            imageFileOwner = DHelper.createTempFile(context, bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        DHelper.pesan(context, "Ukuran gambar maksimal 5mb");
                    }

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}