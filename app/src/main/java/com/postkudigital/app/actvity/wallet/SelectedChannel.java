package com.postkudigital.app.actvity.wallet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SelectedChannel extends DialogFragment {
    private Context context;
    private SessionManager sessionManager;
    private TextView caption;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private Button button;
    private ChannelAdapter adapter;
    public SelectedChannel(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_pilih_channel, container, false);
        context = getActivity();
        sessionManager = new SessionManager(context);
        recyclerView = view.findViewById(R.id.rec_channel);
        button = view.findViewById(R.id.btn_activated);
        caption = view.findViewById(R.id.text_caption);
        backButton = view.findViewById(R.id.back_button);

        caption.setText("Pilih Channel");
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter != null){
                    UpdateText updateText = (UpdateText)getActivity();
                    updateText.updateResult(String.valueOf(adapter.getSelectedItem()), adapter.getChannelName());
                    dismiss();
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getChannel();
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

    public interface UpdateText{
        void updateResult(String id, String nama);
    }
}
