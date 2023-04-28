package com.postkudigital.app.fragment.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.RankingAdapter;
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

public class RankingItemFragment extends Fragment {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rangking_trans, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        rlDate = view.findViewById(R.id.rldate);
        textDateCompleted = view.findViewById(R.id.text_date);
        nama = view.findViewById(R.id.text_nama);
        qty = view.findViewById(R.id.text_qty);
        topRanking = view.findViewById(R.id.topranking);
        recyclerView = view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progressBar);

        calendar = Calendar.getInstance();
        date1 = formater.format(calendar.getTime());
        date2 = formater.format(calendar.getTime());
        dateStart = formatDate.format(calendar.getTime());
        dateEnd = formatDate.format(calendar.getTime());

        textDateCompleted.setText(dateStart + " - " + dateEnd);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateRange();
            }
        });

        getData(true, date1, date2);

        return view;
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
                getData(false, date1, date2);

            }
        });

        SublimeOptions options = new SublimeOptions();
        options.setCanPickDateRange(true);
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", options);
        pickerFragement.setArguments(bundle);
        pickerFragement.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFragement.show(getChildFragmentManager(), "SUBLIME_PICKER");
    }

    private void getData(boolean isFirst, String mDate1, String mDate2){
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
        service.reportdisc(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String msg = object.getString("msg");
                        String status = object.getString("status_code");
                        if(status.equalsIgnoreCase("200")){
                            JSONObject jsonObject = object.getJSONObject("data");
                            JSONArray jsonArray = jsonObject.getJSONArray("discount_item");
                            if(jsonArray.length() > 0){
                                for (int x=0;x < jsonArray.length();x++){
                                    JSONObject jobject = jsonArray.getJSONObject(x);
                                    Ranking ranking = new Ranking();
                                    ranking.setNama(jobject.getString("discount__nama"));
                                    ranking.setQty(jobject.getInt("total_trx"));
                                    rankingList.add(ranking);
                                }
                                topRanking.setVisibility(View.VISIBLE);
                                nama.setText(rankingList.get(0).getNama());
                                qty.setText(rankingList.get(0).getQty() + "");

                                List<Ranking> secondList = new ArrayList<>();
                                for(int i=0;i < rankingList.size();i++){
                                    if(i > 0){
                                        Ranking ranking = new Ranking();
                                        ranking.setNama(rankingList.get(i).getNama());
                                        ranking.setQty(rankingList.get(i).getQty());
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
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
