package com.postkudigital.app.fragment.product.stok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.HistoryStockAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.DetailStockResponse;
import com.postkudigital.app.json.StockTrxResponse;
import com.postkudigital.app.json.TrxStockResponse;
import com.postkudigital.app.models.HistoryStock;
import com.postkudigital.app.models.Stock;
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

public class DetailStockActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView caption, nama, currentStock;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private HistoryStockAdapter adapter;
    private LinearLayout ladd;
    private int id;
    String[] listCategory;
    int typeAdjust = 0;
    private SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stock);
        context = this;
        sessionManager = new SessionManager(context);
        caption = findViewById(R.id.text_caption);
        nama = findViewById(R.id.text_nama_menu);
        currentStock = findViewById(R.id.text_cur_stock);
        backButton = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.rec_history);
        ladd = findViewById(R.id.ladd);
        swipe = findViewById(R.id.swipe);

        caption.setText("Detail Stock");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        id = getIntent().getIntExtra(Constants.ID, 0);
        nama.setText(getIntent().getStringExtra(Constants.NAMA));
        currentStock.setText("" + getIntent().getIntExtra(Constants.METHOD, 0));
        getData(id);

        ladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(id);
                swipe.setRefreshing(false);
            }
        });

    }

    private void getData(int idMenu){
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.historyStock(String.valueOf(idMenu)).enqueue(new Callback<StockTrxResponse>() {
            @Override
            public void onResponse(Call<StockTrxResponse> call, Response<StockTrxResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getHistoryStockList().size() > 0){
                            adapter = new HistoryStockAdapter(context, response.body().getHistoryStockList(), DetailStockActivity.this);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<StockTrxResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void detailstok(String id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailStock(id).enqueue(new Callback<DetailStockResponse>() {
            @Override
            public void onResponse(Call<DetailStockResponse> call, Response<DetailStockResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        Stock stock = response.body().getStock();
                        nama.setText(stock.getNama());
                        currentStock.setText(stock.getCurrentStock() + "");
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailStockResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showDialogAdd(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_stock, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final AutoCompleteTextView selectKategori = dialogView.findViewById(R.id.kategori);
        final EditText edtStock = dialogView.findViewById(R.id.edittext);
        final EditText edtNote = dialogView.findViewById(R.id.edittext2);
        final Button submit = dialogView.findViewById(R.id.btn_submit);

        listCategory = getResources().getStringArray(R.array.trx_stock);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listCategory);
        selectKategori.setAdapter(catAdapter);
        selectKategori.setSelection(0);
        selectKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKategori.showDropDown();
            }
        });

        selectKategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdjust = position;
            }
        });


        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "pos:" + typeAdjust);
                trxStock(id, edtStock.getText().toString(), typeAdjust, edtNote.getText().toString());
                alertDialog.dismiss();
            }
        });


    }

    public void showDialogDetail(HistoryStock historyStock){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_detail_stock, null);
        builder.setView(dialogView);

        final TextView tipe = dialogView.findViewById(R.id.tipe);
        final TextView qty = dialogView.findViewById(R.id.qty);
        final TextView note = dialogView.findViewById(R.id.note);


        if(historyStock.getType() == 1){
            tipe.setText("Penambahan");
            tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_green));
        }else if(historyStock.getType() == 2){
            tipe.setText("Pengurangan");
            tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_red));
        }else if(historyStock.getType() == 3){
            tipe.setText("Reset Stock");
            tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_yellow));
        }else if(historyStock.getType() == 4){
            tipe.setText("Penjualan");
            tipe.setBackground(context.getResources().getDrawable(R.drawable.bg_rectangle_blue));
        }
        qty.setText("" + historyStock.getAdjustment());
        note.setText(historyStock.getNote());

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void trxStock(int id, String qty, int type, String note){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("stock", createPartFromString(String.valueOf(id)));
        map.put("adjustment_stock", createPartFromString(qty));
        map.put("type_adjustment", createPartFromString(String.valueOf(type)));
        map.put("note", createPartFromString(note));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.stocktrx(map).enqueue(new Callback<TrxStockResponse>() {
            @Override
            public void onResponse(Call<TrxStockResponse> call, Response<TrxStockResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        getData(id);
                        detailstok(String.valueOf(id));
                    }else{
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TrxStockResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}