package com.postkudigital.app.fragment.pos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.SelectAdapter;
import com.postkudigital.app.helpers.AddOnClickListener;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetCustomerResponseJson;
import com.postkudigital.app.json.GetPromoResponseJson;
import com.postkudigital.app.json.GetServiceAddResponse;
import com.postkudigital.app.json.GetServiceResponseJson;
import com.postkudigital.app.json.GetTaxResponseJson;
import com.postkudigital.app.json.GetTipeOrderResponse;
import com.postkudigital.app.json.InsertItemResponse;
import com.postkudigital.app.json.UpdCartItemRequest;
import com.postkudigital.app.models.ServiceAdd;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class SelectAddFragment extends DialogFragment {
    private Context context;
    private SessionManager sessionManager;
    private SelectAdapter adapter;
    private RecyclerView recyclerView;
    private AddOnClickListener clickInterface;
    private ImageView backButton;
    private TextView caption;
    private Button btnDelete, btnSelect;
    private String title;
    private List<ServiceAdd> dataList = new ArrayList<>();
    private Realm realm;
    public SelectAddFragment() {
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
        View view = inflater.inflate(R.layout.fragment_select_add, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);

        recyclerView = view.findViewById(R.id.rec_table);
        backButton = view.findViewById(R.id.back_button);
        caption = view.findViewById(R.id.text_caption);
        btnDelete = view.findViewById(R.id.button6);
        btnSelect = view.findViewById(R.id.button8);

        title = getArguments().getString(Constants.NAMA);
        caption.setText(title);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.DISKON)) {
            dataDiskon();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.DISKON_ITEM)){
            dataDiskon();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.CUSTOMER)) {
            dataCustomer();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.TIPE_ORDER)) {
            dataTipeOrder();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.LABEL_ORDER)) {
            dataLabelOrder();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.PAJAK)) {
            dataPajak();
        }else if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.SERVICE_CHARGE)){
            dataService();
        }

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.SERVICE_CHARGE)){
                    if(adapter != null){
                        List<Integer> serviceFee = adapter.getSelectedItems();
                        sessionManager.saveServiceFee(serviceFee, "servicefee");
                        UpdateText updateText = (UpdateText) getActivity();
                        updateText.updateResult(getArguments().getString(Constants.METHOD),
                                adapter.getSelectedItem(), adapter.getSelectedName(), adapter.getSelectedType(), adapter.getTotalServiceFee());
                        dismiss();
                    }
                }else {
                    if(adapter != null){
                        if(getArguments().getString(Constants.METHOD).equalsIgnoreCase(Constants.DISKON_ITEM)){
                            int idcartitem = getArguments().getInt(Constants.ID, 0);
                            int qtyitem = getArguments().getInt(Constants.INTENT_DATA, 0);
                            updCartItwm(idcartitem, qtyitem, adapter.getSelectedItem());
                            dismiss();
                        }else {
                            UpdateText updateText = (UpdateText) getActivity();
                            updateText.updateResult(getArguments().getString(Constants.METHOD),
                                    adapter.getSelectedItem(), adapter.getSelectedName(), adapter.getSelectedType(), adapter.getSelectedValue());
                            dismiss();
                        }

                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.delAddOn(getArguments().getString(Constants.METHOD));
                sessionManager.delAddOn("ID_" + getArguments().getString(Constants.METHOD));
                UpdateText updateText = (UpdateText) getActivity();
                updateText.updateResult(getArguments().getString(Constants.METHOD),
                        0, adapter.getSelectedName(), adapter.getSelectedType(), adapter.getSelectedValue());
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void dataDiskon(){
        dataList.clear();
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getPromo(sessionManager.getIdToko()).enqueue(new Callback<GetPromoResponseJson>() {
            @Override
            public void onResponse(Call<GetPromoResponseJson> call, Response<GetPromoResponseJson> response) {

                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getPromoList().size() > 0){
                            for(int i=0;i < response.body().getPromoList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getPromoList().get(i).getId());
                                serviceAdd.setNama(response.body().getPromoList().get(i).getNama());
                                serviceAdd.setType(response.body().getPromoList().get(i).getType());
                                serviceAdd.setNominal(response.body().getPromoList().get(i).getNominal());
                                if(sessionManager.getDiscount() != null && sessionManager.getIdDiscount() > 0){
                                    if(sessionManager.getIdDiscount() == response.body().getPromoList().get(i).getId()){
                                        serviceAdd.setChecked(true);
                                    }

                                }
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false, getArguments().getString(Constants.METHOD));
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{

                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetPromoResponseJson> call, Throwable t) {

                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataCustomer(){
        dataList.clear();
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getCustomer(sessionManager.getIdToko()).enqueue(new Callback<GetCustomerResponseJson>() {
            @Override
            public void onResponse(Call<GetCustomerResponseJson> call, Response<GetCustomerResponseJson> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getCustomerList().size() > 0){
                            for(int i=0;i < response.body().getCustomerList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getCustomerList().get(i).getId());
                                serviceAdd.setNama(response.body().getCustomerList().get(i).getNama());
                                serviceAdd.setType(0);
                                serviceAdd.setNominal(0);
                                if(sessionManager.getPelanggan() != null && sessionManager.getIdPelanggan() > 0){
                                    if(sessionManager.getIdPelanggan() == response.body().getCustomerList().get(i).getId()){
                                        serviceAdd.setChecked(true);
                                    }

                                }
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false, getArguments().getString(Constants.METHOD));
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetCustomerResponseJson> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataTipeOrder(){
        dataList.clear();
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.tipeorder().enqueue(new Callback<GetTipeOrderResponse>() {
            @Override
            public void onResponse(Call<GetTipeOrderResponse> call, Response<GetTipeOrderResponse> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getDataList().size() > 0){
                            for(int i=0;i < response.body().getDataList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getDataList().get(i).getId());
                                serviceAdd.setNama(response.body().getDataList().get(i).getNama());
                                serviceAdd.setType(0);
                                serviceAdd.setNominal(0);
                                if(sessionManager.getTipeOrder() != null && sessionManager.getIdTipeOrder() > 0){
                                    if(sessionManager.getIdTipeOrder() == response.body().getDataList().get(i).getId()){
                                        serviceAdd.setChecked(true);
                                    }

                                }
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false, getArguments().getString(Constants.METHOD));
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetTipeOrderResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataLabelOrder(){
        dataList.clear();
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.labelorder().enqueue(new Callback<GetServiceAddResponse>() {
            @Override
            public void onResponse(Call<GetServiceAddResponse> call, Response<GetServiceAddResponse> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getDataList().size() > 0){
                            for(int i=0;i < response.body().getDataList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getDataList().get(i).getId());
                                serviceAdd.setNama(response.body().getDataList().get(i).getNama());
                                serviceAdd.setType(0);
                                serviceAdd.setNominal(0);
                                if(sessionManager.getLabelOrder() != null && sessionManager.getIdLabelOrder() > 0){
                                    if(sessionManager.getIdLabelOrder() == response.body().getDataList().get(i).getId()){
                                        serviceAdd.setChecked(true);
                                    }

                                }
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false, getArguments().getString(Constants.METHOD));
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetServiceAddResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataPajak(){
        dataList.clear();
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getPajak(sessionManager.getIdToko()).enqueue(new Callback<GetTaxResponseJson>() {
            @Override
            public void onResponse(Call<GetTaxResponseJson> call, Response<GetTaxResponseJson> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getPajakList().size() > 0){
                            for(int i=0;i < response.body().getPajakList().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getPajakList().get(i).getId());
                                serviceAdd.setNama(response.body().getPajakList().get(i).getNama());
                                serviceAdd.setType(response.body().getPajakList().get(i).getType());
                                serviceAdd.setNominal(response.body().getPajakList().get(i).getNominal());
                                if(sessionManager.getPajak() != null && sessionManager.getIdPajak() > 0){
                                    if(sessionManager.getIdPajak() == response.body().getPajakList().get(i).getId()){
                                        serviceAdd.setChecked(true);
                                    }

                                }
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select,
                                    clickInterface, false, getArguments().getString(Constants.METHOD));
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }else {
//                            lempty.setVisibility(View.VISIBLE);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetTaxResponseJson> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void dataService() {
        dataList.clear();
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getServiceFee(sessionManager.getIdToko()).enqueue(new Callback<GetServiceResponseJson>() {
            @Override
            public void onResponse(Call<GetServiceResponseJson> call, Response<GetServiceResponseJson> response) {
//                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        if(response.body().getServiceFees().size() > 0){
                            for(int i=0;i < response.body().getServiceFees().size();i++){
                                final ServiceAdd serviceAdd = new ServiceAdd();
                                serviceAdd.setId(response.body().getServiceFees().get(i).getId());
                                serviceAdd.setNama(response.body().getServiceFees().get(i).getNama());
                                serviceAdd.setType(0);
                                serviceAdd.setNominal(response.body().getServiceFees().get(i).getNominal());
                                dataList.add(serviceAdd);
                            }
                            adapter = new SelectAdapter(context, dataList,R.layout.item_select_service,
                                    clickInterface, true, getArguments().getString(Constants.METHOD));
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
//                        lempty.setVisibility(View.VISIBLE);
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetServiceResponseJson> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void updCartItwm(int mId, int mQty, int mDiskon){
        UpdCartItemRequest request = new UpdCartItemRequest();
        request.setIdcartitem(mId);
        request.setDiscount(mDiskon);
        request.setQty(mQty);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updCartItem(request).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface UpdateText{
        void updateResult(String metode, int id, String nama, int value, int nominal);
    }
}