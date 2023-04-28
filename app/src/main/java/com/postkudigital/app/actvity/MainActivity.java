package com.postkudigital.app.actvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.plus.PostkuPlusActivity;
import com.postkudigital.app.actvity.profil.ProfileActivity;
import com.postkudigital.app.actvity.wallet.ActivatedWalletActivity;
import com.postkudigital.app.fragment.HomeFragment;
import com.postkudigital.app.fragment.absensi.AbsenStaffActivity;
import com.postkudigital.app.fragment.absensi.AbsensiFragment;
import com.postkudigital.app.fragment.customer.CustomerFragment;
import com.postkudigital.app.fragment.diskon.DiskonFragment;
import com.postkudigital.app.fragment.meja.TableFragment;
import com.postkudigital.app.fragment.pegawai.PegawaiFragment;
import com.postkudigital.app.fragment.pos.PosFragment;
import com.postkudigital.app.fragment.product.ProdukFragment;
import com.postkudigital.app.fragment.report.ReportFragment;
import com.postkudigital.app.fragment.riwayat.HistoryFragment;
import com.postkudigital.app.fragment.setting.SettingFragment;
import com.postkudigital.app.fragment.toko.TokoFragment;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetKritikResponse;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.models.Kontak;
import com.postkudigital.app.models.User;
import com.postkudigital.app.models.Wallet;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView textNama, textJabatan, textNamaToko;
    private CircleImageView avatar;
    String[] listCategory;
    private LinearLayout lfooter;
    private List<Kontak> kontakList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        user = BaseApp.getInstance(context).getLoginUser();
        sessionManager = new SessionManager(context);
        drawerLayout = findViewById(R.id.action_main);
        navigationView = findViewById(R.id.nav_view);
        lfooter = findViewById(R.id.linearLayout);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, navigationView, false);
        navigationView.addHeaderView(headerView);
        RelativeLayout rlsubs = headerView.findViewById(R.id.rlupgrade);
        RelativeLayout rlprofile = headerView.findViewById(R.id.rlprofile);
        CircleImageView cvprofile = headerView.findViewById(R.id.img_profile);

        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.image_placeholder)
                .into(cvprofile);

        lfooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogKritik();
            }
        });

