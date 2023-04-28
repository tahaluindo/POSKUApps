package com.postkudigital.app.fragment.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.CustomerAdapter;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CustomerPostResponseJson;
import com.postkudigital.app.json.GetCustomerResponseJson;
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

public class CustomerFragment extends Fragment {
    private Context context;
    private EditText search;
    private LinearLayout ladd;
    private RecyclerView recyclerView;
    private LinearLayout lempty;
    private ProgressBar progressBar;
    private User user;
    private SessionManager sessionManager;
    private CustomerAdapter adapter;
    private SwipeRefreshLayout swipe;
    public CustomerFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        search = view.findViewById(R.id.edt_search);
        ladd = view.findViewById(R.id.ladd);
        recyclerView = view.findViewById(R.id.rec_history);
        lempty = view.findViewById(R.id.lempty);
        progressBar = view.findViewById(R.id.progressBar);
        swipe = view.findViewById(R.id.swipe);

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
                showDialog(false, "", "", "", "");
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
        service.getCustomer(sessionManager.getIdToko()).enqueue(new Callback<GetCustomerResponseJson>() {
            @Override
            public void onResponse(Call<GetCustomerResponseJson> call, Response<GetCustomerResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getCustomerList().size() > 0){
                            adapter = new CustomerAdapter(context, response.body().getCustomerList(), CustomerFragment.this);
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
            public void onFailure(Call<GetCustomerResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showDialog(boolean isEdit, String id, String nama, String phone, String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_table, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView delete = dialogView.findViewById(R.id.delete);
        final EditText namaMeja = dialogView.findViewById(R.id.edittext);
        final EditText keterangan = dialogView.findViewById(R.id.edittext2);
        final EditText emailtext = dialogView.findViewById(R.id.edittext3);
        final Button submit = dialogView.findViewById(R.id.btn_submit);

        namaMeja.setHint("Nama");
        keterangan.setHint("Phone");
        emailtext.setHint("Email");
        emailtext.setVisibility(View.VISIBLE);

        if(isEdit){
            title.setText("Edit Pelanggan");
            namaMeja.setText(nama);
            keterangan.setText(phone);
            emailtext.setText(email);
            delete.setText("Hapus Pelanggan");
            delete.setVisibility(View.VISIBLE);
        }else {
            title.setText("Tambah Pelanggan");
            delete.setVisibility(View.GONE);
        }

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    update(id, namaMeja.getText().toString(), keterangan.getText().toString(), emailtext.getText().toString());
                }else {
                    submit(namaMeja.getText().toString(), keterangan.getText().toString(), emailtext.getText().toString());
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

    private void submit(String nama, String phone, String email){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("nama", createPartFromString(nama));
        map.put("phone", createPartFromString(phone));
        map.put("email", createPartFromString(email));
        map.put("toko", createPartFromString(sessionManager.getIdToko()));
        map.put("is_active", createPartFromString("1"));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.addCustomer(map).enqueue(new Callback<CustomerPostResponseJson>() {
            @Override
            public void onResponse(Call<CustomerPostResponseJson> call, Response<CustomerPostResponseJson> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, response.body().getMessage());
                    getData();
                }
            }

            @Override
            public void onFailure(Call<CustomerPostResponseJson> call, Throwable t) {

            }
        });
    }

    private void update(String id, String nama, String phone, String email){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_pelanggan", createPartFromString(id));
        map.put("nama", createPartFromString(nama));
        map.put("phone", createPartFromString(phone));
        map.put("email", createPartFromString(email));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updCustomer(map).enqueue(new Callback<CustomerPostResponseJson>() {
            @Override
            public void onResponse(Call<CustomerPostResponseJson> call, Response<CustomerPostResponseJson> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, "success");
                    getData();
                }
            }

            @Override
            public void onFailure(Call<CustomerPostResponseJson> call, Throwable t) {

            }
        });
    }

    private void deleteTable(String id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_pelanggan", createPartFromString(id));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.delCustomer(map).enqueue(new Callback<CustomerPostResponseJson>() {
            @Override
            public void onResponse(Call<CustomerPostResponseJson> call, Response<CustomerPostResponseJson> response) {
                if(response.isSuccessful()){
                    DHelper.pesan(context, "success");
                    getData();
                }
            }

            @Override
            public void onFailure(Call<CustomerPostResponseJson> call, Throwable t) {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
}