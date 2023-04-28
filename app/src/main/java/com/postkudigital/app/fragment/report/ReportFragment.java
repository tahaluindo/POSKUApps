package com.postkudigital.app.fragment.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.bumptech.glide.Glide;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetReportResponseJson;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.DateRangePickerFragement;
import com.postkudigital.app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment {
    private Context context;
    private SessionManager sessionManager;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
    private RelativeLayout rlranking;
    private CircleImageView circleImageView;
    private RelativeLayout selectDate;
    private TextView textDateCompleted, textNamaToko, textKotor, textPajak, textServiceFee,
    textDiskon, textCanceled, textTotalItems, textHpp, textLabaRugi;
    private String date1, date2, dateStart, dateEnd;
    private Calendar calendar;
    private User user;
    public ReportFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        calendar = Calendar.getInstance();

        rlranking = view.findViewById(R.id.rlranking);
        circleImageView = view.findViewById(R.id.userphoto);
        selectDate = view.findViewById(R.id.rldate);
        textDateCompleted = view.findViewById(R.id.text_date);
        textNamaToko = view.findViewById(R.id.text_toko);
        textKotor = view.findViewById(R.id.text_kotor);
        textPajak = view.findViewById(R.id.text_pajak);
        textServiceFee = view.findViewById(R.id.text_service_fee);
        textDiskon = view.findViewById(R.id.text_diskon);
        textCanceled = view.findViewById(R.id.text_cancel);
        textHpp = view.findViewById(R.id.text_hpp);
        textTotalItems = view.findViewById(R.id.text_total_item);
        textLabaRugi = view.findViewById(R.id.text_labarugi);

        textNamaToko.setText(sessionManager.getNamaToko());
        Glide.with(context)
                .load(sessionManager.getLogoToko())
                .placeholder(R.drawable.image_placeholder)
                .into(circleImageView);

//        set data
        date1 = formater.format(calendar.getTime());
        date2 = formater.format(calendar.getTime());
        dateStart = formatDate.format(calendar.getTime());
        dateEnd = formatDate.format(calendar.getTime());
        textDateCompleted.setText(dateStart + " - " + dateEnd);
        
        getData(true, date1, date2);

        if(user.isSubs()){
            rlranking.setVisibility(View.VISIBLE);
        }else {
            rlranking.setVisibility(View.GONE);
        }

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateRange();
            }
        });

        rlranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReportRankingActivity.class);
                startActivity(intent);
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
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.laporan(data).enqueue(new Callback<GetReportResponseJson>() {
            @Override
            public void onResponse(Call<GetReportResponseJson> call, Response<GetReportResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getCode() == 200){
                        textKotor.setText(DHelper.formatRupiah(response.body().getDataPenjualanKotor()));
                        textPajak.setText(DHelper.formatRupiah(response.body().getDataPajak()));
                        textServiceFee.setText(DHelper.formatRupiah(response.body().getDataServiceFee()));
                        textDiskon.setText(DHelper.formatRupiah(response.body().getDataDiscount()));
                        textCanceled.setText(DHelper.toformatRupiah(String.valueOf(response.body().getDataCancelTrx())));
                        textHpp.setText(DHelper.formatRupiah(response.body().getDataHpp()));
                        textTotalItems.setText(DHelper.toformatRupiah(String.valueOf(response.body().getDataTotalItem())));
                        textLabaRugi.setText(DHelper.formatRupiah(response.body().getDataLabaRugi()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReportResponseJson> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}