package com.postkudigital.app.fragment.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.AbsensiAdapter;
import com.postkudigital.app.json.GetAbsensiResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.DateRangePickerFragement;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class AbsensiFragment extends Fragment {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    private Context context;
    private SessionManager sessionManager;
    private EditText search;
    private RelativeLayout rlDate;
    private TextView textDateCompleted;
    private String date1, date2, dateStart, dateEnd;
    private RecyclerView recyclerView;
    private Calendar calendar;
    private LinearLayout lempty;
    private ProgressBar progressBar;
    private AbsensiAdapter adapter;
    private SwipeRefreshLayout swipe;
    public AbsensiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_absensi, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        search = view.findViewById(R.id.edt_search);
        rlDate = view.findViewById(R.id.rldate);
        textDateCompleted = view.findViewById(R.id.text_date);
        recyclerView = view.findViewById(R.id.rec_history);
        progressBar = view.findViewById(R.id.progressBar);
        lempty = view.findViewById(R.id.lempty);
        swipe = view.findViewById(R.id.swipe);

        calendar = Calendar.getInstance();
        date1 = formater.format(calendar.getTime());
        date2 = formater.format(calendar.getTime());
        dateStart = formatDate.format(calendar.getTime());
        dateEnd = formatDate.format(calendar.getTime());

        textDateCompleted.setText(dateStart + " - " + dateEnd);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        getData(true,  date1, date2);

        rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateRange();
            }
        });

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

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(false,  date1, date2);
                swipe.setRefreshing(false);
            }
        });

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
        Map<String, String> data = new HashMap<>();
        data.put("id_toko", sessionManager.getIdToko());
        if(!isFirst){
            data.put("date1", mDate1);
            data.put("date2", mDate2);
        }
        progressBar.setVisibility(View.VISIBLE);
        lempty.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.dataAbsen(data).enqueue(new Callback<GetAbsensiResponse>() {
            @Override
            public void onResponse(Call<GetAbsensiResponse> call, Response<GetAbsensiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getAbsensiList().size() > 0){
                            adapter = new AbsensiAdapter(context, response.body().getAbsensiList());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else {
                            lempty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAbsensiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }
}