package com.release.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.release.R;
import com.release.dropbox.FilesActivity;
import com.release.dropbox.FilesActivityDirect;
import com.release.dropbox.UserActivity;
import com.release.model.Bidang;
import com.release.model.DataResponse;
import com.release.model.DataResponseBidang;
import com.release.model.DataResponseKegiatan;
import com.release.model.DataResponsePJ;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.Kegiatan;
import com.release.model.KegiatanTree;
import com.release.model.Kontraktor;
import com.release.model.Paket;
import com.release.model.Progress;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.Kriteria;
import com.release.sharedexternalmodule.formatMoneyIDR;
import com.release.sharedpreferences.SessionManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailPaket extends AppCompatActivity {
    private static String TAG = "ActivityDetailPaket";
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private SupportMapFragment mapFragment;

    private TextView text_judul;
    private TextView text_jenis;
    private TextView text_tahun;
    private TextView text_pagu;
    private TextView text_pagu_dummy;

    private TextView text_satuan;
    private TextView text_volume;
    private EditText text_tanggal_mulai;
    private EditText text_tanggal_akhir;

    private TextView text_namapptk;
    private TextView text_emailpptk;
    private TextView text_telpptk;
    private TextView text_bidangpptk;
    private TextView text_kegiatan;

    private EditText text_nilaikontrak;
    private TextView text_progress;

    private TextView maps_caption;
    private TextView tx_lasptprog;

    private EditText text_dayaserap;
    private TextView text_sisakontrak;
    private TextView text_sisaanggaran;

    private TextView lin_texttarget;
    private TextView lin_textreal;
    private TextView lin_textdeviasi;
    private EditText text_nokontrak;

    private TextView sisa_waktukerja;
    private CardView card_info_pptk;

    private TextView text_penyediajasa;
    private ProgressBar progres_pekerjaan;

    private ScrollView main_layout_detail_paket;
    private Context mContext;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    int PICK_IMAGE_REQUEST = 1;

    private ProgressBar progressBar;
    private CardView cardView;
    ProgressDialog progressDialog;
    private Handler mHandler;
    private String role;
    private String user_id;
    private String id_paket;
    SessionManager sessionManager;

    BigInteger daya_serap_total;
    BigInteger sisa_kontrak_total;
    BigInteger sisa_anggaran_total;
    BigInteger pagu_total;

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        setContentView(R.layout.activity_paketdetailrev);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + "Informasi Detail Paket Fisik" + "</small>"));

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        role = user.get(SessionManager.KEY_ROLE);
        String dinas_id =  user.get(SessionManager.KEY_DINASID);
        user_id =  user.get(SessionManager.KEY_USERID);

        String bi_id = user.get(SessionManager.KEY_BIDANG);

        final String user_fullname = user.get(SessionManager.KEY_NAME);
        String user_name = user.get(SessionManager.KEY_USERNAME);

        card_info_pptk = findViewById(R.id.card3);
        text_namapptk = findViewById(R.id.text_namapptk);

        text_judul = findViewById(R.id.text_judul);
        text_jenis = findViewById(R.id.text_jenis);
        text_tahun = findViewById(R.id.text_tahun);
        text_pagu = findViewById(R.id.text_pagu);

        text_satuan = findViewById(R.id.text_satuan);
        text_volume = findViewById(R.id.text_volume);
        text_tanggal_mulai = findViewById(R.id.text_date_created);
        text_tanggal_akhir = findViewById(R.id.text_date_end);

        text_bidangpptk = findViewById(R.id.textbidangs);
        text_kegiatan = findViewById(R.id.text_kegjudul);

        text_penyediajasa = findViewById(R.id.text_penyediajasa);

        text_nilaikontrak = findViewById(R.id.prof_email);

        cardView = findViewById(R.id.map_cards);

        maps_caption = findViewById(R.id.tx_projectlocations);

        tx_lasptprog = findViewById(R.id.tx_lasptprog);

        text_dayaserap = findViewById(R.id.text_dayaserap);
        text_sisakontrak = findViewById(R.id.text_sisakontrak);
        text_sisaanggaran = findViewById(R.id.text_sisaanggaran);

        lin_texttarget = findViewById(R.id.lin_texttarget);
        lin_textreal = findViewById(R.id.lin_textreal);
        lin_textdeviasi = findViewById(R.id.lin_textdeviasi);

        text_nokontrak = findViewById(R.id.text_nokontrak);
        sisa_waktukerja = findViewById(R.id.sisa_waktukerja);
        text_emailpptk = findViewById(R.id.text_emailpptk);
        text_telpptk = findViewById(R.id.text_telpptk);
        main_layout_detail_paket = findViewById(R.id.main_layout_detail_paket);
        main_layout_detail_paket.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(ActivityDetailPaket.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");

        progres_pekerjaan = findViewById(R.id.progres_pekerjaan);



        Intent intent = getIntent();
        id_paket = intent.getStringExtra("pa_id");
        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
        final Intent intents = getIntent();
        final String id_paket = intents.getStringExtra("pa_id");
        final String nama_paket = intents.getStringExtra("pa_nama");
        final String pa_pagu = intents.getStringExtra("pa_pagu");
        final String ke_id = intents.getStringExtra("ke_id");

        final Intent intent2 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
        intent2.putExtra("pa_pagu", pa_pagu);
        intent2.putExtra("position", 0);
        intent2.putExtra("pa_id", id_paket);
        intent2.putExtra("pa_nama", nama_paket);
        intent2.putExtra("ke_id", ke_id);


        final Intent intent_keu = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
        intent_keu.putExtra("pa_pagu", pa_pagu);
        intent_keu.putExtra("position", 3);
        intent_keu.putExtra("pa_id", id_paket);
        intent_keu.putExtra("pa_nama", nama_paket);
        intent_keu.putExtra("ke_id", ke_id);

        if(role.equals("Admin")){
            text_nokontrak.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            text_nilaikontrak.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            text_tanggal_mulai.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            text_tanggal_akhir.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            text_dayaserap.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }


        text_nokontrak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (text_nokontrak.getRight() - text_nokontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        intent2.putExtra("flag_update", "1");
                        startActivity(intent2);
                        return true;
                    }
                }
                return false;
            }
        });

        text_nilaikontrak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (text_nokontrak.getRight() - text_nokontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        intent2.putExtra("flag_update", "2");
                        startActivity(intent2);
                        return true;
                    }
                }
                return false;
            }
        });

        text_tanggal_mulai.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (text_nokontrak.getRight() - text_nokontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        intent2.putExtra("flag_update", "3");
                        startActivity(intent2);
                        return true;
                    }
                }
                return false;
            }
        });

        text_tanggal_akhir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (text_nokontrak.getRight() - text_nokontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        intent2.putExtra("flag_update", "4");
                        startActivity(intent2);
                        return true;
                    }
                }
                return false;
            }
        });

        text_dayaserap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (text_nokontrak.getRight() - text_nokontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        intent_keu.putExtra("flag_update", "");
                        startActivity(intent_keu);
                        return true;
                    }
                }
                return false;
            }
        });

        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                Log.w(TAG, "Paket data" + new Gson().toJson(response.body().getData()));
                ArrayList<Paket> paketlist = response.body().getData();
                for(int i = 0; i < paketlist.size(); i++){
                    String name = paketlist.get(i).getPaJudul().trim();
                    String jenis = paketlist.get(i).getPaJenis().trim();
                    String tahun = paketlist.get(i).getPaTahun().trim();
                    String pagu = paketlist.get(i).getPaPagu().trim();
                    String satuan = paketlist.get(i).getPaSatuan().trim();
                    String volume = paketlist.get(i).getPaVolume().trim();
                    String pptk_id = paketlist.get(i).getPptkId();
                    String ke_id = paketlist.get(i).getKeId().trim();
                    String status = paketlist.get(i).getStatus().trim();
                    String tanggal_awal = checkData(paketlist.get(i).getPaAwalKontrak());
                    String tanggal_akhir = paketlist.get(i).getPaAkhirKontrak();
                    String nilai_kontrak = paketlist.get(i).getPaNilaiKontrak();
                    String lokasi_name = paketlist.get(i).getPaLokasi();
                    String nokontrak = paketlist.get(i).getPaNomorKontrak();

                    text_judul.setText(checkData(name));
                    text_jenis.setText(checkData(jenis));
                    text_tahun.setText(checkData(tahun));
                    text_pagu.setText("Rp. " + formatMoneyIDR.convertIDR(pagu));
                    lokasi_name = lokasi_name.equals("")? "Lokasi tidak diset" : lokasi_name;
                    maps_caption.setText(lokasi_name + "");
                    text_satuan.setText(checkData(satuan));
                    text_volume.setText(checkData(volume));
                    text_nokontrak.setText(checkData(nokontrak));

                    String result1 = "";
                    String[] result_temp1;

                    String result2 = "";
                    String[] result_temp2;

                    Log.d(TAG, tanggal_awal + "" );
                    Log.d(TAG, tanggal_akhir + "" );

                    if(tanggal_awal == null){
                        result1 = "-";
                    }else{
                        result_temp1 = tanggal_awal.split(" ");
                        result1 = result_temp1[0].trim();
                    }

                    if(tanggal_akhir == null){
                        result2 = "-";
                    }else{
                        result_temp2 = tanggal_akhir.split(" ");
                        result2 = result_temp2[0].trim();
                    }

                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddd", Locale.FRANCE);

                        String str_curDate = sdf.format(new Date());
                        Date currentDate = sdf.parse(str_curDate);
                        Date secondDate = sdf.parse(result2);

                        long diffInMillies = Math.abs(secondDate.getTime() - currentDate.getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//                        float diff = (diffInMillies / (1000*60*60*24));

                        Log.d(TAG, "Date of activity detail =========> " + diff);
                        sisa_waktukerja.setText("Sisa Waktu Pekerjaan : " + diff + " hari");
                    }catch (ParseException e){
                        e.printStackTrace();
                    }

                    text_tanggal_mulai.setText(result1);
                    text_tanggal_akhir.setText(result2);
                    text_nilaikontrak.setText("Rp. " + formatMoneyIDR.convertIDR(nilai_kontrak));


                    // get bidang
                    Call<DataResponseBidang> call_bidanginfo = apiInterface.getBidang(ke_id);
                    call_bidanginfo.enqueue(new Callback<DataResponseBidang>() {
                        @Override
                        public void onResponse(Call<DataResponseBidang> call, Response<DataResponseBidang> response) {
                            Log.d(TAG, "BIDANG response : " + new Gson().toJson(response));
                            if(response.code() == 200){
                                ArrayList<Bidang> bidangList = response.body().getData();
                                Log.d(TAG, "BIDANG NAME : " + new Gson().toJson(bidangList));
                                for(int i = 0; i < bidangList.size(); i++){
                                    text_bidangpptk.setText(bidangList.get(i).getBiNama());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseBidang> call, Throwable t) {
                            Log.e(TAG, " " + call);
                            Log.e(TAG, " " + t.getMessage());
                        }
                    });

                    daya_serap_total = new BigInteger("0");
                    sisa_kontrak_total = new BigInteger("0");
                    sisa_anggaran_total = new BigInteger("0");
                    final BigInteger pagu_total = new BigInteger(pagu);
                    final BigInteger nilai_kontrak_total = new BigInteger(nilai_kontrak);
                    Call<DataResponseProgress> call_lastprogall = apiInterface.getProgressByPaketKeuangan(id_paket);
                    call_lastprogall.enqueue(new Callback<DataResponseProgress>() {
                        @Override
                        public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                            if(response.code() == 200){
                                ArrayList<Progress> progressList = response.body().getData();
                                for(int i = 0; i < progressList.size(); i++){
                                    BigInteger daya_serap = new BigInteger(progressList.get(i).getPr_daya_serap_kontrak());
//                                    result_daya_serap  = progressList.get(i).getPr_daya_serap_kontrak() == null ? "-" : "Rp. " + formatMoneyIDR.convertIDR(progressList.get(i).getPr_daya_serap_kontrak());
                                    daya_serap_total = daya_serap_total.add(daya_serap);
                                }
                                sisa_kontrak_total = nilai_kontrak_total.subtract(daya_serap_total);
                                sisa_anggaran_total = pagu_total.subtract(daya_serap_total);
                                text_dayaserap.setText("Rp. " + formatMoneyIDR.convertIDR(String.valueOf(daya_serap_total)));
                                text_sisakontrak.setText("Rp. " + formatMoneyIDR.convertIDR(String.valueOf(sisa_kontrak_total)));
                                text_sisaanggaran.setText("Rp. " + formatMoneyIDR.convertIDR(String.valueOf(sisa_anggaran_total)));
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseProgress> call, Throwable t) {

                        }
                    });


                    if(role.equals("Admin")){
                        card_info_pptk.setVisibility(View.VISIBLE);
                        Call<DataResponse> call_user = apiInterface.getUserById(pptk_id);
                        call_user.enqueue(new Callback<DataResponse>() {
                            @Override
                            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                                if(response.code() == 200){
                                    ArrayList<User> list = response.body().getData();
                                    for(int i = 0; i < list.size(); i++){
                                        text_emailpptk.setText(checkData(list.get(i).getEmail()));
                                        text_telpptk.setText(checkData(list.get(i).getTelephone()));
                                        text_namapptk.setText(checkData(list.get(i).getNama()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataResponse> call, Throwable t) {

                            }
                        });
                    }else{
                        card_info_pptk.setVisibility(View.GONE);
                    }


                    // get kegiatan
                    Call<DataResponseKegiatan> call_kegiataninfo = apiInterface.getKegiatan(ke_id);
                    call_kegiataninfo.enqueue(new Callback<DataResponseKegiatan>() {
                        @Override
                        public void onResponse(Call<DataResponseKegiatan> call, Response<DataResponseKegiatan> response) {
                            if(response.code() == 200){
                                ArrayList<KegiatanTree> keglist = response.body().getData();
                                Log.d(TAG, "BIDANG NAME : " + new Gson().toJson(keglist));
                                for(int i = 0; i < keglist.size(); i++){
                                    text_kegiatan.setText(keglist.get(i).getKeJudul());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseKegiatan> call, Throwable t) {

                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


        // set penyediajasa
        Call<DataResponsePJ> call_penyediajasa = apiInterface.getPenyediaJasaByPaket(id_paket);
        call_penyediajasa.enqueue(new Callback<DataResponsePJ>() {
            @Override
            public void onResponse(Call<DataResponsePJ> call, Response<DataResponsePJ> response) {
                if(response.code() == 200){
                    ArrayList<Kontraktor> data = response.body().getData();
                    for(int i = 0; i < data.size(); i++){
                        text_penyediajasa.setText(data.get(i).getKoNama());
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePJ> call, Throwable t) {

            }
        });

        // call last progress
        Call<DataResponseProgress> call_last_progfisik = apiInterface.getProgressFisikLast(id_paket);
        call_last_progfisik.enqueue(new Callback<DataResponseProgress>() {
            @Override
            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                if(response.code() == 200){
                    String real_percent = "";
                    String result = "";
                    String html_format = "";
                    String format_progress;
                    String result_format = "";
                    ArrayList<Progress> progressList = response.body().getData();
                    for(int i = 0; i < progressList.size(); i++){
                        if(progressList.get(i).getPr_tanggal() != null && progressList.get(i).getPr_real() != null && progressList.get(i).getPr_target() != null){
                            String kriteria = Kriteria.get_kriteria(progressList.get(i).getPr_tanggal(), progressList.get(i).getPr_real(), progressList.get(i).getPr_target());
                            String real = String.valueOf(progressList.get(i).getPr_real());
                            if(real.contains(".")){
                                format_progress = real.split("\\.", -1)[0];
                                result_format = format_progress;
                            }else{
                                result_format = progressList.get(i).getPr_real();
                            }
                            switch (kriteria.toLowerCase()){
                                case "kritis" :
                                    progres_pekerjaan.setProgress((Integer.valueOf(result_format)));
                                    tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " % " + "kritis"));
                                    break;
                                case "terlambat" :
                                    tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " % " + " terlambat"));
                                    progres_pekerjaan.setProgress((Integer.valueOf(result_format)));
                                    break;
                                case "wajar" :
                                    progres_pekerjaan.setProgress((Integer.valueOf(result_format)));
                                    tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " % " +" wajar" ));
                                    break;
                                case "baik" :
                                    progres_pekerjaan.setProgress((Integer.valueOf(result_format)));
                                    tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " % " + "baik" ));
                                    break;
                                case "selesai" :
                                    tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " % " + "selesai" ));
                                    progres_pekerjaan.setProgress((Integer.valueOf(result_format)));;
                                    break;
                                case "belum mulai" :
                                    tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " % " + "belum mulai" ));
                                    progres_pekerjaan.setProgress((Integer.valueOf(result_format)));
                                    break;
                            }
                            if (progressList.get(i).getPr_real().toString().equals("100")) {
                                sisa_waktukerja.setText("Status pekerjaan selesai");
                                sisa_waktukerja.setTextColor(Color.parseColor("#1B998B"));
                            }else{
                                sisa_waktukerja.setTextColor(Color.parseColor("#888888"));
                            }
                        }else{
                            tx_lasptprog.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " %" + ""));
                        }
                        lin_textreal.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_real() + " %"));
                        lin_texttarget.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_target() + " %"));
                        lin_textdeviasi.setText(checkData(progressList.get(i).getPr_real(), progressList.get(i).getPr_deviasi() + " %"));
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponseProgress> call, Throwable t) {

            }
        });


