package com.postkudigital.app.actvity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.fragment.ReferenceFragment;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CreateTokoResponse;
import com.postkudigital.app.json.RegisterResponseJson;
import com.postkudigital.app.models.location.Kecamatan;
import com.postkudigital.app.models.location.Kelurahan;
import com.postkudigital.app.models.location.Kota;
import com.postkudigital.app.models.location.Provinsi;
import com.postkudigital.app.services.ApiLocationService;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.ReferenceService;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class RegisterActivity extends AppCompatActivity implements ReferenceFragment.UpdateText {
    private Context context;
    private EditText edtUsername, edtPassword, edtEmail, edtNama, edtPhone, edtAlamat,
                     edtNamaToko, edtAlamatToko;
    private ImageView backButton, imgOwner, imgToko;
    private Button submit;
    private TextView caption;
    private AutoCompleteTextView selectKategori, selectProvinsi, selectKota, selectKecamata, selectDesa;
    String[] listCategory;
    ArrayList<String> listProvinsi = new ArrayList<>();
    ArrayList<String> listKab = new ArrayList<>();
    ArrayList<String> listKecamatan = new ArrayList<>();
    ArrayList<String> listDesa = new ArrayList<>();
    List<Provinsi> provinsiList = new ArrayList<>();
    List<Kota> kotaKabList = new ArrayList<>();
    List<Kecamatan> kecamatanList = new ArrayList<>();
    List<Kelurahan> desaList = new ArrayList<>();
    ArrayAdapter<String> adapterKota;
    ArrayAdapter<String> adapterKecamatan;
    ArrayAdapter<String> adapterKelurahan;
    int idProvinsi = 0;
    int idKota = 0;
    int idKecamatan = 0;
    File imageFileOwner;
    File imageFileToko;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        edtUsername = findViewById(R.id.edt_phone);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        edtNama = findViewById(R.id.edt_nama_akun);
        edtPhone = findViewById(R.id.edt_phone);
        edtAlamat = findViewById(R.id.edt_alamat);
        edtNamaToko = findViewById(R.id.edt_nama_toko);
        edtAlamatToko = findViewById(R.id.edt_alamat_toko);
        selectKategori = findViewById(R.id.kategori);
        selectProvinsi = findViewById(R.id.provinsi);
        selectKota = findViewById(R.id.kabkota);
        selectKecamata = findViewById(R.id.kecamatan);
        selectDesa = findViewById(R.id.desa);
        imgOwner = findViewById(R.id.img_owner);
        imgToko = findViewById(R.id.img_toko);
        submit = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        setup select kategori
        listCategory = getResources().getStringArray(R.array.shop_category);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listCategory);
        selectKategori.setAdapter(catAdapter);
        selectKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKategori.showDropDown();
            }
        });

