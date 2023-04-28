package com.postkudigital.app.fragment.toko;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.TokoAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetOutletResponseJson;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokoFragment extends Fragment {
    private Context context;
    private TokoAdapter adapter;
    private EditText search;
    private LinearLayout ladd;
    private RecyclerView recyclerView;
    private LinearLayout lempty;
    private ProgressBar progressBar;
    private User user;
    private SessionManager sessionManager;
    private SwipeRefreshLayout swipe;
    public TokoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toko, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
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
                if(adapter != null){
                    if(s.length() > 0){
                        adapter.getFilter().filter(s.toString());
                    }
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
                Intent intent = new Intent(context, DetailTokoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(Constants.METHOD, Constants.ADD);
                startActivity(intent);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getToko();
                swipe.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getToko();
    }
    private void getToko(){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getToko(user.getId()).enqueue(new Callback<GetOutletResponseJson>() {
            @Override
            public void onResponse(Call<GetOutletResponseJson> call, Response<GetOutletResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode().equalsIgnoreCase("200")){
                        if(response.body().getTokoList().isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new TokoAdapter(context, response.body().getTokoList());
                            recyclerView.setAdapter(adapter);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMsg());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetOutletResponseJson> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(Constants.TAG, t.getMessage());
            }
        });
    }
}