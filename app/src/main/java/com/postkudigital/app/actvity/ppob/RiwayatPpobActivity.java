package com.postkudigital.app.actvity.ppob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.HistoryPpobAdapter;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetHistoryPpobResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.DateRangePickerFragement;
import com.postkudigital.app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatPpobActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private EditText search;
    private RelativeLayout selectDate;
    private TextView totalTrans, transSuccess, caption, tanggal;
    private RecyclerView recyclerView;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    private String date1, date2, dateStart, dateEnd;
    private Calendar calendar;
    private ProgressBar progressBar;
    private LinearLayout lempty;
    private HistoryPpobAdapter adapter;
    private SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_ppob);
        context = this;
        sessionManager = new SessionManager(context);
        calendar = Calendar.getInstance();
        tanggal = findViewById(R.id.text_date);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        caption.setText("Riwayat Transaksi PPOB");
        search = findViewById(R.id.edt_search);
        selectDate = findViewById(R.id.rldate);
        totalTrans = findViewById(R.id.text_total);
        transSuccess = findViewById(R.id.text_jumlah);
        recyclerView = findViewById(R.id.rec_history);
        progressBar = findViewById(R.id.progressBar);
        lempty = findViewById(R.id.lempty);
        swipe = findViewById(R.id.swipe);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        //        set data
        date1 = formater.format(calendar.getTime());
        date2 = formater.format(calendar.getTime());
        dateStart = formatDate.format(calendar.getTime());
        dateEnd = formatDate.format(calendar.getTime());
        tanggal.setText(dateStart + " - " + dateEnd);

        getData(true, date1, date2);

        selectDate.setOnClickListener(new View.OnClickListener() {
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
                getData(false, date1, date2);
                swipe.setRefreshing(false);
            }
        });

    }

    private void getData(boolean isFirst, String mDate1, String mDate2){
        progressBar.setVisibility(View.VISIBLE);
        lempty.setVisibility(View.GONE);
        Map<String, String> data = new HashMap<>();
        data.put("id_toko", sessionManager.getIdToko());
        if(!isFirst){
            data.put("date1", mDate1);
            data.put("date2", mDate2);
        }
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.historyPppob(data).enqueue(new Callback<GetHistoryPpobResponse>() {
            @Override
            public void onResponse(Call<GetHistoryPpobResponse> call, Response<GetHistoryPpobResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        totalTrans.setText("Rp" + DHelper.toformatRupiah(String.valueOf(response.body().getTotalTransaksi())));
                        transSuccess.setText(DHelper.toformatRupiah(String.valueOf(response.body().getJumlahTransaksi())));
                        if(response.body().getTransPpobList().isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            lempty.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new HistoryPpobAdapter(context, response.body().getTransPpobList());
                            recyclerView.setAdapter(adapter);
                        }
                    }else {
                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetHistoryPpobResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
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
                tanggal.setText(dateStart + " - " + dateEnd);
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
        pickerFragement.show(getSupportFragmentManager(), "SUBLIME_PICKER");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}