//        setup select provinsi
//        getProvinsi();
        final ReferenceFragment dialogFragment = new ReferenceFragment();
        selectProvinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.PROVINSI);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });


        selectKota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.KABKOTA);
                bundle.putInt(Constants.ID, idProvinsi);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        selectKecamata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.KECAMATAN);
                bundle.putInt(Constants.ID, idKota);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        selectDesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, Constants.KELURAHAN);
                bundle.putInt(Constants.ID, idKecamatan);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        imgOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }
                dialogImagePicker(1);
            }
        });

        imgToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }
                dialogImagePicker(2);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(edtUsername.getText().toString().isEmpty()){
//                    edtUsername.setError(context.getString(R.string.error_empty));
//                    edtUsername.requestFocus();
//                    return;
//                }else
                if(edtPassword.getText().toString().isEmpty()){
                    edtPassword.setError(context.getString(R.string.error_empty));
                    edtPassword.requestFocus();
                    return;
                }else if(edtEmail.getText().toString().isEmpty()){
                    edtEmail.setError(context.getString(R.string.error_empty));
                    edtEmail.requestFocus();
                    return;
                }else if(edtNama.getText().toString().isEmpty()){
                    edtNama.setError(context.getString(R.string.error_empty));
                    edtNama.requestFocus();
                    return;
                }else if(edtPhone.getText().toString().isEmpty()){
                    edtPhone.setError(context.getString(R.string.error_empty));
                    edtPhone.requestFocus();
                    return;
                }else if(edtAlamat.getText().toString().isEmpty()) {
                    edtAlamat.setError(context.getString(R.string.error_empty));
                    edtAlamat.requestFocus();
                    return;
                }else if(edtNamaToko.getText().toString().isEmpty()){
                    edtNamaToko.setError(context.getString(R.string.error_empty));
                    edtNamaToko.requestFocus();
                    return;
                }else if(selectKategori.getText().toString().isEmpty()){
                    selectKategori.setError(context.getString(R.string.error_empty));
                    selectKategori.requestFocus();
                    return;
                }else if(selectProvinsi.getText().toString().isEmpty()){
                    selectProvinsi.setError(context.getString(R.string.error_empty));
                    selectProvinsi.requestFocus();
                    return;
                }else if(selectKota.getText().toString().isEmpty()){
                    selectKota.setError(context.getString(R.string.error_empty));
                    selectKota.requestFocus();
                    return;
                }else if(selectKecamata.getText().toString().isEmpty()){
                    selectKecamata.setError(context.getString(R.string.error_empty));
                    selectKecamata.requestFocus();
                    return;
                }else if(selectDesa.getText().toString().isEmpty()){
                    selectDesa.setError(context.getString(R.string.error_empty));
                    selectDesa.requestFocus();
                    return;
                }

                if(NetworkUtils.isConnected(context)){
                    Log.e(TAG, "completed data");
                    createAccount(edtUsername.getText().toString(),
                            edtPassword.getText().toString(),
                            edtEmail.getText().toString());
                }else{
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }
        });


//
    }
    private void getProvinsi(){
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.provinsi().enqueue(new Callback<List<Provinsi>>() {
            @Override
            public void onResponse(Call<List<Provinsi>> call, Response<List<Provinsi>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        provinsiList = response.body();
                        for(int i =0; i < provinsiList.size();i++){
                            listProvinsi.add(provinsiList.get(i).getNama());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_dropdown, listProvinsi);
                        selectProvinsi.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Provinsi>> call, Throwable t) {

            }
        });
    }

