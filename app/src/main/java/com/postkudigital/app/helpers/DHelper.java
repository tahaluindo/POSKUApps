package com.postkudigital.app.helpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DHelper {
    public static Double d(String s){
        Double localDouble1 = Double.valueOf(0.0D);
        try {
            Double localDouble2 = Double.valueOf(Double.parseDouble(s));
            return localDouble2;
        } catch (Exception localException) {
        }
        return localDouble1;
    }

    public static void pesan(final Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String toformatRupiah(String s){
        Locale localeID = new Locale("in", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(localeID);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        decimalFormatSymbols.setMonetaryDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat.format(d(s)).replace(",", ".");
    }

    public static String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.setMaximumFractionDigits(0);
        return formatRupiah.format(number).replace(",", ".");
    }

    public static boolean isValidEmail(EditText email) {
        boolean isValid = false;
        String expresion = "^[\\\\w\\\\.-]+@([\\\\w\\\\-]+\\\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email.getText().toString();

        Pattern pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()){
            isValid = true;
        }
        return isValid;
    }

    public static void isLoading(boolean load, TextView textView, ProgressBar progressBar){
        if(load){
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public static boolean isCompare(EditText etText, EditText ex){
        String a = etText.getText().toString();
        String b = ex.getText().toString();
        return !a.equals(b);
    }

    public static Date strToDateFull(String data) {
        DateFormat df = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
        Date startDate = null;
        String newDateString = "";
        try {
            startDate = df.parse(data);
            // newDateString = df.format(startDate);
            System.out.println("newdate : " + startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }
    public static Date dateAddYear(Date in, int tahun) {
        if (in == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(in);
        cal.add(Calendar.YEAR, tahun);
        return cal.getTime();

    }
    public static String tglSekarang() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Date strTodate(String data) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date startDate = null;
        String newDateString = "";
        try {
            startDate = df.parse(data);
            // newDateString = df.format(startDate);
            System.out.println("newdate : " + startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }

    public static String strTodatetime(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy, hh:mm", Locale.getDefault());
        return df.format(data);
    }

    public static String strToSimpleDate(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return df.format(data);
    }

    public static boolean isBatasUsia(String etText, int batas) {
//        String a = etText.getText().toString();

        Date usia = strTodate(etText);
        Date sekarang = dateAddYear(strTodate(tglSekarang()), batas);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (usia.before(sekarang)) {
            return false;
        } else {
            return true;
        }

    }

    public static File createTempFile(Context context, Bitmap bitmap) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.webp");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.WEBP,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String calculateFileSize(Uri filepath)
    {
        //String filepathstr=filepath.toString();
        File file = new File(filepath.getPath());

        // Get length of file in bytes
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;

        String calString= Float.toString(fileSizeInMB);
        return calString;
    }

    public static float getImageSize(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            float imageSize = cursor.getLong(sizeIndex);
            cursor.close();
            return imageSize; // returns size in bytes
        }
        return 0;
    }

}