//        Call<DataResponseProgress> call_lastprogall = apiInterface.getProgressByPaketKeuangan(id_paket);
//        call_lastprogall.enqueue(new Callback<DataResponseProgress>() {
//            @Override
//            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
//                if(response.code() == 200){
//                    String real_percent = "";
//                    String result = "";
//                    ArrayList<Progress> progressList = response.body().getData();
//                    for(int i = 0; i < progressList.size(); i++){
//                        real_percent = progressList.get(i).getPr_real().equals("") ||  progressList.get(i).getPr_real() == null ? "0" : progressList.get(i).getPr_real().toString();
//                        result = real_percent + " %";
//
//                        String result_daya_serap = "";
//                        String result_sisa_kontrak = "";
//                        String result_sisa_anggaran = "";
//
//                        result_daya_serap  = progressList.get(i).getPr_daya_serap_kontrak() == null ? "-" : "Rp. " + formatMoneyIDR.convertIDR(progressList.get(i).getPr_daya_serap_kontrak());
//                        result_sisa_kontrak = progressList.get(i).getPr_sisa_kontrak() == null ? "-" :  "Rp. " + formatMoneyIDR.convertIDR(progressList.get(i).getPr_sisa_kontrak());
//                        result_sisa_anggaran = progressList.get(i).getPr_sisa_anggaran() == null ? "-" :  "Rp. " +  formatMoneyIDR.convertIDR(progressList.get(i).getPr_sisa_anggaran());
//
//                        text_dayaserap.setText(result_daya_serap);
//                        text_sisakontrak.setText(result_sisa_kontrak);
//                        text_sisaanggaran.setText(result_sisa_anggaran);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DataResponseProgress> call, Throwable t) {
//
//            }
//        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String id_paket = intent.getStringExtra("pa_id");
                Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
                call_paket.enqueue(new Callback<DataResponsePaket>() {
                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                        Log.w(TAG, "Paket data" + new Gson().toJson(response.body().getData()));
                        ArrayList<Paket> paketlist = response.body().getData();
                        Intent mapIntent = new Intent(ActivityDetailPaket.this, ActivityMapDetail.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST", paketlist);
                        mapIntent.putExtra("BUNDLE", args);
                        startActivity(mapIntent);
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });

            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(Message.obtain(mHandler, 1));
            }
        }).start();

        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        progressDialog.dismiss();
                        main_layout_detail_paket.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    public static String checkData(String data){
        if(data == null || data.equals("null") || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    public static String checkData(String data_main, String data){
        if(data_main == null || data_main.equals("null") || data_main == "" || data_main.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onRestart();
        onBackPressedRefresh();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    public void onBackPressedRefresh(){
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String flagtosend = "";
        if(role.equals("Admin")){
            flagtosend = "1";
        }else if(role.equals("Bidang")){
            flagtosend = "1";
        }else if(role.equals("Pptk")){
            flagtosend = "1";
        }
        intent.putExtra("flag_list", flagtosend);
        startActivity(intent);
        sessionManager.checkLogin();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intents = getIntent();
        String id_paket = intents.getStringExtra("pa_id");
        String nama_paket = intents.getStringExtra("pa_nama");
        String pa_pagu = intents.getStringExtra("pa_pagu");
        String ke_id = intents.getStringExtra("ke_id");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String dinas_id =  user.get(SessionManager.KEY_DINASID);
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                onBackPressedRefresh();
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                return true;
            case R.id.nav_upload :
                String path_todropbox = "";
//                mPath = "/files/gov/16731/pa-483/photos";
                path_todropbox = "/files/gov/"+dinas_id+"/pa-"+id_paket+"/photos";
//                startActivity(FilesActivity.getIntent(ActivityDetailPaket.this, path_todropbox));
                Intent intent = new Intent(ActivityDetailPaket.this, UserActivity.class);
                intent.putExtra("path_dropbox", path_todropbox);
                intent.putExtra("pa_judul", nama_paket);
                intent.putExtra("upload_type", "1");
                startActivity(FilesActivityDirect.getIntent(ActivityDetailPaket.this, path_todropbox));
//                startActivity(intent);
                return true;
//            case R.id.nav_kurvasrencana :
//                Intent intent6 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
//                intent6.putExtra("position", 2);
//                intent6.putExtra("pa_id", id_paket);
//                intent6.putExtra("pa_nama", nama_paket);
//                intent6.putExtra("ke_id", ke_id);
//                intent6.putExtra("pa_pagu", pa_pagu);
//                startActivity(intent6);
//                return true;
            case R.id.nav_editkontrak :
                Intent intent2 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);

                intent2.putExtra("pa_pagu", pa_pagu);
                intent2.putExtra("position", 0);
                intent2.putExtra("pa_id", id_paket);
                intent2.putExtra("pa_nama", nama_paket);
                intent2.putExtra("ke_id", ke_id);
                intent2.putExtra("flag_update", "5");
                startActivity(intent2);
                return true;
            case R.id.nav_uploadoc :
                String path_todropbox2 = "";
//                mPath = "/files/gov/16731/pa-483/photos";
                path_todropbox = "/files/gov/"+dinas_id+"/pa-"+id_paket+"/documents";
//                startActivity(FilesActivity.getIntent(ActivityDetailPaket.this, path_todropbox));
                Intent intent5 = new Intent(ActivityDetailPaket.this, UserActivity.class);
                intent5.putExtra("pa_pagu", pa_pagu);
                intent5.putExtra("path_dropbox", path_todropbox);
                intent5.putExtra("pa_judul", nama_paket);
                intent5.putExtra("upload_type", "2");
                intent5.putExtra("flag_update", "");
                startActivity(FilesActivityDirect.getIntent(ActivityDetailPaket.this, path_todropbox));
//                startActivity(intent5);
                return true;
            case R.id.nav_editlokasi :
                Intent intent3 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent3.putExtra("pa_pagu", pa_pagu);
                intent3.putExtra("position", 1);
                intent3.putExtra("pa_id", id_paket);
                intent3.putExtra("pa_nama", nama_paket);
                intent3.putExtra("ke_id", ke_id);
                intent3.putExtra("flag_update", "");
                startActivity(intent3);
                return true;
            case R.id.nav_progress :
                Intent intent4 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent4.putExtra("pa_pagu", pa_pagu);
                intent4.putExtra("position", 2);
                intent4.putExtra("pa_id", id_paket);
                intent4.putExtra("pa_nama", nama_paket);
                intent4.putExtra("ke_id", ke_id);
                intent4.putExtra("flag_update", "");
                startActivity(intent4);
                return true;
            case R.id.nav_keu:
                Intent intent8 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent8.putExtra("pa_pagu", pa_pagu);
                intent8.putExtra("position", 3);
                intent8.putExtra("pa_id", id_paket);
                intent8.putExtra("pa_nama", nama_paket);
                intent8.putExtra("ke_id", ke_id);
                intent8.putExtra("flag_update", "");
                startActivity(intent8);
                return true;
            case R.id.nav_catatan :
                Intent intent7 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent7.putExtra("pa_pagu", pa_pagu);
                intent7.putExtra("position", 4);
                intent7.putExtra("pa_id", id_paket);
                intent7.putExtra("pa_nama", nama_paket);
                intent7.putExtra("ke_id", ke_id);
                intent7.putExtra("flag_update", "");
                startActivity(intent7);
                return true;
            case R.id.nav_penyediajasa :
                Intent intent9 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent9.putExtra("position", 5);
                intent9.putExtra("pa_pagu", pa_pagu);
                intent9.putExtra("pa_id", id_paket);
                intent9.putExtra("pa_nama", nama_paket);
                intent9.putExtra("ke_id", ke_id);
                intent9.putExtra("flag_update", "");
                startActivity(intent9);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_detail_paket, menu);
        Intent intents = getIntent();
        String request = intents.getStringExtra("request");
        Boolean check = request.equals("admin");
        MenuItem prog = menu.findItem(R.id.nav_progress);
        MenuItem keu = menu.findItem(R.id.nav_keu);
        MenuItem up = menu.findItem(R.id.nav_upload);
        MenuItem up2 = menu.findItem(R.id.nav_uploadoc);
        MenuItem kont = menu.findItem(R.id.nav_editkontrak);
        MenuItem lok = menu.findItem(R.id.nav_editlokasi);
        MenuItem kus = menu.findItem(R.id.nav_kurvasrencana);
        MenuItem cat = menu.findItem(R.id.nav_catatan);
        MenuItem pj = menu.findItem(R.id.nav_penyediajasa);
        if(check){
            pj.setVisible(false);
            cat.setVisible(false);
            keu.setVisible(false);
            kus.setVisible(false);
            prog.setVisible(false);
            kont.setVisible(false);
            lok.setVisible(false);
            up2.setVisible(true);
            up.setVisible(true);
        }else{
            pj.setVisible(true);
            cat.setVisible(true);
            keu.setVisible(true);
            kus.setVisible(false);
            prog.setVisible(true);
            kont.setVisible(true);
            lok.setVisible(true);
            up2.setVisible(true);
            up.setVisible(true);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
