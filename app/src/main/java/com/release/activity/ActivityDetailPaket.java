package com.release.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.release.R;
import com.release.dropbox.FilesActivity;
import com.release.dropbox.UserActivity;
import com.release.model.Bidang;
import com.release.model.DataResponseBidang;
import com.release.model.DataResponseKegiatan;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.Kegiatan;
import com.release.model.Paket;
import com.release.model.Progress;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.formatMoneyIDR;
import com.release.sharedpreferences.SessionManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

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

    private TextView text_satuan;
    private TextView text_volume;
    private TextView text_tanggal_mulai;
    private TextView text_tanggal_akhir;

    private TextView text_namapptk;
    private TextView text_emailpptk;
    private TextView text_telpptk;
    private TextView text_bidangpptk;
    private TextView text_kegiatan;

    private TextView text_nilaikontrak;
    private TextView text_progress;

    private TextView maps_caption;
    private TextView tx_lasptprog;

    private TextView text_dayaserap;
    private TextView text_sisakontrak;
    private TextView text_sisaanggaran;

    private TextView lin_texttarget;
    private TextView lin_textreal;
    private TextView lin_textdeviasi;
    private TextView text_nokontrak;

    private TextView sisa_waktukerja;
    private Context mContext;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    int PICK_IMAGE_REQUEST = 1;

    private ProgressBar progressBar;
    private CardView cardView;


    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paketdetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String role = user.get(SessionManager.KEY_ROLE);
        String dinas_id =  user.get(SessionManager.KEY_DINASID);
        String user_id =  user.get(SessionManager.KEY_USERID);

        String bi_id = user.get(SessionManager.KEY_BIDANG);

        final String user_fullname = user.get(SessionManager.KEY_NAME);
        String user_name = user.get(SessionManager.KEY_USERNAME);

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


        Intent intent = getIntent();
        String id_paket = intent.getStringExtra("pa_id");
        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                Log.w(TAG, "Paket data" + new Gson().toJson(response.body().getData()));
                ArrayList<Paket> paketlist = response.body().getData();
                for(int i = 0; i < paketlist.size(); i++){
                    String name = paketlist.get(i).getPaJudul();
                    String jenis = paketlist.get(i).getPaJenis();
                    String tahun = paketlist.get(i).getPaTahun();
                    String pagu = paketlist.get(i).getPaPagu();
                    String satuan = paketlist.get(i).getPaSatuan();
                    String volume = paketlist.get(i).getPaVolume();
                    String ke_id = paketlist.get(i).getKeId();
                    String status = paketlist.get(i).getStatus();
                    String tanggal_awal = paketlist.get(i).getPaAwalKontrak();
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
                    text_namapptk.setText(checkData(user_fullname));
                    text_satuan.setText(checkData(satuan));
                    text_volume.setText(checkData(volume));
                    text_nokontrak.setText(nokontrak);

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


                    // get kegitan
                    Call<DataResponseKegiatan> call_kegiataninfo = apiInterface.getKegiatan(ke_id);
                    call_kegiataninfo.enqueue(new Callback<DataResponseKegiatan>() {
                        @Override
                        public void onResponse(Call<DataResponseKegiatan> call, Response<DataResponseKegiatan> response) {
                            if(response.code() == 200){
                                ArrayList<Kegiatan> keglist = response.body().getData();
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




        // call last progress
        Call<DataResponseProgress> call_lastprogall = apiInterface.getlastProgressallPPTK(id_paket);
        call_lastprogall.enqueue(new Callback<DataResponseProgress>() {
            @Override
            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                if(response.code() == 200){
                    String real_percent = "";
                    String result = "";
                    ArrayList<Progress> progressList = response.body().getData();
                    for(int i = 0; i < progressList.size(); i++){
                        real_percent = progressList.get(i).getPr_real().equals("") ||  progressList.get(i).getPr_real() == null ? "0" : progressList.get(i).getPr_real().toString();
                        result = real_percent + " %";
                        tx_lasptprog.setText(result.toString());

                        String result_daya_serap = "";
                        String result_sisa_kontrak = "";
                        String result_sisa_anggaran = "";

                        String result_lin_textreal = "";
                        String result_lin_texttarget = "";
                        String result_lin_textdeviasi = "";

                        result_daya_serap  = progressList.get(i).getPr_daya_serap_kontrak() == null ? "-" : "Rp. " + formatMoneyIDR.convertIDR(progressList.get(i).getPr_daya_serap_kontrak());
                        result_sisa_kontrak = progressList.get(i).getPr_sisa_kontrak() == null ? "-" :  "Rp. " + formatMoneyIDR.convertIDR(progressList.get(i).getPr_sisa_kontrak());
                        result_sisa_anggaran = progressList.get(i).getPr_sisa_anggaran() == null ? "-" :  "Rp. " +  formatMoneyIDR.convertIDR(progressList.get(i).getPr_sisa_anggaran());


                        text_dayaserap.setText(result_daya_serap);
                        text_sisakontrak.setText(result_sisa_kontrak);
                        text_sisaanggaran.setText(result_sisa_anggaran);

                        lin_textreal.setText(result);
                        lin_texttarget.setText(progressList.get(i).getPr_target() + " %");
                        lin_textdeviasi.setText(progressList.get(i).getPr_deviasi() + " %");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponseProgress> call, Throwable t) {

            }
        });


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
//                        for(int i = 0; i < paketlist.size(); i++){
//                            String name = paketlist.get(i).getPaJudul();
//                            String jenis = paketlist.get(i).getPaJenis();
//                            String tahun = paketlist.get(i).getPaTahun();
//                            String pagu = paketlist.get(i).getPaPagu();
//                            String satuan = paketlist.get(i).getPaSatuan();
//                            String volume = paketlist.get(i).getPaVolume();
//                            String status = paketlist.get(i).getStatus();
//                            String tanggal_awal = paketlist.get(i).getDateCreated();
//                            String tanggal_akhir = paketlist.get(i).getDateUpdated();
//                            String nilai_kontrak = paketlist.get(i).getPaNilaiKontrak();
//                        }
//                        Toast.makeText(ActivityDetailPaket.this, "Show map", Toast.LENGTH_SHORT).show();
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
    }

    public static String idrFormat(String data){
        if(data == null || data == "" || data.equals("")){
            return "Rp. -";
        }else{
            return "Rp. " + data;
        }
    }

    public static String checkData(String data){
        if(data == null || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    public static String checkStatus(String status){
        if(status.equals('0')){
            return "-";
        }else{
            return status;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityDetailPaket.this.finish();
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
        String ke_id = intents.getStringExtra("ke_id");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String dinas_id =  user.get(SessionManager.KEY_DINASID);
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
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
                startActivity(intent);
                return true;
//            case R.id.nav_kurvasrencana :
//                Intent intent6 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
//                intent6.putExtra("position", 0);
//                intent6.putExtra("pa_id", id_paket);
//                intent6.putExtra("pa_nama", nama_paket);
//                intent6.putExtra("ke_id", ke_id);
//                startActivity(intent6);
//                return true;
            case R.id.nav_editkontrak :
                Intent intent2 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent2.putExtra("position", 0);
                intent2.putExtra("pa_id", id_paket);
                intent2.putExtra("pa_nama", nama_paket);
                intent2.putExtra("ke_id", ke_id);
                startActivity(intent2);
                return true;
            case R.id.nav_uploadoc :
                String path_todropbox2 = "";
//                mPath = "/files/gov/16731/pa-483/photos";
                path_todropbox = "/files/gov/"+dinas_id+"/pa-"+id_paket+"/documents";
//                startActivity(FilesActivity.getIntent(ActivityDetailPaket.this, path_todropbox));
                Intent intent5 = new Intent(ActivityDetailPaket.this, UserActivity.class);
                intent5.putExtra("path_dropbox", path_todropbox);
                intent5.putExtra("pa_judul", nama_paket);
                intent5.putExtra("upload_type", "2");
                startActivity(intent5);
                return true;
            case R.id.nav_editlokasi :
                Intent intent3 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent3.putExtra("position", 1);
                intent3.putExtra("pa_id", id_paket);
                intent3.putExtra("pa_nama", nama_paket);
                intent3.putExtra("ke_id", ke_id);
                startActivity(intent3);
                return true;
            case R.id.nav_progress :
                Intent intent4 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent4.putExtra("position", 2);
                intent4.putExtra("pa_id", id_paket);
                intent4.putExtra("pa_nama", nama_paket);
                intent4.putExtra("ke_id", ke_id);
                startActivity(intent4);
                return true;
//            case R.id.nav_penyediajasa :
//                Intent intent5 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
//                intent5.putExtra("position", 3);
//                intent5.putExtra("pa_id", id_paket);
//                intent5.putExtra("pa_nama", nama_paket);
//                intent5.putExtra("ke_id", ke_id);
//                startActivity(intent5);
//                return true;
//            case R.id.nav_upload :
//                Intent intent6 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
//                intent6.putExtra("position", 5);
//                intent6.putExtra("pa_id", id_paket);
//                intent6.putExtra("pa_nama", nama_paket);
//                intent6.putExtra("ke_id", ke_id);
//                startActivity(intent6);
//                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_detail_paket, menu);
        return true;
    }

}