//        manajemen menu
        Menu menu = navigationView.getMenu();
        MenuItem mReport = menu.findItem(R.id.report);
        MenuItem mToko = menu.findItem(R.id.toko);
        MenuItem mStaff = menu.findItem(R.id.pegawai);
        MenuItem mAbsensi = menu.findItem(R.id.absensi);
        MenuItem mTable = menu.findItem(R.id.meja);
        MenuItem mSetting = menu.findItem(R.id.setting);
        MenuItem mDiskon = menu.findItem(R.id.diskon);
        MenuItem mCustomer = menu.findItem(R.id.customer);
        MenuItem mMenu = menu.findItem(R.id.menu);

        if(user.isSubs()){
            if(user.isOwner()){
                mReport.setVisible(true);
                mToko.setVisible(true);
                mStaff.setVisible(true);
                mAbsensi.setVisible(true);
                mTable.setVisible(true);
                mSetting.setVisible(true);
                mDiskon.setVisible(true);
                mCustomer.setVisible(true);
                mMenu.setVisible(true);
            }else {
                mReport.setVisible(false);
                mToko.setVisible(false);
                mStaff.setVisible(false);
                mAbsensi.setVisible(true);
                mTable.setVisible(false);
                mSetting.setVisible(true);
                mDiskon.setVisible(false);
                mCustomer.setVisible(false);
                mMenu.setVisible(false);
            }

        }else {
            if(user.isOwner()){
                mReport.setVisible(true);
                mToko.setVisible(false);
                mStaff.setVisible(false);
                mAbsensi.setVisible(false);
                mTable.setVisible(false);
                mSetting.setVisible(false);
                mDiskon.setVisible(false);
                mCustomer.setVisible(false);
                mMenu.setVisible(true);
            }else {
                mReport.setVisible(false);
                mToko.setVisible(false);
                mStaff.setVisible(false);
                mAbsensi.setVisible(false);
                mTable.setVisible(false);
                mSetting.setVisible(false);
                mDiskon.setVisible(false);
                mCustomer.setVisible(false);
                mMenu.setVisible(false);
            }
        }


        rlsubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatusDeposit();
            }
        });

        rlprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        toolbar = findViewById(R.id.toolbar);
        textNama = headerView.findViewById(R.id.text_nama);
        textJabatan = headerView.findViewById(R.id.text_jabatan);
        textNamaToko = headerView.findViewById(R.id.text_toko);
        avatar = headerView.findViewById(R.id.img_profile);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.logo_app)
                .into(avatar);
        textNama.setText(user.getNama());
        if(user.isOwner()){
            textJabatan.setText("Owner");
        }else {
            textJabatan.setText("Kasir");
        }
        textNamaToko.setText(sessionManager.getNamaToko());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                toolbar.setTitle(item.getTitle());
                int itemId = item.getItemId();

                switch (itemId){
                    case R.id.home:
                        loadFragment(new HomeFragment());
                        break;
                    case R.id.riwayat:
                        loadFragment(new HistoryFragment());
                        break;
                    case R.id.kasir:
                        loadFragment(new PosFragment());
                        break;
                    case R.id.report:
                        loadFragment(new ReportFragment());
                        break;
                    case R.id.toko:
                        loadFragment(new TokoFragment());
                        break;
                    case R.id.pegawai:
                        loadFragment(new PegawaiFragment());
                        break;
                    case R.id.menu:
                        loadFragment(new ProdukFragment());
                        break;
                    case R.id.absensi:
                        if(user.isOwner()){
                            loadFragment(new AbsensiFragment());
                            break;
                        }else {
                            Intent intent = new Intent(context, AbsenStaffActivity.class);
                            startActivity(intent);
                            break;
                        }

                    case R.id.meja:
                        loadFragment(new TableFragment());
                        break;
                    case R.id.diskon:
                        loadFragment(new DiskonFragment());
                        break;
                    case R.id.customer:
                        loadFragment(new CustomerFragment());
                        break;
                    case R.id.chat:
                        toolbar.setTitle("Beranda");
                        showContactUs();
                        break;
                    case R.id.setting:
                        loadFragment(new SettingFragment());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        if(getIntent().getStringExtra(Constants.METHOD) != null){
            toolbar.setTitle("Kasir");
            loadFragment(new PosFragment());
        }else {
            toolbar.setTitle("Beranda");
            loadFragment(new HomeFragment());
        }

        if(sessionManager.getKontakList().size() > 0){
            kontakList = sessionManager.getKontakList();
        }

    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    private void showContactUs(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_contactus, null);
        builder.setView(dialogView);

        final RelativeLayout wasap = dialogView.findViewById(R.id.rlwa);
        final RelativeLayout instagram = dialogView.findViewById(R.id.rlinsta);

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        wasap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= kontakList.get(1).getUrl();
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= kontakList.get(0).getUrl();
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    private void showDialogKritik(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_stock, null);
        builder.setView(dialogView);

        final TextView title = dialogView.findViewById(R.id.title);
        final AutoCompleteTextView selectKategori = dialogView.findViewById(R.id.kategori);
        final EditText edtStock = dialogView.findViewById(R.id.edittext);
        final EditText edtNote = dialogView.findViewById(R.id.edittext2);
        final Button submit = dialogView.findViewById(R.id.btn_submit);

        selectKategori.setHint("Pilih");
        title.setText("Saran dan Kritik");
        edtStock.setVisibility(View.GONE);

        listCategory = getResources().getStringArray(R.array.list_saran);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listCategory);
        selectKategori.setAdapter(catAdapter);
        selectKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKategori.showDropDown();
            }
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "pos:" + selectKategori.getText().toString().trim());
                if(selectKategori.getText().toString().isEmpty()){
                    selectKategori.setError(context.getString(R.string.error_empty));
                    return;
                }else if(edtNote.getText().toString().isEmpty()){
                    edtNote.setError(context.getString(R.string.error_empty));
                    edtNote.requestFocus();
                    return;
                }

                postKritik(selectKategori.getText().toString(), edtNote.getText().toString());
                alertDialog.dismiss();
            }
        });
    }

    private void postKritik(String label, String isi){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.kritiksaran(user.getId(), label, isi).enqueue(new Callback<GetKritikResponse>() {
            @Override
            public void onResponse(Call<GetKritikResponse> call, Response<GetKritikResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 201){
                        DHelper.pesan(context, response.body().getMessage());
                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<GetKritikResponse> call, Throwable t) {

            }
        });
    }

    private void checkStatusDeposit(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detailWallet(sessionManager.getIdToko()).enqueue(new Callback<WalletResponseJson>() {
            @Override
            public void onResponse(Call<WalletResponseJson> call, Response<WalletResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        Wallet wallet = response.body().getWallet();
                        Intent intent = new Intent(context, PostkuPlusActivity.class);
                        intent.putExtra(Constants.NOMINAL, wallet.getBalance());
                        intent.putExtra(Constants.ID, wallet.getId());
                        startActivity(intent);
                    }else if(response.body().getStatusCode() == 404) {
                        Intent intent = new Intent(context, ActivatedWalletActivity.class);
                        startActivity(intent);
                    }
                }else {
                    DHelper.pesan(context, context.getString(R.string.error_connection));
                }
            }

            @Override
            public void onFailure(Call<WalletResponseJson> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();
//            layout.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, ask user if he wants to leave :)

            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi?")
                    .setMessage("Apakah yakin akan keluar aplikasi?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // MainActivity.super.onBackPressed();
                            finish();
                            moveTaskToBack(true);
                        }
                    }).create().show();
        }
    }
}