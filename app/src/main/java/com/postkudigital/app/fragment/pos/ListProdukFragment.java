package com.postkudigital.app.fragment.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.ProdukAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.helpers.OnItemClickListener;
import com.postkudigital.app.json.CreateCartResponse;
import com.postkudigital.app.json.DetailCartResponse;
import com.postkudigital.app.json.GetCartResponse;
import com.postkudigital.app.json.GetMenuResponseJson;
import com.postkudigital.app.json.InsertItemResponse;
import com.postkudigital.app.models.Cart;
import com.postkudigital.app.models.ItemCart;
import com.postkudigital.app.models.User;
import com.postkudigital.app.models.order.OrderLocal;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class ListProdukFragment extends Fragment implements OnItemClickListener, SelectTable.UpdateText {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private int idCat;
    private TextView textView;
    private ProdukAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout lempty, lbasket;
    private LinearLayout boxCart;
    private TextView qty, total;
    private ImageView imgTable, imgList;
    int idCart = 0;
    int idTable = 0;
    private List<ItemCart> itemCartList = new ArrayList<>();
    Realm realm;
    public ListProdukFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_produk, container, false);
        context = getActivity();
        realm = BaseApp.getInstance(context).getRealmInstance();
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        textView = view.findViewById(R.id.textView24);
        idCat = getArguments().getInt(Constants.ID_KATEGORI,0);
        textView.setText(getArguments().getString(Constants.NAMA) + "-" + idCat);
        recyclerView = view.findViewById(R.id.recProduk);
        lempty = view.findViewById(R.id.layout_empty);
        boxCart = view.findViewById(R.id.container);
        lbasket = view.findViewById(R.id.lbasket);
        qty = view.findViewById(R.id.text_qty);
        total = view.findViewById(R.id.text_total);
        imgTable = view.findViewById(R.id.img_meja);
        imgList = view.findViewById(R.id.img_save);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        if(idCart > 0){
            lbasket.setEnabled(true);
        }else {
            lbasket.setEnabled(false);
        }

        lbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailOrderActivity.class);
                intent.putExtra(Constants.ID, idCart);
                startActivity(intent);
            }
        });

        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PesananActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        final SelectTable dialogFragment = new SelectTable();
        imgTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.METHOD, "1");
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, TAG);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(idCat);
        if(sessionManager.isCartActive()){
            checkCartExist(Integer.parseInt(sessionManager.getActiveCart()));
        }else{
            checkCart();
        }
        Log.e(TAG, "IDCART:" + idCart);