//    private void getKotaKabupaten(String id){
////        adapterKota.clear();
//        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
//        service.kota(id).enqueue(new Callback<GetKotaResponse>() {
//            @Override
//            public void onResponse(Call<GetKotaResponse> call, Response<GetKotaResponse> response) {
//                if(response.isSuccessful()){
//                    if(response.body().getKotaList().size() > 0){
//                        kotaKabList = response.body().getKotaList();
//                        for(int i =0; i < kotaKabList.size();i++){
//                            listKab.add(kotaKabList.get(i).getNama());
//                        }
//                        adapterKota = new ArrayAdapter<String>(context, R.layout.item_dropdown, listKab);
//                        selectKota.setAdapter(adapterKota);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetKotaResponse> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void getKecamatan(String id){
////        adapterKecamatan.clear();
//        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
//        service.kecamatan(id).enqueue(new Callback<GetKecamatanResponse>() {
//            @Override
//            public void onResponse(Call<GetKecamatanResponse> call, Response<GetKecamatanResponse> response) {
//                if(response.isSuccessful()){
//                    if(response.body().getKecamatanList().size() > 0){
//                        kecamatanList = response.body().getKecamatanList();
//                        for(int i =0; i < kecamatanList.size();i++){
//                            listKecamatan.add(kecamatanList.get(i).getNama());
//                        }
//                        adapterKecamatan = new ArrayAdapter<String>(context, R.layout.item_dropdown, listKecamatan);
//                        selectKecamata.setAdapter(adapterKecamatan);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetKecamatanResponse> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void getKelurahan(String id){
////        adapterKelurahan.clear();
//        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
//        service.desa(id).enqueue(new Callback<GetDesaResponse>() {
//            @Override
//            public void onResponse(Call<GetDesaResponse> call, Response<GetDesaResponse> response) {
//                if(response.isSuccessful()){
//                    if(response.body().getKelurahanList().size() > 0){
//                        desaList = response.body().getKelurahanList();
//                        for(int i =0; i < desaList.size();i++){
//                            listDesa.add(desaList.get(i).getNama());
//                        }
//                        adapterKelurahan = new ArrayAdapter<String>(context, R.layout.item_dropdown, listDesa);
//                        selectDesa.setThreshold(2);
//                        selectDesa.setAdapter(adapterKelurahan);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetDesaResponse> call, Throwable t) {
//
//            }
//        });
//    }

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
                        createToko(response.body().getToken(), response.body().getUser().getId());
                        updateAccount(response.body().getToken(), response.body().getUser().getId());
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

    private void updateAccount(String token, String idUser){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_user", createPartFromString(idUser));
        map.put("nama", createPartFromString(edtNama.getText().toString().trim()));
        map.put("phone", createPartFromString(edtPhone.getText().toString().trim()));
        map.put("address", createPartFromString(edtAlamat.getText().toString().trim()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileOwner);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", imageFileOwner.getName(), reqFile);
        UserService service = ServiceGenerator.createService(UserService.class, token, null, null, null);
        service.updateOwner(body, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                   dialogSuccess("Registrasi berhasil");
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

    private void createToko(String token, String idUser){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_user", createPartFromString(idUser));
        map.put("nama", createPartFromString(edtNamaToko.getText().toString().trim()));
        map.put("alamat", createPartFromString(edtAlamatToko.getText().toString().trim()));
        map.put("kategori", createPartFromString(selectKategori.getText().toString().trim()));
        map.put("add_provinsi", createPartFromString(selectProvinsi.getText().toString().trim()));
        map.put("add_kab_kot", createPartFromString(selectKota.getText().toString().trim()));
        map.put("add_kecamatan", createPartFromString(selectKecamata.getText().toString().trim()));
        map.put("add_kel_des", createPartFromString(selectDesa.getText().toString().trim()));
        map.put("is_active", createPartFromString("1"));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileToko);

        MultipartBody.Part body = MultipartBody.Part.createFormData("logo", imageFileToko.getName(), reqFile);

        UserService service = ServiceGenerator.createService(UserService.class, token, null, null, null);
        service.createToko(body, map).enqueue(new Callback<CreateTokoResponse>() {
            @Override
            public void onResponse(Call<CreateTokoResponse> call, Response<CreateTokoResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    Log.e(TAG, response.body().getMsg());
                }else {
                    Log.e(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<CreateTokoResponse> call, Throwable t) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.CAMERA_PROFILE_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgOwner.setImageBitmap(bitmap);
                imageFileOwner = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_PROFILE_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        imgOwner.setImageBitmap(bitmap);
                        imageFileOwner = DHelper.createTempFile(context, bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if(requestCode == Constants.CAMERA_TOKO_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgToko.setImageBitmap(bitmap);
                imageFileToko = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_TOKO_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    float sizeori = DHelper.getImageSize(context, contentUri);
                    float sizemb = sizeori/(1024f * 1024f);
                    if(sizemb < 5){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                            imgToko.setImageBitmap(bitmap);
                            imageFileToko = DHelper.createTempFile(context, bitmap);

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
                dialog.cancel();
                finish();
            }
        }, 1000);
    }

    @Override
    public void updateResult(String id, String nama, String metode) {
        if(metode.equalsIgnoreCase(Constants.PROVINSI)){
            selectProvinsi.setText(nama);
            idProvinsi = Integer.parseInt(id);
        }else if(metode.equalsIgnoreCase(Constants.KABKOTA)){
            selectKota.setText(nama);
            idKota = Integer.parseInt(id);
        }else if(metode.equalsIgnoreCase(Constants.KECAMATAN)){
            selectKecamata.setText(nama);
            idKecamatan = Integer.parseInt(id);
        }else if(metode.equalsIgnoreCase(Constants.KELURAHAN)){
            selectDesa.setText(nama);
        }
    }
}