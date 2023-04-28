package com.postkudigital.app.actvity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.postkudigital.app.R;
import com.postkudigital.app.adapter.ChannelAdapter;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetChannelResponseJson;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihChannelActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private Button button;
    private ChannelAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_channel);
        context = this;
        sessionManager = new SessionManager(context);
        recyclerView = findViewById(R.id.rec_channel);
        button = findViewById(R.id.btn_activated);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){

                }
            }
        });
    }

    private void getChannel(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getchannel().enqueue(new Callback<GetChannelResponseJson>() {
            @Override
            public void onResponse(Call<GetChannelResponseJson> call, Response<GetChannelResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        adapter = new ChannelAdapter(context, response.body().getChannelList());
                        recyclerView.setAdapter(adapter);
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetChannelResponseJson> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }
}