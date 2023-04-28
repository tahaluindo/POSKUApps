package com.postkudigital.app.fragment.product.menu;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.ActiveStockResponse;
import com.postkudigital.app.json.DetailMenuResponse;
import com.postkudigital.app.json.GetKategoriResponseJson;
import com.postkudigital.app.json.KonfirmTopupResponseJson;
import com.postkudigital.app.json.PostMenuResponse;
import com.postkudigital.app.models.Kategori;
import com.postkudigital.app.models.Menus;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

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

public class ManageMenuActivity extends AppCompatActivity {
    private Context context;
    private User user;
    private SessionManager sessionManager;
    private TextView caption, hapus;
    private ImageView back;
    private RelativeLayout rlimage;
    private EditText edtNama, edtDeskripsi, edtHarga, edtModal;
    private AutoCompleteTextView selectKategori;
    private Switch aSwitch;
    private Button submit;
    private ImageView imageView;
    private int id= 0;
    ArrayList<String> stringArrayList = new ArrayList<>();
    List<Kategori> kategoriList = new ArrayList<>();
    File imageFileMenu;
    private int idkategori = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        caption = findViewById(R.id.text_caption);
        back = findViewById(R.id.back_button);
        rlimage = findViewById(R.id.rlimage);
        edtNama = findViewById(R.id.edt_nama_produk);
        edtDeskripsi = findViewById(R.id.edt_deskripsi);
        edtHarga = findViewById(R.id.edt_harga);
        edtModal = findViewById(R.id.edt_harga_modal);
        selectKategori = findViewById(R.id.kategori);
        aSwitch = findViewById(R.id.switch1);
        submit = findViewById(R.id.btn_submit);
        hapus = findViewById(R.id.text_delete);
        imageView = findViewById(R.id.img_produk);

