package com.release.sharedexternalmodule;

import android.icu.text.NumberFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class formatMoneyIDR {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String convertIDR(String money){
        if(money.equals("") || money.equals("-")){
            return  "0";
        }else{
            Double uang = Double.valueOf(money);
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);
            String convert = kursIndonesia.format(uang);
            return convert;
        }
    }

    public static String convertString(String money){
        money = money.replaceAll("^0*([0-9]+).*", "$1");
        return money;
    }



}
