package com.release.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.release.R;
import com.release.dropbox.DropboxActivity;
import com.release.dropbox.UserActivity;
import com.release.model.Anggaran;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponsePAS;
import com.release.model.DataResponseUsers;
import com.release.model.PaketDashboard;
import com.release.model.DataResponsePA;
import com.release.model.PaketDashboardSuper;
import com.release.model.User;
import com.release.receiver.NotificationPublisher;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.service.ServiceReminder;
import com.release.sharedexternalmodule.ReminderCore;
import com.release.sharedpreferences.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import com.release.sharedexternalmodule.formatMoneyIDR;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDashboard extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    private Button show_list; // button paket fisik
    private Button show_list2; // button anggaran
    private Button show_list4;
    private Button show_list3; // button daftar dinas
    private Button btn_mapdash;
    private Button btn_chartdash;
    private static String TAG = "ActivityDashboard";

    SessionManager sessionManager;
    ProgressDialog progress;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    private TextView tx_dashpagu;
    private TextView tx_dashreal;
    private TextView tx_dashsisa;
    private TextView tx_dashtotalpaket;
    private TextView tx_dashongoing;
    private TextView tx_dashbelum;
    private TextView tx_dashselesai;
    private TextView tx_dashtotalnonfisik;
    private TextView tx_dashpagu_dummy;
    private TextView tx_dashreal_dummy;
    private TextView tx_datecalendar;
    private TextView tx_namauser;
    private TextView tx_dashtotalpaket_nonfisik;

    private Date date;

    private LinearLayout container_dashboards;

    private String total_paket;
    private String total_progress;
    private String total_paket_belum;
    private String role;
    private String dinas_id;
    private String user_id;
    private String daerah_id;
    private String bi_id;
    private String user_fullname;
    private String user_name;
    private Handler mHandler;
    private  ProgressDialog progressDialog;
    private ScrollView main_container_dashbaord;

    private LinearLayout linear_calendar;

    public ActivityDashboard(){}
    final Calendar myCalendar = Calendar. getInstance () ;

    ReminderCore reminderCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_scafol_logo3);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_dashboard);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        role = user.get(SessionManager.KEY_ROLE);
        dinas_id =  user.get(SessionManager.KEY_DINASID);
        user_id =  user.get(SessionManager.KEY_USERID);
        daerah_id = user.get(SessionManager.KEY_DAERAH_ID);
        bi_id = user.get(SessionManager.KEY_BIDANG);
        user_fullname = user.get(SessionManager.KEY_NAME);
        String user_name = user.get(SessionManager.KEY_USERNAME);
        show_list = findViewById(R.id.btn_tolist);
        show_list2 = findViewById(R.id.btn_tolist2);
        show_list4 = findViewById(R.id.btn_tolist4);
        show_list3 = findViewById(R.id.btn_tolist3);
        main_container_dashbaord = findViewById(R.id.main_container_dashbaord);
        progress = new ProgressDialog(this);
        final View parentLayout = findViewById(android.R.id.content);

        LineChart chart = findViewById(R.id.dashchart);
        chart.setVisibility(View.GONE);

        final String default_format_nomoney = "Rp. " +  formatMoneyIDR.convertIDR("0");
        tx_dashtotalpaket = findViewById(R.id.tx_dashtotalpaket);
        tx_dashongoing = findViewById(R.id.tx_dsahongoing);
        tx_dashpagu = findViewById(R.id.tx_dashpagu);
        tx_dashreal = findViewById(R.id.tx_dashreal);
        tx_dashsisa = findViewById(R.id.tx_dashsisa);
        tx_datecalendar = findViewById(R.id.tx_datecalendar);
        tx_namauser = findViewById(R.id.tx_namauser);
        tx_dashbelum = findViewById(R.id.tx_dashpaketbelum);
        tx_dashselesai = findViewById(R.id.tx_dashpaketselesai);
        btn_mapdash = findViewById(R.id.btn_mapdash);
        btn_chartdash = findViewById(R.id.btn_chartdash);
        linear_calendar = findViewById(R.id.linear_calendar);
        tx_dashtotalpaket_nonfisik = findViewById(R.id.tx_dashtotalpaket_nonfisik);

        container_dashboards = findViewById(R.id.container_dashboards);
        container_dashboards.setVisibility(View.GONE);

        date = Calendar.getInstance().getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        String date_result = format1.format(date);
        Log.d(TAG, "Date today " + date_result);
        tx_datecalendar.setText(date_result);


        reminderCore = new ReminderCore(getApplicationContext(), ServiceReminder.class, "Silahkan Update Progress");
        reminderCore.run();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");

        main_container_dashbaord.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(ActivityDashboard.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();


        Call<DataResponseUsers> callgetUsers = apiInterface.getUserByName(user_name);
        callgetUsers.enqueue(new Callback<DataResponseUsers>() {
            @Override
            public void onResponse(Call<DataResponseUsers> call, Response<DataResponseUsers> response) {
                if(response.code() == 200){
                    ArrayList<User> result = response.body().getData();
                    for(int i = 0; i < result.size(); i++){
                        tx_namauser.setText("" + result.get(i).getNama());
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponseUsers> call, Throwable t) {

            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //To do//
                            return;
                        }

                        // Get the Instance ID token//
                        String token = task.getResult().getToken();
                        String msg = "FCM TOKEN " + token;
                        Log.d(TAG, msg);

                    }
                });

        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        progressDialog.dismiss();
                        main_container_dashbaord.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        if(sessionManager.isLoggedIn()){
            // total paket count
            dialog.show();
            if(role.toLowerCase().equals("pptk")){

                Call<DataResponsePA> callinfopaketpptk = apiInterface.infoPaketPPTK(user_id);
                callinfopaketpptk.enqueue(new Callback<DataResponsePA>() {
                            @Override
                            public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                                if(response.code() == 200){
                                    ArrayList<PaketDashboard> result = response.body().getData();
                                    String paket_all = "";
                                    String paket_progress = "";
                                    String paket_belum = "";
                                    String paket_selesai = "";
                                    for(int i = 0; i < result.size(); i++){
                                        paket_all = result.get(i).getPaketAll();
                                        paket_progress = result.get(i).getPaketProgress();
                                        paket_belum = result.get(i).getPaketBelumMulai();
                                        paket_selesai = result.get(i).getPaketSelesai();

                                        tx_dashtotalpaket.setText(paket_all + "");
                                        tx_dashongoing.setText(paket_progress + "");
                                        tx_dashbelum.setText(paket_belum + "");
                                        tx_dashselesai.setText(paket_selesai + "");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataResponsePA> call, Throwable t) {

                            }
                        });

                // call total pagu
                Call<DataResponsePA> calltotalpagu = apiInterface.countPaguPPTK(user_id);
                Log.d(TAG, "paket datas " + user_id);
                calltotalpagu.enqueue(new Callback<DataResponsePA>() {
                            @Override
                            public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                                if(response.code() == 200){
                                    ArrayList<PaketDashboard> result = response.body().getData();
                                    for(int i = 0; i < result.size(); i++){
                                        Log.d(TAG, "paket size all : " + result.get(i).getTotalPaguPPTK());
                                        total_progress =  result.get(i).getPaketProgress();
                                        String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalPaguPPTK());
                                        tx_dashpagu.setText(reformat);
                                    }
                                }else{
                                    tx_dashpagu.setText(default_format_nomoney);
                                }
                            }
                            @Override
                            public void onFailure(Call<DataResponsePA> call, Throwable t) {

                            }
                        });

                // call total realisasi
                Call<DataResponsePA> callreal = apiInterface.countRealPPTK(user_id);
                Log.d(TAG, "paket datas " + user_id);
                callreal.enqueue(new Callback<DataResponsePA>() {
                            @Override
                            public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                                if(response.code() == 200){
                                    ArrayList<PaketDashboard> result = response.body().getData();
                                    for(int i = 0; i < result.size(); i++){
                                        Log.d(TAG, "paket size all : " + result.get(i).getTotalRealPPTK());
                                        total_progress =  result.get(i).getTotalRealPPTK();
                                        String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalRealPPTK());
                                        tx_dashreal.setText(reformat);
                                    }
                                }else{
                                    tx_dashreal.setText(default_format_nomoney);
                                }
                            }
                            @Override
                            public void onFailure(Call<DataResponsePA> call, Throwable t) {

                            }
                        });

                // call total sisa
                Call<DataResponsePA> callsisa = apiInterface.countSisaPPTK(user_id);
                callsisa.enqueue(new Callback<DataResponsePA>() {
                            @Override
                            public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                                if(response.code() == 200){
                                    ArrayList<PaketDashboard> result = response.body().getData();
                                    for(int i = 0; i < result.size(); i++){
                                        Log.d(TAG, "paket size all : " + result.get(i).getTotalRealPPTK());
                                        String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalSisa());
                                        tx_dashsisa.setText(reformat);
                                    }
                                }else{
                                    tx_dashsisa.setText(default_format_nomoney);
                                }
                            }

                            @Override
                            public void onFailure(Call<DataResponsePA> call, Throwable t) {

                            }
                        });

                // call count anggaran di pptk
                Call<DataResponseAnggaran> count_anggaran_bidang = apiInterface.getAnggaranPPTK(user_id);
                count_anggaran_bidang.enqueue(new Callback<DataResponseAnggaran>() {
                            @Override
                            public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                                Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                                String response_code = new Gson().toJson(response.code()).toString();
                                if(response_code.equals("200")){
                                    ArrayList<Anggaran> result = null;
                                    result = (response.body().getData() == null) ?  null : response.body().getData();
                                    if(result != null){
                                        tx_dashtotalpaket_nonfisik.setText(String.valueOf(response.body().getData().size()));
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {

                            }
                        });

                // set marker
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendMessage(Message.obtain(mHandler, 1));
                    }
                }).start();
            }else if(role.toLowerCase().equals("admin")){
                // admin page goes heres
                show_list.setVisibility(View.VISIBLE);

                // call info pakte
                Call<DataResponsePA> callinfopaketpptk = apiInterface.infoPaketAdmin(dinas_id);
                callinfopaketpptk.enqueue(new Callback<DataResponsePA>() {
                    @Override
                    public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                        if(response.code() == 200){
                            ArrayList<PaketDashboard> result = response.body().getData();
                            String paket_all = "";
                            String paket_progress = "";
                            String paket_belum = "";
                            String paket_selesai = "";
                            for(int i = 0; i < result.size(); i++){
                                paket_all = result.get(i).getPaketAll();
                                paket_progress = result.get(i).getPaketProgress();
                                paket_belum = result.get(i).getPaketBelumMulai();
                                paket_selesai = result.get(i).getPaketSelesai();
                                tx_dashtotalpaket.setText(paket_all + "");
                                tx_dashongoing.setText(paket_progress + "");
                                tx_dashbelum.setText(paket_belum + "");
                                tx_dashselesai.setText(paket_selesai + "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePA> call, Throwable t) {

                    }
                });


                // call total pagu
                    Call<DataResponsePA> calltotalpagu = apiInterface.countPaguAdmin(dinas_id);
                    Log.d(TAG, "paket datas " + user_id);
                    calltotalpagu.enqueue(new Callback<DataResponsePA>() {
                        @Override
                        public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                            Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<PaketDashboard> result = response.body().getData();
                                if(result != null){
                                    for(int i = 0; i < result.size(); i++){
                                        Log.d(TAG, "paket size all : " + result.get(i).getTotalPaguPPTK());
                                        total_progress =  result.get(i).getPaketProgress();
                                        String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalPaguPPTK());
                                        tx_dashpagu.setText(reformat);
                                    }
                                }
                            }else{
                                tx_dashpagu.setText(default_format_nomoney);
                            }
                        }
                        @Override
                        public void onFailure(Call<DataResponsePA> call, Throwable t) {

                        }
                    });


                    // call total realisasi
                    Call<DataResponsePA> callreal = apiInterface.countRealuAdmin(dinas_id);
                    Log.d(TAG, "paket datas " + user_id);
                    callreal.enqueue(new Callback<DataResponsePA>() {
                        @Override
                        public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                            Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<PaketDashboard> result = null;
                                result = (response.body().getData() == null) ?  null : response.body().getData();
                                if(result != null){
                                    for(int i = 0; i < result.size(); i++){
                                        Log.d(TAG, "paket size all : " + result.get(i).getTotalRealPPTK());
                                        total_progress =  result.get(i).getTotalRealPPTK();
                                        String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalRealPPTK());
                                        tx_dashreal.setText(reformat);
                                    }
                                }
                            }else{
                                tx_dashreal.setText(default_format_nomoney);
                            }
                        }
                        @Override
                        public void onFailure(Call<DataResponsePA> call, Throwable t) {

                        }
                    });


                    // call total sisa anggaran
                    Call<DataResponsePA> callsisa = apiInterface.countSisaAdmin(dinas_id);
                    callsisa.enqueue(new Callback<DataResponsePA>() {
                        @Override
                        public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                            Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<PaketDashboard> result = null;
                                result = (response.body().getData() == null) ?  null : response.body().getData();
                                if(result != null){
                                    for(int i = 0; i < result.size(); i++){
                                        Log.d(TAG, "paket size all : " + result.get(i).getTotalSisa());
                                        String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalSisa());
                                        tx_dashsisa.setText(reformat);
                                    }
                                }
                            }else{
                                tx_dashsisa.setText(default_format_nomoney);
                            }
                        }
                        @Override
                        public void onFailure(Call<DataResponsePA> call, Throwable t) {

                        }
                    });

                    // call count anggaran di admind
                    Call<DataResponseAnggaran> count_anggaran_admin = apiInterface.getAnggaranAdmin(dinas_id);
                    count_anggaran_admin.enqueue(new Callback<DataResponseAnggaran>() {
                        @Override
                        public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                            Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Anggaran> result = null;
                                result = (response.body().getData() == null) ?  null : response.body().getData();
                                if(result != null){
                                   tx_dashtotalpaket_nonfisik.setText(String.valueOf(response.body().getData().size()));
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {

                        }
                    });


                    // set marker
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mHandler.sendMessage(Message.obtain(mHandler, 1));
                        }
                    }).start();
            }else if(role.toLowerCase().equals("bidang")){
                show_list.setVisibility(View.VISIBLE);

                // call total pagu bidang
                Call<DataResponsePA> calltotalpagu = apiInterface.countPaguBidang(bi_id);
                calltotalpagu.enqueue(new Callback<DataResponsePA>() {
                    @Override
                    public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                        Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                        String response_code = new Gson().toJson(response.code()).toString();
                        if(response_code.equals("200")){
                            ArrayList<PaketDashboard> result = response.body().getData();
                            if(result != null){
                                for(int i = 0; i < result.size(); i++){
                                    String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalPaguPPTK());
                                    tx_dashpagu.setText(reformat);
                                }
                            }
                        }else{
                            tx_dashpagu.setText(default_format_nomoney);
                        }
                    }
                    @Override
                    public void onFailure(Call<DataResponsePA> call, Throwable t) {

                    }
                });

                // call total realisasi
                Call<DataResponsePA> callreal = apiInterface.countRealBidang(bi_id);
                callreal.enqueue(new Callback<DataResponsePA>() {
                    @Override
                    public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                        if(response.code() == 200){
                            ArrayList<PaketDashboard> result = response.body().getData();
                            for(int i = 0; i < result.size(); i++){
                                Log.d(TAG, "paket size all : " + result.get(i).getTotalRealPPTK());
                                String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalRealPPTK());
                                tx_dashreal.setText(reformat);
                            }
                        }else{
                            tx_dashreal.setText(default_format_nomoney);
                        }
                    }
                    @Override
                    public void onFailure(Call<DataResponsePA> call, Throwable t) {

                    }
                });

                // call total sisa
                Call<DataResponsePA> callsisa = apiInterface.countSisaBidang(bi_id);
                callsisa.enqueue(new Callback<DataResponsePA>() {
                    @Override
                    public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                        if(response.code() == 200){
                            ArrayList<PaketDashboard> result = response.body().getData();
                            for(int i = 0; i < result.size(); i++){
                                Log.d(TAG, "paket size all : " + result.get(i).getTotalRealPPTK());
                                String reformat = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalSisa());
                                tx_dashsisa.setText(reformat);
                            }
                        }else{
                            tx_dashreal.setText(default_format_nomoney);
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePA> call, Throwable t) {

                    }
                });


                Call<DataResponsePA> callinfopaketbidang = apiInterface.infoPaketBidang(bi_id);
                callinfopaketbidang.enqueue(new Callback<DataResponsePA>() {
                    @Override
                    public void onResponse(Call<DataResponsePA> call, Response<DataResponsePA> response) {
                        if(response.code() == 200){
                            Log.d(TAG, "RESPONSE BIDANG : " + new Gson().toJson(response.body().getData()));
                            ArrayList<PaketDashboard> result = response.body().getData();
                            String paket_all = "";
                            String paket_progress = "";
                            String paket_belum = "";
                            String paket_selesai = "";
                            for(int i = 0; i < result.size(); i++){
                                paket_all = result.get(i).getPaketAll();
                                paket_progress = result.get(i).getPaketProgress();
                                paket_belum = result.get(i).getPaketBelumMulai();
                                paket_selesai = result.get(i).getPaketSelesai();

                                tx_dashtotalpaket.setText(paket_all + "");
                                tx_dashongoing.setText(paket_progress + "");
                                tx_dashbelum.setText(paket_belum + "");
                                tx_dashselesai.setText(paket_selesai + "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePA> call, Throwable t) {
                        Log.d(TAG, "RESPONSE GAGAL : " + call);
                    }
                });


                // call count anggaran di admind
                Call<DataResponseAnggaran> count_anggaran_bidang = apiInterface.getAnggaranBidang(bi_id, dinas_id);
                count_anggaran_bidang.enqueue(new Callback<DataResponseAnggaran>() {
                    @Override
                    public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                        Log.d(TAG, "RESPONSE " + new Gson().toJson(response.code()));
                        String response_code = new Gson().toJson(response.code()).toString();
                        if(response_code.equals("200")){
                            ArrayList<Anggaran> result = null;
                            result = (response.body().getData() == null) ?  null : response.body().getData();
                            if(result != null){
                                tx_dashtotalpaket_nonfisik.setText(String.valueOf(response.body().getData().size()));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {

                    }
                });


                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendMessage(Message.obtain(mHandler, 1));
                    }
                }).start();
            }else if(role.toLowerCase().equals("superadmin")){
                Toast.makeText(getApplicationContext(), "Daerah id : " + daerah_id, Toast.LENGTH_LONG);
                Log.d(TAG, "Daerah id : " + daerah_id);
                show_list4.setVisibility(View.GONE);
                show_list.setVisibility(View.GONE);
                show_list2.setVisibility(View.GONE);
                show_list3.setVisibility(View.VISIBLE);

                Call<DataResponsePAS> callinfopaket_super = apiInterface.infoPaketSuper(daerah_id);
                callinfopaket_super.enqueue(new Callback<DataResponsePAS>() {
                    @Override
                    public void onResponse(Call<DataResponsePAS> call, Response<DataResponsePAS> response) {
                        if(response.code() == 200){
                            ArrayList<PaketDashboardSuper> result = response.body().getData();
                            String paket_all = "";
                            String paket_progress = "";
                            String paket_belum = "";
                            String paket_selesai = "";
                            for(int i = 0; i < result.size(); i++){
                                paket_all = result.get(i).getTotalPaket();
                                paket_progress = result.get(i).getPaketProgress();
                                paket_belum = result.get(i).getPaketBelumMulai();
                                paket_selesai = result.get(i).getPaketSelesai();
                                String reformat1 = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalPaguDaerah());
                                String reformat2 = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getSisaPaguDaerah());
                                String reformat3 = "Rp. " +  formatMoneyIDR.convertIDR(result.get(i).getTotalRealDaerah());
                                tx_dashpagu.setText(reformat1);
                                tx_dashsisa.setText(reformat2);
                                tx_dashreal.setText(reformat3);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePAS> call, Throwable t) {

                    }
                });


                Call<DataResponsePAS> callinfopaketsuper_detail = apiInterface.infoPaketDetailSuper(daerah_id);
                callinfopaketsuper_detail.enqueue(new Callback<DataResponsePAS>() {
                    @Override
                    public void onResponse(Call<DataResponsePAS> call, Response<DataResponsePAS> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Ini response " + response.code(), Toast.LENGTH_LONG );
                            Log.d(TAG, "Ini response : " + response.code());
                            ArrayList<PaketDashboardSuper> result = response.body().getData();
                            String paket_all = "";
                            String paket_progress = "";
                            String paket_belum = "";
                            String paket_selesai = "";
                            for(int i = 0; i < result.size(); i++){
                                paket_all = result.get(i).getTotalPaket();
                                paket_progress = result.get(i).getPaketProgress();
                                paket_belum = result.get(i).getPaketBelumMulai();
                                paket_selesai = result.get(i).getPaketSelesai();
                                tx_dashtotalpaket.setText(paket_all + "");
                                tx_dashongoing.setText(paket_progress + "");
                                tx_dashbelum.setText(paket_belum + "");
                                tx_dashselesai.setText(paket_selesai + "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePAS> call, Throwable t) {

                    }
                });

                show_list3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivityDashboard.this, "Klik daftar dinas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ActivityDinas.class);
                        intent.putExtra("daerah_id", daerah_id);
                        startActivity(intent);
                    }
                });


                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHandler.sendMessage(Message.obtain(mHandler, 1));
                    }
                }).start();
            }

            btn_mapdash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ActivityMapDashboard.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            });

            btn_chartdash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(getApplicationContext(), LineChartActivity1.class);
                    intent2.putExtra("user_id", user_id);
                    startActivity(intent2);
                }
            });

            dialog.dismiss();
        }

        tx_dashbelum.setText("0");
        tx_dashongoing.setText("0");
        tx_dashselesai.setText("0");

        // onclick activity paket fisik
        show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                intent.putExtra("flag_list", "1");
                startActivity(intent);
            }
        });

        // onclick activity anggaran/ paket non fisik
        show_list2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                intent.putExtra("flag_list", "2");
                startActivity(intent);
            }
        });

        // onclick activity kegiatan tree
        show_list4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                intent.putExtra("flag_list", "3");
                startActivity(intent);
            }
        });

        container_dashboards.setVisibility(View.VISIBLE);
        if(container_dashboards.getVisibility() == View.VISIBLE){
        }


        linear_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendarDialog();
            }
        });
        Date date = myCalendar.getTime();
        scheduleNotification(getNotification("Jangan lupa update progres anda"), date.getTime());
    }

    public void startServiceReminder(){
        Intent serviceIntent = new Intent(this, ServiceReminder.class);
        serviceIntent.putExtra("inputExtra", "Let's update our progress");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, NotificationPublisher. class ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(getApplication(). ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;
    }

    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout :
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Apakah anda ingin keluar ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                sessionManager.logoutUser();
                                preferences = getSharedPreferences("dropbox-sample", getApplicationContext().MODE_PRIVATE);
                                editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "Aborting mission...");
                            }
                        })
                        .show();
                break;
            case R.id.nav_search :
                break;
            case R.id.nav_fragment :
                Intent dashfrag = new Intent(this, ActivityDashboardFragment.class);
                startActivity(dashfrag);
                break;
            case R.id.nav_uploaddropbox :
                Intent dropbox_act = new Intent(this, UserActivity.class);
                startActivity(dropbox_act);
                break;
            case R.id.nav_profile :
                Intent intent2 = new Intent(ActivityDashboard.this, ActivityEditProfil.class);
                intent2.putExtra("user_id", user_id);
                startActivity(intent2);
                break;
            case R.id.nav_about :
                openBottomDialog();
                break;
            case R.id.nav_notif :
                Intent intent = new Intent(getApplicationContext(), ActivityNotif.class);
                startActivity(intent);
                return true;
            case R.id.nav_mylocation :
                Intent intent3 = new Intent(getApplicationContext(), ActivityMyLocation.class);
                startActivity(intent3);
                return  true;
            case R.id.nav_signature :
                Intent intent4 = new Intent(getApplicationContext(), ActivitySignature.class);
                startActivity(intent4);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        sessionManager.checkLogin();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void openBottomDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        Dialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void openCalendarDialog(){
        View view = getLayoutInflater().inflate(R.layout.activity_calendar, null);
        Dialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }


}
