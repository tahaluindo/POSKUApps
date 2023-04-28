package com.postkudigital.app.fragment.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.postkudigital.app.R;
import com.postkudigital.app.actvity.MainActivity;
import com.postkudigital.app.helpers.Constants;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.DetailTransactionResponse;
import com.postkudigital.app.models.ItemCart;
import com.postkudigital.app.models.ServiceFee;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultTransactionActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private TextView kasir, toko, payment, total, tanggal, status;
    private ImageView imgStatus;
    private Button print, backToTransaction;
    private Printing printing = null;
    PrintingCallback printingCallback = null;
    private byte FONT_SMALL = 5;
    private byte LINE_SPACING_1 = 1;
    private double pajak;
    private double discount;
    private double serviceFee;
    private double grandTotal;
    private double bayar;
    private double kembalian;
    private List<ItemCart> itemCartList = new ArrayList<>();
    private List<ServiceFee> serviceFeeList = new ArrayList<>();
    private String inv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_transaction);
        context = this;
        sessionManager = new SessionManager(context);
        imgStatus = findViewById(R.id.img_status);
        status = findViewById(R.id.text_status);
        tanggal = findViewById(R.id.text_tanggal);
        kasir = findViewById(R.id.text_kasir);
        toko = findViewById(R.id.text_toko);
        payment = findViewById(R.id.text_payment);
        total = findViewById(R.id.text_total);
        print = findViewById(R.id.button3);
        backToTransaction = findViewById(R.id.button4);

        inv = getIntent().getStringExtra(Constants.INVOICE);
        getResult(inv);

        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (printing != null) {
                    printing.print(getSomePrintables());
                } else {
                    Toast.makeText(getApplicationContext(), "no printer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backToTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.METHOD, Constants.RESET);
                startActivity(intent);
                sessionManager.deleteCart();
                finish();
            }
        });
    }


    private void getResult(String invoice) {
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.detail(invoice).enqueue(new Callback<DetailTransactionResponse>() {
            @Override
            public void onResponse(Call<DetailTransactionResponse> call, Response<DetailTransactionResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatusCode() == 200) {
                        tanggal.setText(DHelper.strTodatetime(response.body().getTransaction().getCreatedAt()));
                        kasir.setText(response.body().getUser().getNama());
                        toko.setText(response.body().getToko().getNama());
                        if (response.body().getTransaction().getPaymentType() == 1) {
                            payment.setText("Tunai");
                        } else {
                            payment.setText("QRIS");
                        }
                        total.setText(DHelper.formatRupiah(response.body().getTransaction().getGrandTotal()));
                        serviceFeeList = response.body().getServiceFeeList();
                        itemCartList = response.body().getItemCarts();
                        pajak = Math.round(response.body().getCart().getTotalPajak());
                        discount = Math.round(response.body().getCart().getTotalDisc());
                        serviceFee = Math.round(response.body().getCart().getTotalServiceFee());
                        bayar = response.body().getTransaction().getBayar();
                        kembalian = response.body().getTransaction().getKembalian();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailTransactionResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private ArrayList<Printable> getSomePrintables() {
        ArrayList<Printable> al = new ArrayList<>();
        Resources resources = getResources();
//        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode


        al.add((new TextPrintable.Builder())
                .setText(sessionManager.getNamaToko())
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_30())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(1)
                .build());

        al.add((new TextPrintable.Builder())
                .setText(sessionManager.getAlamatToko())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setFontSize(FONT_SMALL)
                .setNewLinesAfter(1)
                .build());

        al.add((new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        int lenKasir = 32 - 5;
        al.add((new TextPrintable.Builder())
                .setText("Kasir" + String.format("%" + lenKasir + "s", kasir.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenMetode = 32 - 12;
        al.add((new TextPrintable.Builder())
                .setText("Metode bayar" + String.format("%" + lenMetode + "s", payment.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenWaktu = 32 - 5;
        al.add((new TextPrintable.Builder())
                .setText("Waktu" + String.format("%" + lenWaktu + "s", tanggal.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenInv = 32 - 3;
        al.add((new TextPrintable.Builder())
                .setText("Inv" + String.format("%" + lenInv + "s", inv))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        al.add((new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        for (int i = 0; i < itemCartList.size(); i++) {
            al.add((new TextPrintable.Builder())
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

            al.add((new TextPrintable.Builder())
                    .setText(hargaitem + String.format("%" + lenPrice + "s", grandItem))
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                    .setFontSize(FONT_SMALL)
                    .setLineSpacing(LINE_SPACING_1)
                    .setNewLinesAfter(1)
                    .build());

            double diskonItem = Math.round(itemCartList.get(i).getTotalDisc());
            if (diskonItem > 0) {
                String discountItem = "-Rp" + DHelper.toformatRupiah(String.valueOf(diskonItem));
                al.add((new TextPrintable.Builder())
                        .setText("Disc:" + discountItem)
                        .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                        .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                        .setFontSize(FONT_SMALL)
                        .setLineSpacing(LINE_SPACING_1)
                        .setNewLinesAfter(1)
                        .build());
            }

        }

        al.add((new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        int lenPpn = 32 - 5;
        al.add((new TextPrintable.Builder())
                .setText("Pajak" + String.format("%" + lenPpn + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(pajak))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenDiscTot = 32 - 8;
        al.add((new TextPrintable.Builder())
                .setText("Discount" + String.format("%" + lenDiscTot + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(discount))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        for (int x = 0; x < serviceFeeList.size(); x++) {
            String namaService = "";
            String hargaService = "";
            namaService = serviceFeeList.get(x).getNama();
            hargaService = "Rp" + DHelper.toformatRupiah(String.valueOf(serviceFeeList.get(x).getNominal()));

            int lenNama = 32 - namaService.length();
            al.add((new TextPrintable.Builder())
                    .setText(namaService + String.format("%" + lenNama + "s", hargaService))
                    .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                    .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                    .setFontSize(FONT_SMALL)
                    .setLineSpacing(LINE_SPACING_1)
                    .setNewLinesAfter(1)
                    .build());

        }


        al.add((new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());


        int lenGrandTot = 32 - 11;
        al.add((new TextPrintable.Builder())
                .setText("Grand Total" + String.format("%" + lenGrandTot + "s", total.getText().toString()))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        al.add((new TextPrintable.Builder())
                .setText("--------------------------------")
                .setNewLinesAfter(1)
                .build());

        int lenBayar = 32 - 5;
        al.add((new TextPrintable.Builder())
                .setText("Bayar" + String.format("%" + lenBayar + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(bayar))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenKembali = 32 - 9;
        al.add((new TextPrintable.Builder())
                .setText("Kembalian" + String.format("%" + lenKembali + "s", "Rp" + DHelper.toformatRupiah(String.valueOf(kembalian))))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setNewLinesAfter(1)
                .build());

        al.add((new TextPrintable.Builder())
                .setText("Powered By POSTKU")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setFontSize(FONT_SMALL)
                .setNewLinesAfter(1)
                .build());

        al.add((new TextPrintable.Builder())
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
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.METHOD, Constants.RESET);
        startActivity(intent);
        sessionManager.deleteCart();
        finish();
    }
}