//
//        loadOrder();
    }

    private void getData(int id){

        lempty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        if(id > 0){
            service.getMenuByKategori(sessionManager.getIdToko(), String.valueOf(id)).enqueue(new Callback<GetMenuResponseJson>() {
                @Override
                public void onResponse(Call<GetMenuResponseJson> call, Response<GetMenuResponseJson> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatusCode() == 200){
                            if(response.body().getMenusList().isEmpty()){
                                lempty.setVisibility(View.VISIBLE);
                            }else {
                                adapter = new ProdukAdapter(context, response.body().getMenusList(), ListProdukFragment.this::onItemClick);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
                        }else {
                            lempty.setVisibility(View.VISIBLE);
                            DHelper.pesan(context, response.body().getMsg());
                        }
                    }else {
                        DHelper.pesan(context, context.getString(R.string.error_server));
                    }
                }

                @Override
                public void onFailure(Call<GetMenuResponseJson> call, Throwable t) {

                    t.printStackTrace();
                    Log.e(Constants.TAG, t.getMessage());
                }
            });
        }else {
            service.getMenu(sessionManager.getIdToko()).enqueue(new Callback<GetMenuResponseJson>() {
                @Override
                public void onResponse(Call<GetMenuResponseJson> call, Response<GetMenuResponseJson> response) {

                    if(response.isSuccessful()){
                        if(response.body().getStatusCode() == 200){
                            if(response.body().getMenusList().isEmpty()){
                                lempty.setVisibility(View.VISIBLE);
                            }else {
                                adapter = new ProdukAdapter(context, response.body().getMenusList(), ListProdukFragment.this::onItemClick);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
                        }else {
                            lempty.setVisibility(View.VISIBLE);
                            DHelper.pesan(context, response.body().getMsg());
                        }
                    }else {
                        DHelper.pesan(context, context.getString(R.string.error_server));
                    }
                }

                @Override
                public void onFailure(Call<GetMenuResponseJson> call, Throwable t) {

                    t.printStackTrace();
                    Log.e(Constants.TAG, t.getMessage());
                }
            });
        }
    }

    @Override
    public void onItemClick(int id) {
        addItem(id);
    }

    private void checkCartExist(int id){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailCart(String.valueOf(id)).enqueue(new Callback<DetailCartResponse>() {
            @Override
            public void onResponse(Call<DetailCartResponse> call, Response<DetailCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        boxCart.setVisibility(View.VISIBLE);
                        imgTable.setVisibility(View.VISIBLE);
                        lbasket.setEnabled(true);
                        Cart cart = response.body().getCart();
                        idCart = cart.getId();
                        qty.setText(cart.getTotalItem() + " item");
                        double grandTotal = Math.round(cart.getTotalPrice());
                        total.setText("Total= Rp" + DHelper.toformatRupiah(String.valueOf(grandTotal)));
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailCartResponse> call, Throwable t) {

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
                            Log.e(TAG, "------------sini");
                            boxCart.setVisibility(View.VISIBLE);
                            imgTable.setVisibility(View.VISIBLE);
                            lbasket.setEnabled(true);
                            Cart cart = response.body().getCartList().get(0);
                            idCart = cart.getId();
                            sessionManager.createCart(String.valueOf(idCart));
                            qty.setText(cart.getTotalItem() + " item");
                            double grandTotal = Math.round(cart.getGrandTotal());
                            total.setText("Total= Rp" + DHelper.toformatRupiah(String.valueOf(grandTotal)));
                        }else {
                            boxCart.setVisibility(View.VISIBLE);
                            imgTable.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCartResponse> call, Throwable t) {
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

    public void addItem(int id){
        Log.e(TAG, id + "-------------");
        if(idCart > 0){
            addMenuItem(id);
        }else {
            createCart(id);
        }
//        if(checkOrder(id)){
//
//        }else {
//
//        }
    }

    private void loadOrder(){
        List<OrderLocal> existingOrder = realm.where(OrderLocal.class).findAll();
        int quantity = 0;
        long cost = 0;

        for(int o = 0; o < existingOrder.size();o++){
            quantity += Objects.requireNonNull(existingOrder.get(o).getQty());
            cost += Objects.requireNonNull(existingOrder.get(o).getTotalHarga());
        }
        qty.setText(quantity + " item");
        total.setText("Total= Rp" + DHelper.toformatRupiah(String.valueOf(cost)));
    }

    private boolean checkOrder(int idItem){
        RealmResults<OrderLocal> basket = realm.where(OrderLocal.class).equalTo("idMenu", idItem).findAll();
        if(basket.size() > 0){
            return true;
        }
        return false;
    }

    private void addMenu(int idMenu, int harga, int totalHarga, int qty, int disc){
        OrderLocal local = new OrderLocal();
        local.setIdMenu(idMenu);
        local.setHarga(harga);
        local.setTotalHarga(totalHarga);
        local.setQty(qty);
        local.setDisc(disc);

        realm.beginTransaction();
        realm.copyToRealm(local);
        realm.commitTransaction();
    }


    private void createCart(int id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user", createPartFromString(user.getId()));
        map.put("toko", createPartFromString(sessionManager.getIdToko()));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.createCart(map).enqueue(new Callback<CreateCartResponse>() {
            @Override
            public void onResponse(Call<CreateCartResponse> call, Response<CreateCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        Cart cart = response.body().getCart();
                        idCart = cart.getId();
                        addMenuItem(id);
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateCartResponse> call, Throwable t) {

            }
        });
    }

    private void addMenuItem(int id){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("cart", createPartFromString(String.valueOf(idCart)));
        map.put("menu", createPartFromString(String.valueOf(id)));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.addItem(map).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
//                        DHelper.pesan(context, response.body().getMessage());
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

            }
        });
    }

    private void updMenuItem(int id, int qty){
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id_cart_item", createPartFromString(String.valueOf(id)));
        map.put("qty", createPartFromString(String.valueOf(qty)));
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.updateItem(map).enqueue(new Callback<InsertItemResponse>() {
            @Override
            public void onResponse(Call<InsertItemResponse> call, Response<InsertItemResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
//                        DHelper.pesan(context, response.body().getMessage());
                        checkCart();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertItemResponse> call, Throwable t) {

            }
        });
    }

    private void detailCart(int id, int idItem){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailCart(String.valueOf(id)).enqueue(new Callback<DetailCartResponse>() {
            @Override
            public void onResponse(Call<DetailCartResponse> call, Response<DetailCartResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        if(response.body().getItemCartList().size() > 0){
                            itemCartList = response.body().getItemCartList();
                            for(int i=0;i < itemCartList.size();i++){
                                Log.e(TAG, i + "------");
                                if(itemCartList.get(i).getMenu() == idItem){
                                    Log.e(TAG, "upd idmenu: " + itemCartList.get(i).getMenu() + " iditem:" + idItem);
                                    updMenuItem(itemCartList.get(i).getId(), itemCartList.get(i).getQty() + 1);
                                }else{
                                    Log.e(TAG, "add idmenu: " + itemCartList.get(i).getMenu() + " iditem:" + idItem);
                                    addMenuItem(idItem);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailCartResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void updateResult(String id, String nama) {
        idTable = Integer.parseInt(id);
        Log.e(TAG, "Meja:" + nama);
    }

}