        if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
            getData();
            caption.setText("Tambah Menu");
            hapus.setVisibility(View.GONE);
        }else {
            caption.setText("Edit Menu");
            id = getIntent().getIntExtra(Constants.ID, 0);
            hapus.setVisibility(View.VISIBLE);
            detail(String.valueOf(id));

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectKategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Kategori k = kategoriList.get(position);
                idkategori = k.getId();
            }
        });

        rlimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ManageMenuActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_CAMERA_CODE);
                    return;

                }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ManageMenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ_DATA);
                    return;
                }

                dialogImagePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNama.getText().toString().isEmpty()){
                    edtNama.setError(context.getString(R.string.error_empty));
                    edtNama.requestFocus();
                    return;
                }else if(edtDeskripsi.getText().toString().isEmpty()){
                    edtDeskripsi.setError(context.getString(R.string.error_empty));
                    edtDeskripsi.requestFocus();
                    return;
                }else if(edtHarga.getText().toString().isEmpty()){
                    edtHarga.setError(context.getString(R.string.error_empty));
                    edtHarga.requestFocus();
                    return;
                }else if(edtModal.getText().toString().isEmpty()){
                    edtModal.setError(context.getString(R.string.error_empty));
                    edtModal.requestFocus();
                    return;
                }

                if(getIntent().getStringExtra(Constants.METHOD).equalsIgnoreCase(Constants.ADD)){
                    addMenu();
                }else {
                    editMenu(String.valueOf(id));
                }
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMenu(String.valueOf(id));
            }
        });

    }

    private void dialogImagePicker(){
        String[]items = {"Kamera", "Galery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ambil gambar");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getData(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getKategori(sessionManager.getIdToko()).enqueue(new Callback<GetKategoriResponseJson>() {
            @Override
            public void onResponse(Call<GetKategoriResponseJson> call, Response<GetKategoriResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getKategoriList().size() > 0){
                        kategoriList = response.body().getKategoriList();
                        for(int i =0; i < kategoriList.size();i++){
                            stringArrayList.add(kategoriList.get(i).getLabel());
                        }
                        ArrayAdapter<String> adapterKecamatan = new ArrayAdapter<String>(context, R.layout.item_dropdown, stringArrayList);
                        selectKategori.setAdapter(adapterKecamatan);
                        selectKategori.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectKategori.showDropDown();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKategoriResponseJson> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void detail(String id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailMenu(id).enqueue(new Callback<DetailMenuResponse>() {
            @Override
            public void onResponse(Call<DetailMenuResponse> call, Response<DetailMenuResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Menus menus = response.body().getMenus();
                        edtNama.setText(menus.getNama().toString());
                        edtDeskripsi.setText(menus.getDescription().toString());
                        edtHarga.setText(DHelper.toformatRupiah(String.valueOf(menus.getHarga())));
                        edtModal.setText(DHelper.toformatRupiah(String.valueOf(menus.getHargaModal())));
                        imageView.setDrawingCacheEnabled(true);
                        Glide.with(context)
                                .load(menus.getImage())
                                .placeholder(R.drawable.image_placeholder)
                                .into(imageView);
                        if(menus.getKategori() != null){
                            selectKategori.setText(menus.getKategori().getLabel());
                            idkategori = menus.getKategori().getId();
                        }

                        Bitmap bitmap = imageView.getDrawingCache();
                        imageFileMenu = DHelper.createTempFile(context, bitmap);
                        if(response.body().getStock() != null){
                            aSwitch.setChecked(true);
                        }else {
                            aSwitch.setChecked(false);
                        }
                        getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailMenuResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void addMenu(){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("toko", createPartFromString(sessionManager.getIdToko()));
        map.put("nama", createPartFromString(edtNama.getText().toString()));
        map.put("harga", createPartFromString(edtHarga.getText().toString()
                .replaceAll(",","")
                .replaceAll("\\.","")));
        map.put("desc", createPartFromString(edtDeskripsi.getText().toString()));
        if(idkategori > 0){
            map.put("kategori", createPartFromString(String.valueOf(idkategori)));
        }
        
        map.put("harga_modal", createPartFromString(edtModal.getText().toString()
                .replaceAll(",","")
                .replaceAll("\\.","")));
        map.put("is_active", createPartFromString("1"));
        MultipartBody.Part body = null;
        if(imageFileMenu !=null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileMenu);
            body = MultipartBody.Part.createFormData("menu_pic", imageFileMenu.getName(), reqFile);
        }

        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.addMenu(body, map).enqueue(new Callback<PostMenuResponse>() {
            @Override
            public void onResponse(Call<PostMenuResponse> call, Response<PostMenuResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 201){
                        DHelper.pesan(context, response.body().getMessage());
                        if(aSwitch.isChecked()){
                            activeStock(String.valueOf(response.body().getMenus().getId()));
                        }
                        finish();

                    }
                }
                finish();
            }

            @Override
            public void onFailure(Call<PostMenuResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void editMenu(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_menu", createPartFromString(id));
        map.put("nama", createPartFromString(edtNama.getText().toString()));
        map.put("harga", createPartFromString(edtHarga.getText().toString()
                .replaceAll(",","")
                .replaceAll("\\.","")));
        map.put("desc", createPartFromString(edtDeskripsi.getText().toString()));
        if(idkategori > 0){
            map.put("kategori_id", createPartFromString(String.valueOf(idkategori)));
        }

        map.put("harga_modal", createPartFromString(edtModal.getText().toString()
                .replaceAll(",","")
                .replaceAll("\\.","")));
        MultipartBody.Part body = null;
        if(imageFileMenu !=null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileMenu);
            body = MultipartBody.Part.createFormData("menu_pic", imageFileMenu.getName(), reqFile);
        }
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.editMenu(body, map).enqueue(new Callback<PostMenuResponse>() {
            @Override
            public void onResponse(Call<PostMenuResponse> call, Response<PostMenuResponse> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, "Success");
                    if(aSwitch.isChecked()){
                        activeStock(id);
                    }else{
                        deleteStock(id);
                    }

                    finish();

                }
            }

            @Override
            public void onFailure(Call<PostMenuResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void activeStock(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("menu", createPartFromString(id));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.activeStock(map).enqueue(new Callback<ActiveStockResponse>() {
            @Override
            public void onResponse(Call<ActiveStockResponse> call, Response<ActiveStockResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200);{
                        Log.e(TAG, response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<ActiveStockResponse> call, Throwable t) {

            }
        });
    }

    private void deleteStock(String id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.deleteStock(id).enqueue(new Callback<KonfirmTopupResponseJson>() {
            @Override
            public void onResponse(Call<KonfirmTopupResponseJson> call, Response<KonfirmTopupResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Log.e(TAG, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<KonfirmTopupResponseJson> call, Throwable t) {

            }
        });
    }

    private void deleteMenu(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_menu", createPartFromString(id));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.deleteMenu(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == Constants.CAMERA_PROFILE_REQUEST){
                Log.e(TAG, "sini");
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.destroyDrawingCache();
                imageView.setImageBitmap(bitmap);
                imageFileMenu = DHelper.createTempFile(context, bitmap);
            }else if(requestCode == Constants.GALERY_PROFILE_REQUEST){
                if(data != null){
                    Uri contentUri = data.getData();
                    float sizeori = DHelper.getImageSize(context, contentUri);
                    float sizemb = sizeori/(1024f * 1024f);
                    Log.e(TAG, "size" + sizemb);
                    if(sizemb < 5){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                            Glide.with(context)
                                    .load(contentUri)
                                    .dontAnimate()
                                    .placeholder(R.drawable.image_placeholder)
                                    .into(imageView);
                            imageFileMenu = DHelper.createTempFile(context, bitmap);

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
}