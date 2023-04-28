package com.postkudigital.app.actvity.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.KonfirmTopupResponseJson;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class KonfirmasiTopupActivity extends AppCompatActivity implements SelectedChannel.UpdateText {
    private Context context;
    private SessionManager sessionManager;
    private TextView textNominal, textJenis, caption, textUpload;
    private RelativeLayout selectChannel, upload;
    private Button button;
    private ImageView backButton, imgBukti;
    private int id;
    private long nominal;
    private String idChannel="";
    File imageFileOwner;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_topup);
        context = this;
        sessionManager = new SessionManager(context);
        textNominal = findViewById(R.id.text_nominal);
        textJenis = findViewById(R.id.text_channel);
        selectChannel = findViewById(R.id.selectchannel);
        upload = findViewById(R.id.upload);
        button = findViewById(R.id.btn_submit);
        caption = findViewById(R.id.text_caption);
        backButton = findViewById(R.id.back_button);
        imgBukti = findViewById(R.id.imageView45);
        textUpload = findViewById(R.id.text_upload);
        progressBar = findViewById(R.id.progressBar7);

        caption.setText("Konfirmasi Topup");

        id = getIntent().getIntExtra(Constants.ID, 0);
        nominal = getIntent().getIntExtra(Constants.NOMINAL, 0);
        textNominal.setText(DHelper.toformatRupiah(String.valueOf(nominal)));

        Log.e(TAG, getIntent().getLongExtra(Constants.NOMINAL, 0) + "");

        final SelectedChannel dialogFragment = new SelectedChannel();
        selectChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.PROVINSI);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(KonfirmasiTopupActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(KonfirmasiTopupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }
                dialogImagePicker(1);
            }
        });

        textUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(KonfirmasiTopupActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(KonfirmasiTopupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }
                dialogImagePicker(1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idChannel.equalsIgnoreCase("")){
                    DHelper.pesan(context, "Silahkan pilih channel pembayaran");
                    return;
                }else if(imageFileOwner == null){
                    DHelper.pesan(context, "Silahkan upload bukti pembayaran");
                    return;
                }

                konfirm();

            }
        });

    }

    private void konfirm(){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("wallet", createPartFromString(String.valueOf(id)));
        map.put("chanel", createPartFromString(idChannel));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileOwner);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", imageFileOwner.getName(), reqFile);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.konfirmTopup(body, map).enqueue(new Callback<KonfirmTopupResponseJson>() {
            @Override
            public void onResponse(Call<KonfirmTopupResponseJson> call, Response<KonfirmTopupResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    DHelper.pesan(context, response.body().getMessage());
                    Intent intent = new Intent(context, TopupPendingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    Log.e(TAG, response.errorBody().toString());
                    DHelper.pesan(context, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<KonfirmTopupResponseJson> call, Throwable t) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.CAMERA_PROFILE_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgBukti.setVisibility(View.VISIBLE);
                imgBukti.setImageBitmap(bitmap);
                imageFileOwner = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_PROFILE_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        imgBukti.setVisibility(View.VISIBLE);
                        imgBukti.setImageBitmap(bitmap);
                        imageFileOwner = DHelper.createTempFile(context, bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void updateResult(String id, String nama) {
        textJenis.setText(nama);
        idChannel = id;
    }
}