package com.postkudigital.app.fragment.riwayat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.postkudigital.app.R;
import com.postkudigital.app.adapter.ItemCartAdapter;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.helpers.OnCartItemClickListener;
import com.postkudigital.app.json.DetailTransactionResponse;
import com.postkudigital.app.models.ItemCart;
import com.postkudigital.app.models.ServiceFee;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postkudigital.app.helpers.Constants.TAG;

public class DetailTransaksiActivity extends AppCompatActivity implements OnCartItemClickListener {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption, invoice, kasir, tanggal, subtotal, diskon, customer, meja, tipeOrder,
                     labelOrder, payment, grandTotal, qty;
    private RecyclerView recyclerView;
    private Button printStruk;
    private ProgressBar progressBar;
    private LinearLayout main;
    private String inv;
    private ItemCartAdapter adapter;
    private Printing printing = null;
    PrintingCallback printingCallback=null;
    private byte FONT_SMALL = 5;
    private byte LINE_SPACING_1 = 1;
    private double pajak;
    private double discount;
    private double serviceFee;
    private double bayar;
    private double kembalian;
    private List<ItemCart> itemCartList = new ArrayList<>();
    private List<ServiceFee> serviceFeeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        invoice = findViewById(R.id.text_no_transaksi);
        kasir = findViewById(R.id.text_nama_kasir);
        tanggal = findViewById(R.id.text_tanggal);
        subtotal = findViewById(R.id.text_subtotal);
        diskon = findViewById(R.id.text_discount);
        customer = findViewById(R.id.text_customer);
        meja = findViewById(R.id.text_meja);
        tipeOrder = findViewById(R.id.text_tipe_order);
        labelOrder = findViewById(R.id.text_label_order);
        payment = findViewById(R.id.text_payment);
        grandTotal = findViewById(R.id.text_total);
        qty = findViewById(R.id.text_total_item);
        recyclerView = findViewById(R.id.rec_rincian);
        printStruk = findViewById(R.id.btn_print);
        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.main_content);

        inv = getIntent().getStringExtra(Constants.ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        getDetail(inv);
        caption.setText("Detail Transaksi");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();

        printStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(printing != null) {
                    printing.print(getSomePrintables());
                }else {
                    Toast.makeText(getApplicationContext(), "no printer", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getDetail(String kode){
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detail(kode).enqueue(new Callback<DetailTransactionResponse>() {
            @Override
            public void onResponse(Call<DetailTransactionResponse> call, Response<DetailTransactionResponse> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        main.setVisibility(View.VISIBLE);
                        invoice.setText(kode);
                        tanggal.setText(DHelper.strTodatetime(response.body().getTransaction().getCreatedAt()));
                        kasir.setText(response.body().getUser().getNama());
                        if(response.body().getTransaction().getPaymentType() == 1){
                            payment.setText("Tunai");
                        }else {
                            payment.setText("QRIS");
                        }
                        subtotal.setText(DHelper.formatRupiah(response.body().getCart().getTotalPrice()));
                        diskon.setText(DHelper.formatRupiah(response.body().getCart().getTotalDisc()));
                        grandTotal.setText(DHelper.formatRupiah(response.body().getCart().getGrandTotal()));
                        qty.setText(response.body().getCart().getTotalItem() + " Items");
                        if(response.body().getItemCarts().size() > 0){
                            adapter = new ItemCartAdapter(context, response.body().getItemCarts(), DetailTransaksiActivity.this::onItemClick, false);
                            recyclerView.setAdapter(adapter);
                        }
                        itemCartList = response.body().getItemCarts();
                        serviceFeeList = response.body().getServiceFeeList();
                        pajak = Math.round(response.body().getCart().getTotalPajak());
                        discount = Math.round(response.body().getCart().getTotalDisc());
                        serviceFee = Math.round(response.body().getCart().getTotalServiceFee());
                        bayar = response.body().getTransaction().getBayar();
                        kembalian = response.body().getTransaction().getKembalian();

                        if(response.body().getCustomer() != null){
                            customer.setText(response.body().getCustomer().getNama());
                        }else{
                            customer.setText("-");
                        }

                        if(response.body().getMeja() != null){
                            meja.setText(response.body().getMeja().getNama());
                        }else {
                            meja.setText("-");
                        }

                        if(response.body().getLabelOrder() != null){
                            labelOrder.setText(response.body().getLabelOrder().getNama());
                        }else {
                            labelOrder.setText("-");
                        }

                        if(response.body().getTipeOrder() != null){
                            tipeOrder.setText(response.body().getTipeOrder().getNama());
                        }else {
                            tipeOrder.setText("-");
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<DetailTransactionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private ArrayList<Printable> getSomePrintables() {
        ArrayList<Printable> al = new ArrayList<>();
        Resources resources = getResources();
//        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode

        al.add( (new TextPrintable.Builder())
                .setText(sessionManager.getNamaToko())
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_30())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText(sessionManager.getAlamatToko())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setFontSize(FONT_SMALL)
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        int lenKasir = 32 - 5;
        al.add( (new TextPrintable.Builder())
                .setText("Kasir" + String.format("%" + lenKasir + "s", kasir.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenMetode = 32 - 12;
        al.add( (new TextPrintable.Builder())
                .setText("Metode bayar" + String.format("%" + lenMetode + "s", payment.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenWaktu = 32 - 5;
        al.add( (new TextPrintable.Builder())
                .setText("Waktu" + String.format("%" + lenWaktu + "s", tanggal.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenInv = 32 - 3;
        al.add( (new TextPrintable.Builder())
                .setText("Inv" + String.format("%" + lenInv + "s", inv))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        for(int i=0;i < itemCartList.size();i++){
            al.add( (new TextPrintable.Builder())
                    .setText(itemCartList.get(i).getMenuName().getNama())
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                    .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                    .setFontSize(FONT_SMALL)
                    .setLineSpacing(LINE_SPACING_1)
                    .setNewLinesAfter(1)
                    .build());

            String hargaitem = "";
            String grandItem = "";
            hargaitem = "Rp" + DHelper.toformatRupiah(String.valueOf(itemCartList.get(i).getMenuName().getHarga())) + "x" + itemCartList.get(i).getQty();
            double grandTotalItem = Math.round(itemCartList.get(i).getGrandTotalPrice());
            grandItem = "Rp" + DHelper.toformatRupiah(String.valueOf(grandTotalItem));

            int lenPrice = 32 - hargaitem.length();

            al.add( (new TextPrintable.Builder())
                    .setText(hargaitem + String.format("%" + lenPrice + "s", grandItem))
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                    .setFontSize(FONT_SMALL)
                    .setLineSpacing(LINE_SPACING_1)
                    .setNewLinesAfter(1)
                    .build());

            double diskonItem = Math.round(itemCartList.get(i).getTotalDisc());
            if(diskonItem > 0){
                String discountItem = "-Rp" + DHelper.toformatRupiah(String.valueOf(diskonItem));
                al.add( (new TextPrintable.Builder())
                        .setText("Disc:" + discountItem)
                        .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                        .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                        .setFontSize(FONT_SMALL)
                        .setLineSpacing(LINE_SPACING_1)
                        .setNewLinesAfter(1)
                        .build());
            }

        }

        for(int x=0;x < serviceFeeList.size();x++){
            String namaService = "";
            String hargaService = "";
            namaService = serviceFeeList.get(x).getNama();
            hargaService = "Rp" + DHelper.toformatRupiah(String.valueOf(serviceFeeList.get(x).getNominal()));

            int lenNama = 32 - namaService.length();
            al.add( (new TextPrintable.Builder())
                    .setText(namaService + String.format("%" + lenNama + "s", hargaService))
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                    .setFontSize(FONT_SMALL)
                    .setLineSpacing(LINE_SPACING_1)
                    .setNewLinesAfter(1)
                    .build());

        }

        al.add( (new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        int lenPpn = 32 - 5;
        al.add( (new TextPrintable.Builder())
                .setText("Pajak" + String.format("%" + lenPpn + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(pajak))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenDiscTot = 32 - 8;
        al.add( (new TextPrintable.Builder())
                .setText("Discount" + String.format("%" + lenDiscTot + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(discount))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());



        al.add( (new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());


        int lenGrandTot = 32 - 11;
        al.add( (new TextPrintable.Builder())
                .setText("Grand Total" + String.format("%" + lenGrandTot + "s", grandTotal.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        int lenBayar = 32 - 5;
        al.add( (new TextPrintable.Builder())
                .setText("Bayar" + String.format("%" + lenBayar + "s", "Rp"+DHelper.toformatRupiah(String.valueOf(bayar))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenKembali = 32 - 9;
        al.add( (new TextPrintable.Builder())
                .setText("Kembalian" + String.format("%" + lenKembali + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(kembalian))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText("Powered By POSTKU")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setFontSize(FONT_SMALL)
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText("www.postku.site")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setFontSize(FONT_SMALL)
                .setNewLinesAfter(2)
                .build());

//        Bitmap image = BitmapFactory.decodeResource(resources, R.drawable.img_footer_print);
//        al.add(new ImagePrintable.Builder(image)
//                .setNewLinesAfter(1)
//                .build());

        return al;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClick(int id, int qty, String nama, int harga, int method) {

    }
}