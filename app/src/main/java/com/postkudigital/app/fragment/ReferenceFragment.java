package com.postkudigital.app.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.ReferenceAdapter;
import com.postkudigital.app.helpers.ClickInterface;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.json.GetBankResponseJson;
import com.postkudigital.app.models.Bank;
import com.postkudigital.app.models.location.Kecamatan;
import com.postkudigital.app.models.location.Kelurahan;
import com.postkudigital.app.models.location.Kota;
import com.postkudigital.app.models.location.Provinsi;
import com.postkudigital.app.models.location.Reference;
import com.postkudigital.app.services.ApiLocationService;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.ReferenceService;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class ReferenceFragment extends DialogFragment {
    private Context context;
    private SessionManager sessionManager;
    private List<Reference> referenceList = new ArrayList<>();
    private ReferenceAdapter adapter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EditText search;
    private int id;
    private String metode;
    private ClickInterface clickInterface;
    private ProgressBar progressBar;
    public ReferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reference, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recycler);
        search = view.findViewById(R.id.edt_search);
        progressBar = view.findViewById(R.id.progressBar4);

        id = getArguments().getInt(Constants.ID, 0);
        metode = getArguments().getString(Constants.METHOD);

        clickInterface = new ClickInterface() {
            @Override
            public void onItemSelected(String id, String nama) {
                UpdateText updateText = (UpdateText)getActivity();
                updateText.updateResult(id, nama, metode);
                dismiss();
            }

        };

        adapter = new ReferenceAdapter(context, referenceList, clickInterface);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(metode.equalsIgnoreCase(Constants.PROVINSI)){
            toolbar.setTitle("Cari Provinsi");
            getProvinsi();
        }else if(metode.equalsIgnoreCase(Constants.KABKOTA)){
            toolbar.setTitle("Cari Kabupaten/Kota");
            getKotaKabupaten(String.valueOf(id));
        }else if(metode.equalsIgnoreCase(Constants.KECAMATAN)){
            toolbar.setTitle("Cari Kecamatan");
            getKecamatan(String.valueOf(id));
        }else if(metode.equalsIgnoreCase(Constants.KELURAHAN)){
            toolbar.setTitle("Cari Desa/Kelurahan");
            getKelurahan(String.valueOf(id));
        }else if(metode.equalsIgnoreCase(Constants.BANK)){
            toolbar.setTitle("Cari Bank");
            getBank();
        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(adapter !=null){
                    if(s.length() == 0){
                        adapter.getFilter().filter("");
                    }else {
                        adapter.getFilter().filter(s.toString());
                    }
                }
            }
        });


        return view;
    }

    private void getProvinsi(){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.provinsi().enqueue(new Callback<List<Provinsi>>() {
            @Override
            public void onResponse(Call<List<Provinsi>> call, Response<List<Provinsi>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        List<Provinsi> provinsiList = response.body();
                        for(int i =0; i < provinsiList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(provinsiList.get(i).getId()));
                            reference.setNama(provinsiList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Provinsi>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void getKotaKabupaten(String id){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.kota(id).enqueue(new Callback<List<Kota>>() {
            @Override
            public void onResponse(Call<List<Kota>> call, Response<List<Kota>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        List<Kota> kotaKabList = response.body();
                        for(int i =0; i < kotaKabList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(kotaKabList.get(i).getId()));
                            reference.setNama(kotaKabList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Kota>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getKecamatan(String id){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.kecamatan(id).enqueue(new Callback<List<Kecamatan>>() {
            @Override
            public void onResponse(Call<List<Kecamatan>> call, Response<List<Kecamatan>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        List<Kecamatan> kecamatanList = response.body();
                        for(int i =0; i < kecamatanList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(kecamatanList.get(i).getId()));
                            reference.setNama(kecamatanList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Kecamatan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getKelurahan(String id){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceService service = ApiLocationService.createService(ReferenceService.class);
        service.desa(id).enqueue(new Callback<List<Kelurahan>>() {
            @Override
            public void onResponse(Call<List<Kelurahan>> call, Response<List<Kelurahan>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        List<Kelurahan> desaList = response.body();
                        for(int i =0; i < desaList.size();i++){
                            Reference reference = new Reference();
                            reference.setId(desaList.get(i).getId());
                            reference.setNama(desaList.get(i).getNama());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Kelurahan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getBank(){
        referenceList.clear();
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.bank().enqueue(new Callback<GetBankResponseJson>() {
            @Override
            public void onResponse(Call<GetBankResponseJson> call, Response<GetBankResponseJson> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getBankList().size() > 0){
                        List<Bank> banks = response.body().getBankList();
                        for(int i =0; i < banks.size();i++){
                            Reference reference = new Reference();
                            reference.setId(String.valueOf(banks.get(i).getId()));
                            reference.setNama(banks.get(i).getName());
                            referenceList.add(reference);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBankResponseJson> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public interface UpdateText{
        void updateResult(String id, String nama, String metode);
    }
}