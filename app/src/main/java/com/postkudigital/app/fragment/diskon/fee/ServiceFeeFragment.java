package com.postkudigital.app.fragment.diskon.fee;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.ServiceFeeAdapter;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetServiceResponseJson;
import com.postkudigital.app.json.ServicePostResponseJson;
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

public class ServiceFeeFragment extends Fragment {
    private Context context;
    private EditText search;
    private LinearLayout ladd;
    private RecyclerView recyclerView;
    private LinearLayout lempty;
    private ProgressBar progressBar;
    private User user;
    private SessionManager sessionManager;
    private ServiceFeeAdapter adapter;
    private SwipeRefreshLayout swipe;
    public ServiceFeeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        search = view.findViewById(R.id.edt_search);
        ladd = view.findViewById(R.id.ladd);
        recyclerView = view.findViewById(R.id.rec_history);
        lempty = view.findViewById(R.id.lempty);
        progressBar = view.findViewById(R.id.progressBar2);
        swipe = view.findViewById(R.id.swipe);

        search.setHint("Cari Biaya layanan");

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(adapter != null){
                    if(s.length() == 0){
                        adapter.getFilter().filter("");
                    }else {
                        adapter.getFilter().filter(s.toString());
                    }
                }
            }
        });

        ladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(false, "", "", "");
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipe.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        lempty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getServiceFee(sessionManager.getIdToko()).enqueue(new Callback<GetServiceResponseJson>() {
            @Override
            public void onResponse(Call<GetServiceResponseJson> call, Response<GetServiceResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getServiceFees().size() > 0){
                            adapter = new ServiceFeeAdapter(context, response.body().getServiceFees(), ServiceFeeFragment.this);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }else {
                            lempty.setVisibility(View.VISIBLE);
                        }
                    }else{
                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetServiceResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showDialog(boolean isEdit, String id, String nama, String note){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_promo, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView delete = dialogView.findViewById(R.id.delete);
        final EditText namaPromo = dialogView.findViewById(R.id.edittext);
        final EditText nominal = dialogView.findViewById(R.id.edittext2);
        final RadioGroup metode = dialogView.findViewById(R.id.method);
        final RadioButton rbFixed = dialogView.findViewById(R.id.rbTunai);
        final RadioButton rbPercent = dialogView.findViewById(R.id.rbQris);
        final Button submit = dialogView.findViewById(R.id.btn_submit);
        metode.setVisibility(View.GONE);
        namaPromo.setHint("Nama Biaya Layanan");
        delete.setText("Hapus Biaya Layanan");
        if(isEdit){
            title.setText("Edit Biaya Layanan");
            namaPromo.setText(nama);
            nominal.setText(note);
            delete.setVisibility(View.VISIBLE);
        }else {
            title.setText("Tambah Biaya Layanan");
            delete.setVisibility(View.GONE);
        }

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    update(id, namaPromo.getText().toString(), nominal.getText().toString());
                }else {
                    submit(namaPromo.getText().toString(), nominal.getText().toString());
                }

                alertDialog.dismiss();
            }
        });

        if(isEdit){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTable(id);
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void submit(String nama, String nominal){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("nama", createPartFromString(nama));
        map.put("nominal", createPartFromString(nominal));
        map.put("toko", createPartFromString(sessionManager.getIdToko()));
        map.put("is_active", createPartFromString("1"));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.addServiceFee(map).enqueue(new Callback<ServicePostResponseJson>() {
            @Override
            public void onResponse(Call<ServicePostResponseJson> call, Response<ServicePostResponseJson> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, response.body().getMessage());
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ServicePostResponseJson> call, Throwable t) {

            }
        });
    }

    private void update(String id, String nama, String nominal){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_service_fee", createPartFromString(id));
        map.put("nama", createPartFromString(nama));
        map.put("nominal", createPartFromString(nominal));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updServiceFee(map).enqueue(new Callback<ServicePostResponseJson>() {
            @Override
            public void onResponse(Call<ServicePostResponseJson> call, Response<ServicePostResponseJson> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, response.body().getMessage());
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ServicePostResponseJson> call, Throwable t) {

            }
        });
    }

    private void deleteTable(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_service_fee", createPartFromString(id));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.delServiceFee(map).enqueue(new Callback<ServicePostResponseJson>() {
            @Override
            public void onResponse(Call<ServicePostResponseJson> call, Response<ServicePostResponseJson> response) {
                if(response.isSuccessful()){
                    getData();
                }
            }

            @Override
            public void onFailure(Call<ServicePostResponseJson> call, Throwable t) {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
}
