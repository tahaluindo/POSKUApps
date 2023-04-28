package com.postkudigital.app.fragment.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.RankingAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.models.Ranking;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.DateRangePickerFragement;
import com.postkudigital.app.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private SessionManager sessionManager;
    private RelativeLayout rlDate, topRanking;
    private RecyclerView recyclerView;
    private String date1, date2, dateStart, dateEnd;
    private TextView textDateCompleted, nama, qty;
    private Calendar calendar;
    private RankingAdapter adapter;
    private ProgressBar progressBar;
    private List<Ranking> rankingList = new ArrayList<>();
    private String metode = "";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        context = this;
        sessionManager = new SessionManager(context);
        rlDate = findViewById(R.id.rldate);
        textDateCompleted = findViewById(R.id.text_date);
        nama = findViewById(R.id.text_nama);
        qty = findViewById(R.id.text_qty);
        topRanking = findViewById(R.id.topranking);
        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        calendar = Calendar.getInstance();
        date1 = formater.format(calendar.getTime());
        date2 = formater.format(calendar.getTime());
        dateStart = formatDate.format(calendar.getTime());
        dateEnd = formatDate.format(calendar.getTime());

        textDateCompleted.setText(dateStart + " - " + dateEnd);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        metode = getIntent().getStringExtra(Constants.METHOD);
        toolbar.setTitle(metode);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateRange();
            }
        });

        getData(true, metode, date1, date2);

    }

    private void selectDateRange(){
        DateRangePickerFragement pickerFragement = new DateRangePickerFragement();
        pickerFragement.setCallback(new DateRangePickerFragement.Callback() {
            @Override
            public void onCancelled() {

            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                date1 = formater.format(selectedDate.getStartDate().getTime());
                date2 = formater.format(selectedDate.getEndDate().getTime());
                dateStart = formatDate.format(selectedDate.getStartDate().getTime());
                dateEnd = formatDate.format(selectedDate.getEndDate().getTime());
                textDateCompleted.setText(dateStart + " - " + dateEnd);
                getData(false, metode, date1, date2);

            }
        });

        SublimeOptions options = new SublimeOptions();
        options.setCanPickDateRange(true);
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", options);
        pickerFragement.setArguments(bundle);
        pickerFragement.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFragement.show(getSupportFragmentManager(), "SUBLIME_PICKER");
    }

    private void getData(boolean isFirst, String method, String mDate1, String mDate2){
        rankingList.clear();
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> data = new HashMap<>();
        data.put("id_toko", sessionManager.getIdToko());
        if(!isFirst){
            data.put("date1", mDate1);
            data.put("date2", mDate2);
        }
        UserService service = ServiceGenerator.createService(UserService.class,
                sessionManager.getToken(), null, null, null);
        if(method.equalsIgnoreCase(Constants.LAP_MENU)){
            service.reportmenu(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if(status.equalsIgnoreCase("200")){
                                JSONArray jsonArray = object.getJSONArray("data");
                                if(jsonArray.length() > 0){
                                    for(int i=0;i < jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("menu__nama"));
                                        ranking.setQty(jsonObject.getInt("qty"));
                                        rankingList.add(ranking);
                                    }

                                    topRanking.setVisibility(View.VISIBLE);
                                    nama.setText(rankingList.get(0).getNama());
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for(int x=0;x < rankingList.size();x++){
                                        if(x > 0){
                                            Ranking ranking = new Ranking();
                                            ranking.setNama(rankingList.get(x).getNama());
                                            ranking.setQty(rankingList.get(x).getQty());
                                            secondList.add(ranking);
                                        }
                                    }
                                    if(secondList.size() > 0){
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }else {
                                        recyclerView.setVisibility(View.GONE);
                                    }


                                }else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            }else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if(method.equalsIgnoreCase(Constants.LAP_STAFF)){
            service.reportemployee(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if(status.equalsIgnoreCase("200")){
                                JSONArray jsonArray = object.getJSONArray("data");
                                if(jsonArray.length() > 0){
                                    for(int i=0;i < jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("pegawai__nama"));
                                        ranking.setQty(jsonObject.getInt("jumlah_trx"));
                                        rankingList.add(ranking);
                                    }
                                    topRanking.setVisibility(View.VISIBLE);
                                    nama.setText(rankingList.get(0).getNama());
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for(int x=0;x < rankingList.size();x++){
                                        if(x > 0){
                                            Ranking ranking = new Ranking();
                                            ranking.setNama(rankingList.get(x).getNama());
                                            ranking.setQty(rankingList.get(x).getQty());
                                            secondList.add(ranking);
                                        }
                                    }
                                    if(secondList.size() > 0){
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            }else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if(method.equalsIgnoreCase(Constants.LAP_KATEGORI)){
            service.reportkategori(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if(status.equalsIgnoreCase("200")){
                                JSONArray jsonArray = object.getJSONArray("data");
                                if(jsonArray.length() > 0){
                                    for(int i=0;i < jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("menu_kategori__label"));
                                        ranking.setQty(jsonObject.getInt("qty"));
                                        rankingList.add(ranking);
                                    }
                                    topRanking.setVisibility(View.VISIBLE);
                                    String namacat="";
                                    if(rankingList.get(0).getNama().equalsIgnoreCase("null")){
                                        namacat = "Menu Tanpa Kategori";
                                    }else {
                                        namacat = rankingList.get(0).getNama();
                                    }

                                    nama.setText(namacat);
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for(int x=0;x < rankingList.size();x++){

                                        if(rankingList.get(x).getNama().equalsIgnoreCase("null")){
                                            namacat = "Menu Tanpa Kategori";
                                        }else {
                                            namacat = rankingList.get(x).getNama();
                                        }

                                       if(x > 0){

                                           Ranking ranking = new Ranking();
                                           ranking.setNama(namacat);
                                           ranking.setQty(rankingList.get(x).getQty());
                                           secondList.add(ranking);
                                       }
                                    }
                                    if(secondList.size() > 0){
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            }else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if(method.equalsIgnoreCase(Constants.LAP_MEJA)){
            service.reporttable(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if(status.equalsIgnoreCase("200")){
                                JSONArray jsonArray = object.getJSONArray("data");
                                if(jsonArray.length() > 0){
                                    for(int i=0;i < jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("table__nama"));
                                        ranking.setQty(jsonObject.getInt("jumlah_trx"));
                                        rankingList.add(ranking);
                                    }
                                    topRanking.setVisibility(View.VISIBLE);
                                    nama.setText(rankingList.get(0).getNama());
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for(int x=0;x < rankingList.size();x++){
                                       if(x > 0) {
                                            Ranking ranking = new Ranking();
                                            ranking.setNama(rankingList.get(x).getNama());
                                            ranking.setQty(rankingList.get(x).getQty());
                                            secondList.add(ranking);
                                        }
                                    }
                                    if(secondList.size() > 0){
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            }else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if(method.equalsIgnoreCase(Constants.LAP_PELANGGAN)) {
            service.reportpelanggan(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("pelanggan__nama"));
                                        ranking.setQty(jsonObject.getInt("jumlah_trx"));
                                        rankingList.add(ranking);
                                    }
                                    topRanking.setVisibility(View.VISIBLE);
                                    nama.setText(rankingList.get(0).getNama());
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for (int x = 0; x < rankingList.size(); x++) {
                                        if(x > 0){
                                            Ranking ranking = new Ranking();
                                            ranking.setNama(rankingList.get(x).getNama());
                                            ranking.setQty(rankingList.get(x).getQty());
                                            secondList.add(ranking);
                                        }
                                    }
                                    if (secondList.size() > 0) {
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                } else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            } else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if(method.equalsIgnoreCase(Constants.LAP_TIPE_ORDER)) {
            service.reporttipe(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("tipe_order__nama"));
                                        ranking.setQty(jsonObject.getInt("jumlah_trx"));
                                        rankingList.add(ranking);
                                    }
                                    topRanking.setVisibility(View.VISIBLE);
                                    nama.setText(rankingList.get(0).getNama());
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for (int x = 0; x < rankingList.size(); x++) {
                                        if(x > 0){
                                            Ranking ranking = new Ranking();
                                            ranking.setNama(rankingList.get(x).getNama());
                                            ranking.setQty(rankingList.get(x).getQty());
                                            secondList.add(ranking);
                                        }
                                    }
                                    if (secondList.size() > 0) {
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                } else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            } else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if(method.equalsIgnoreCase(Constants.LAP_LABEL_ORDER)) {
            service.reportlabel(data).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            String msg = object.getString("msg");
                            String status = object.getString("status_code");

                            if (status.equalsIgnoreCase("200")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(jsonObject.getString("label_order__nama"));
                                        ranking.setQty(jsonObject.getInt("jumlah_trx"));
                                        rankingList.add(ranking);
                                    }
                                    topRanking.setVisibility(View.VISIBLE);
                                    nama.setText(rankingList.get(0).getNama());
                                    qty.setText(rankingList.get(0).getQty() + "");

                                    List<Ranking> secondList = new ArrayList<>();
                                    for (int x = 0; x < rankingList.size(); x++) {
                                        if(x > 0){
                                            Ranking ranking = new Ranking();
                                            ranking.setNama(rankingList.get(x).getNama());
                                            ranking.setQty(rankingList.get(x).getQty());
                                            secondList.add(ranking);
                                        }
                                    }
                                    if (secondList.size() > 0) {
                                        adapter = new RankingAdapter(context, secondList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                } else {
                                    topRanking.setVisibility(View.GONE);
                                }
                            } else {
                                topRanking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        topRanking.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}