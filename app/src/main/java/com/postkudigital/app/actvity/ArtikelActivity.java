package com.postkudigital.app.actvity;

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
import com.postkudigital.app.adapter.NewsAdapter;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetArtikelResponse;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class ArtikelActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption, readMore;
    private LinearLayout lempty;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private int pages=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        readMore = findViewById(R.id.text_read_more);
        lempty = findViewById(R.id.lempty);
        recyclerView = findViewById(R.id.rec_history);
        caption.setText("Kategori Produk");
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        caption.setText("Berita Bisnis");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData(pages);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(pages);
            }
        });
    }

    private void getData(int page) {
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.artikel(String.valueOf(page)).enqueue(new Callback<GetArtikelResponse>() {
            @Override
            public void onResponse(Call<GetArtikelResponse> call, Response<GetArtikelResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getArtikelList().isEmpty()){
                            lempty.setVisibility(View.VISIBLE);
                            readMore.setVisibility(View.GONE);
                        }else {
                            adapter = new NewsAdapter(context, response.body().getArtikelList());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            readMore.setVisibility(View.VISIBLE);
                            pages = pages + 1;
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetArtikelResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}