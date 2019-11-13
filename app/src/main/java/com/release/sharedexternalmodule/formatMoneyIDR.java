package com.release.sharedexternalmodule;

import android.icu.text.NumberFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class formatMoneyIDR {
    public static String convertIDR(String money){
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

    public static String convertString(String money){
        money = money.replaceAll("^0*([0-9]+).*", "$1");
        return money;
    }


    public static String reverseIDR(Double money){
        String result = "";
        String reverse_money = "";
        Boolean check_comma = result.contains(",");
        if(check_comma){
            reverse_money = String.valueOf(money).split(",")[0];
            result = reverse_money;
        }else{
            result = String.valueOf(money);
        }
        return result;
    }


    public static String reverseIDR(String money){
        String result = "";
        String reverse_money = "";
        Boolean check_comma = result.contains(",");
        if(check_comma){
            reverse_money = String.valueOf(money).split(",")[0];
            result = reverse_money;
        }else{
            result = String.valueOf(money);
        }
        return result;
    }



}
