package com.release.sharedexternalmodule;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderCore {
    private int day = 0;
    private Class service;
    private Class broadcast_rec;
    private Context context;
    private String message;
    private Integer req_code;
    private PendingIntent pendingIntent;

    // method overloading/ overriding

    public ReminderCore(Context context, Integer day, Class service){
        this.context = context;
        this.day = day;
        this.service = service;
    }

    public ReminderCore(Context context, Integer day, Class service, String message){
        this.context = context;
        this.day = day;
        this.service = service;
        this.message = message;
    }

    // Alarm Manager
    public ReminderCore(Context context, Class broadcast_rec, Integer req_code, PendingIntent pendingIntent){
        this.context = context;
        this.broadcast_rec = broadcast_rec;
        this.req_code = req_code;
        this.pendingIntent = pendingIntent;
    }

    public ReminderCore(Context context, Class service, String message){
        this.context = context;
        this.service = service;
        this.message = message;
    }

    public void run(){
        if(this.day == 0){
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            final Date d = new Date();
            final String dayOfTheWeek = sdf.format(d);
            int day = Integer.parseInt(dayOfTheWeek);
            if (day == 2 || day == 5 || day == 20 || day == 25 || day == 30){
                this.message = this.message.equals("") ? "Jangan lupa update progres ya" : this.message;
                Intent serviceIntent = new Intent(context, service);
                serviceIntent.putExtra("inputExtra", this.message);
                ContextCompat.startForegroundService(context, serviceIntent);
            }
        }else{
            //        String input =  "Jangan lupa untuk update progres pekerjaan";
            this.message = this.message.equals("") ? "Jangan lupa update progres ya" : this.message;
            Intent serviceIntent = new Intent(context, service);
            serviceIntent.putExtra("inputExtra", this.message);
            ContextCompat.startForegroundService(context, serviceIntent);
        }
    }



    public void start(){

    }

    public void stop(){

    }



}
