package com.release.sharedexternalmodule;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateInfo {
    public DateInfo(){

    }

    public static String dateTime(){
        String date_result = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date_result = formatter.format(calendar.getTime());
        return date_result;
    }
}
