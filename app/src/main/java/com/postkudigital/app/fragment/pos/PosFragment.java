package com.postkudigital.app.fragment.pos;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.CreateCartResponse;
import com.postkudigital.app.json.GetCartResponse;
import com.postkudigital.app.json.GetKategoriResponseJson;
import com.postkudigital.app.json.InsertItemResponse;
import com.postkudigital.app.models.Cart;
import com.postkudigital.app.models.Kategori;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class PosFragment extends Fragment {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CategoryTabAdapter adapter;
    List<Kategori> kategoriList = new ArrayList<>();
    private LinearLayout boxCart;
    private TextView qty, total;
    private ImageView imgTable, imgList;
    int idCart = 0;
    public PosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pos, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        boxCart = view.findViewById(R.id.container);
        qty = view.findViewById(R.id.text_qty);
        total = view.findViewById(R.id.text_total);
        imgTable = view.findViewById(R.id.img_meja);
        imgList = view.findViewById(R.id.img_save);

        getData();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getData(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getKategori(sessionManager.getIdToko()).enqueue(new Callback<GetKategoriResponseJson>() {
            @Override
            public void onResponse(Call<GetKategoriResponseJson> call, Response<GetKategoriResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getKategoriList().size() > 0){
                            Kategori kategori = new Kategori();
                            kategori.setId(0);
                            kategori.setActive(true);
                            kategori.setLabel("All");
                            kategori.setToko(Integer.parseInt(sessionManager.getIdToko()));
                            kategoriList.add(kategori);

                            for (int i=0;i < response.body().getKategoriList().size();i++){
                                Kategori k = new Kategori();
                                k.setId(response.body().getKategoriList().get(i).getId());
                                k.setLabel(response.body().getKategoriList().get(i).getLabel());
                                k.setToko(response.body().getKategoriList().get(i).getToko());
                                k.setActive(response.body().getKategoriList().get(i).isActive());
                                k.setCreatedAt(response.body().getKategoriList().get(i).getCreatedAt());
                                kategoriList.add(k);
                            }

//                            kategoriList = response.body().getKategoriList();
                            adapter = new CategoryTabAdapter(getChildFragmentManager(), kategoriList);
                            for(int i=0;i < kategoriList.size();i++){
                                tabLayout.addTab(tabLayout.newTab().setText(kategoriList.get(i).getLabel()));
                            }
                            viewPager.setAdapter(adapter);
                            viewPager.setOffscreenPageLimit(1);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                        }else{
                            Kategori kategori = new Kategori();
                            kategori.setId(0);
                            kategori.setActive(true);
                            kategori.setLabel("All");
                            kategori.setToko(Integer.parseInt(sessionManager.getIdToko()));
                            kategoriList.add(kategori);
                            adapter = new CategoryTabAdapter(getChildFragmentManager(), kategoriList);
                            for(int i=0;i < kategoriList.size();i++){
                                tabLayout.addTab(tabLayout.newTab().setText(kategoriList.get(i).getLabel()));
                            }
                            viewPager.setAdapter(adapter);
                            viewPager.setOffscreenPageLimit(1);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        }
                    }else {
                        DHelper.pesan(context, response.body().getMsg());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_server));
                }
            }

            @Override
            public void onFailure(Call<GetKategoriResponseJson> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void checkCart(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.getCart(sessionManager.getIdToko()).enqueue(new Callback<GetCartResponse>() {
            @Override
            public void onResponse(Call<GetCartResponse> call, Response<GetCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getCartList().size() > 0){
                            boxCart.setVisibility(View.VISIBLE);
                            Cart cart = response.body().getCartList().get(0);
                            idCart = cart.getId();
                            qty.setText(cart.getTotalItem() + " item");
                            double grandTotal = Math.round(cart.getGrandTotal());
                            total.setText("Total= Rp" + DHelper.toformatRupiah(String.valueOf(grandTotal)));
                        }else {
                            boxCart.setVisibility(View.VISIBLE);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCartResponse> call, Throwable t) {

            }
        });
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public void addItem(int id, String userId, String idToko, String token){
        Log.e(TAG, id + "-------------");
        if(idCart > 0){
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("cart", createPartFromString(String.valueOf(idCart)));
            map.put("menu", createPartFromString(String.valueOf(id)));
            UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
            service.addItem(map).enqueue(new Callback<InsertItemResponse>() {
                @Override
                public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatusCode() == 201){
//                            DHelper.pesan(context, response.body().getMessage());
                            checkCart();
                        }
                    }
                }

                @Override
                public void onFailure(Call<InsertItemResponse> call, Throwable t) {

                }
            });
        }else {
            createCart(userId, idToko, token);
        }

    }

    private void createCart(String userId, String idToko,String token){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user", createPartFromString(userId));
        map.put("toko", createPartFromString(idToko));
        UserService service = ServiceGenerator.createService(UserService.class, token, null, null, null);
        service.createCart(map).enqueue(new Callback<CreateCartResponse>() {
            @Override
            public void onResponse(Call<CreateCartResponse> call, Response<CreateCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        Cart cart = response.body().getCart();
                        idCart = cart.getId();
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateCartResponse> call, Throwable t) {

            }
        });
    }
}