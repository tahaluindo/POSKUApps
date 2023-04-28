package com.postkudigital.app.actvity.ppob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.CategoryPpobAdapter;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.PpobCategoryResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class PpobKategoriActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption;
    private LinearLayout lempty;
    private RecyclerView recyclerView;
    private CategoryPpobAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppob_kategori);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        lempty = findViewById(R.id.lempty);
        recyclerView = findViewById(R.id.rec_history);
        caption.setText("Kategori Produk");
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

    private void getData() {
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.kategoriPpob().enqueue(new Callback<PpobCategoryResponse>() {
            @Override
            public void onResponse(Call<PpobCategoryResponse> call, Response<PpobCategoryResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getKategoriPpobList().isEmpty()){
                            lempty.setVisibility(View.VISIBLE);
                        }else {
                            adapter = new CategoryPpobAdapter(context, response.body().getKategoriPpobList(), R.layout.item_category_ppob);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<PpobCategoryResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }
}