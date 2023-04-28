package com.postkudigital.app.fragment.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.ImagePrintable;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.postkudigital.app.R;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import java.util.ArrayList;

public class PrinterActivity extends AppCompatActivity {
    private Context context;
    private SessionManager sessionManager;
    private ImageView backButton;
    private TextView caption, namaPrinter;
    private Button btnScan, btnUnpair, btnTest;
    private Printing printing = null;
    PrintingCallback printingCallback=null;
    private LinearLayout lempty, lprinter;
    private byte FONT_SMALL = 5;
    private byte LINE_SPACING_1 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        context = this;
        sessionManager = new SessionManager(context);
        backButton = findViewById(R.id.back_button);
        caption = findViewById(R.id.text_caption);
        btnScan = findViewById(R.id.button5);
        btnTest = findViewById(R.id.button9);
        namaPrinter = findViewById(R.id.text_nama_printer);
        btnUnpair = findViewById(R.id.btn_unpair);
        lempty = findViewById(R.id.lempty);
        lprinter = findViewById(R.id.lprinter);

        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();


        caption.setText("Printer");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Printooth.INSTANCE.hasPairedPrinter()) {Printooth.INSTANCE.removeCurrentPrinter();
                } else {
                    startActivityForResult(new Intent(context, ScanningActivity.class ),ScanningActivity.SCANNING_FOR_PRINTER);

                }
            }
        });

        btnUnpair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Printooth.INSTANCE.hasPairedPrinter()) {
                    Printooth.INSTANCE.removeCurrentPrinter();
                    initViews();
                }
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(printing != null) {
                    printing.print(getSomePrintables());
                }else {
                    Toast.makeText(getApplicationContext(), "no print", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initViews();
        initListeners();



    }

    private ArrayList<Printable> getSomePrintables() {
        ArrayList<Printable> al = new ArrayList<>();
        Resources resources = getResources();
        al.add(new RawPrintable.Builder(new byte[]{27, 100, 4}).build()); // feed lines example in raw mode

        Bitmap image = BitmapFactory.decodeResource(resources, R.drawable.img_logo_print);
        al.add(new ImagePrintable.Builder(image)
                .setNewLinesAfter(1)
                .build());

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
                .setText("Kasir" + String.format("%" + lenKasir + "s", "Joko Susantor"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenMetode = 32 - 12;
        al.add( (new TextPrintable.Builder())
                .setText("Metode bayar" + String.format("%" + lenMetode + "s", "QRIS"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenWaktu = 32 - 5;
        al.add( (new TextPrintable.Builder())
                .setText("Waktu" + String.format("%" + lenWaktu + "s", "2021-01-13, 13:00"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenInv = 32 - 3;
        al.add( (new TextPrintable.Builder())
                .setText("Inv" + String.format("%" + lenInv + "s", "PKU-20210807094240"))
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

        al.add( (new TextPrintable.Builder())
                .setText("La Burger Charlos Jr")
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenDisc = 32 - 9;
        al.add( (new TextPrintable.Builder())
                .setText("Rp5000(2)" + String.format("%" + lenDisc + "s", "Rp7.500"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        al.add( (new TextPrintable.Builder())
                .setText("Disc: - Rp 2.500")
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

        int lenPpn = 32 - 7;
        al.add( (new TextPrintable.Builder())
                .setText("Pajak" + String.format("%" + lenPpn + "s", "Rp1.700"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenDiscTot = 32 - 8;
        al.add( (new TextPrintable.Builder())
                .setText("Discount" + String.format("%" + lenDiscTot + "s", "Rp2.000"))
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
                .setText("Grand Total" + String.format("%" + lenGrandTot + "s", "Rp20.000"))
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
                .setText("Bayar" + String.format("%" + lenBayar + "s", "Rp20.000"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(LINE_SPACING_1)
                .setNewLinesAfter(1)
                .build());

        int lenKembali = 32 - 9;
        al.add( (new TextPrintable.Builder())
                .setText("Kembalian" + String.format("%" + lenDiscTot + "s", "Rp0"))
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_LEFT())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC850())
                .setFontSize(FONT_SMALL)
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_30())
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
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_30())
                .setFontSize(FONT_SMALL)
                .setNewLinesAfter(1)
                .build());

        return al;
    }

    private void initViews() {
        if (Printooth.INSTANCE.getPairedPrinter()!=null){
            lempty.setVisibility(View.GONE);
            lprinter.setVisibility(View.VISIBLE);
            btnTest.setVisibility(View.VISIBLE);
            namaPrinter.setText(Printooth.INSTANCE.getPairedPrinter().getName());

        }else {
            lempty.setVisibility(View.VISIBLE);
            lprinter.setVisibility(View.GONE);
            btnTest.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("SCANPRINTER", "onActivityResult "+requestCode);

        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK) {
            initListeners();
//            printSomePrintable();
        }
        initViews();
    }

    private void initListeners() {
        if (printing!=null && printingCallback==null) {
            Log.d("xxx", "initListeners ");
            printingCallback = new PrintingCallback() {

                public void connectingWithPrinter() {
                    Toast.makeText(getApplicationContext(), "Connecting with printer", Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "Connecting");
                }
                public void printingOrderSentSuccessfully() {
                    Toast.makeText(getApplicationContext(), "printingOrderSentSuccessfully", Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "printingOrderSentSuccessfully");
                }
                public void connectionFailed(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "connectionFailed :"+error, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "connectionFailed : "+error);
                }
                public void onError(@NonNull String error) {
                    Toast.makeText(getApplicationContext(), "onError :"+error, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "onError : "+error);
                }
                public void onMessage(@NonNull String message) {
                    Toast.makeText(getApplicationContext(), "onMessage :" +message, Toast.LENGTH_SHORT).show();
                    Log.e("xxx", "onMessage : "+message);
                }
            };

            Printooth.INSTANCE.printer().setPrintingCallback(printingCallback);
        }
    }
}