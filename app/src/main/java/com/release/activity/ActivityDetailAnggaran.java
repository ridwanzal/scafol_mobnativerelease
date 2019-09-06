package com.release.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.release.R;
import com.release.dropbox.FilesActivityDirect;
import com.release.dropbox.UserActivity;
import com.release.model.Anggaran;
import com.release.model.Bidang;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponseBidang;
import com.release.model.DataResponseKegiatan;
import com.release.model.Kegiatan;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.formatMoneyIDR;
import com.release.sharedpreferences.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailAnggaran extends AppCompatActivity {
    private static String TAG = "ActivityDetailPaket";
    private TextView text_nama_anggaran;
    private TextView text_kontrak_anggaran;
    private TextView text_nomorkontrak_anggaran;
    private TextView text_akhirkontrak_anggaran;
    private TextView text_anggaran_pagu;
    private TextView tx_tanggalupdate;
    private TextView text_kegjudul_ang;
    private TextView textbidangs_ang;
    String path_todropbox = "";
    String path_todropbox2 = "";
    SessionManager sessionManager;
    // Service
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggarandetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + "Informasi Detail Anggaran/ Non Fisik" + "</small>"));
        text_nama_anggaran = findViewById(R.id.text_nama_anggaran);
        text_kontrak_anggaran = findViewById(R.id.text_kontrak_anggaran);
        text_nomorkontrak_anggaran = findViewById(R.id.text_kontrak_anggaran);
        text_akhirkontrak_anggaran = findViewById(R.id.text_akhirkontrak_anggaran);
        text_anggaran_pagu = findViewById(R.id.text_anggaran_pagu);
        text_kegjudul_ang = findViewById(R.id.text_kegjudul_ang);
        textbidangs_ang = findViewById(R.id.textbidangs_ang);

        String anggaran_id = getIntent().getStringExtra("an_id");
        Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaran(anggaran_id);
        call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
            @Override
            public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                String response_code = new Gson().toJson(response.code()).toString();
                if(response_code.equals("200")){
                    ArrayList<Anggaran> data = response.body().getData();
                    for(int i=0; i < data.size(); i++){
                        String ke_id = data.get(i).getKeId();
                        text_nama_anggaran.setText(data.get(i).getAnNama());
//                        if(data.get(i).getAnNilaikontrak().equals("") || data.get(i).getAnNilaikontrak() == null){
//                            text_kontrak_anggaran.setText("-");
//                        }else {
//                            text_kontrak_anggaran.setText(formatMoneyIDR.convertIDR(data.get(i).getAnNilaikontrak()));
//                        }
                        text_nomorkontrak_anggaran.setText(data.get(i).getAnNomorkontrak());
                        text_akhirkontrak_anggaran.setText(data.get(i).getAnAkhirkontrak());
                        text_anggaran_pagu.setText("Rp. " + formatMoneyIDR.convertIDR(data.get(i).getAnpPagu()));


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
                                        textbidangs_ang.setText(bidangList.get(i).getBiNama());
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
                                        text_kegjudul_ang.setText(keglist.get(i).getKeJudul());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataResponseKegiatan> call, Throwable t) {

                            }
                        });
                    }
                }else{
                }
            }

            @Override
            public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intents = getIntent();
        String id_anggaran = intents.getStringExtra("an_id");
        String nama_paket = intents.getStringExtra("pa_nama");
        String pa_pagu = intents.getStringExtra("pa_pagu");
        String ke_id = intents.getStringExtra("ke_id");
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String dinas_id =  user.get(SessionManager.KEY_DINASID);
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            case R.id.nav_uploadphoto_anggaran :
                path_todropbox = "/files/gov/"+dinas_id+"/an-"+id_anggaran+"/photos";
                Intent intent = new Intent(ActivityDetailAnggaran.this, UserActivity.class);
                intent.putExtra("path_dropbox", path_todropbox);
                intent.putExtra("pa_judul", nama_paket);
                intent.putExtra("upload_type", "1");
                startActivity(FilesActivityDirect.getIntent(ActivityDetailAnggaran.this, path_todropbox));
                return true;
            case R.id.nav_uploaddoc_anggaran :
                path_todropbox = "/files/gov/"+dinas_id+"/an-"+id_anggaran+"/documents";
                Intent intent2 = new Intent(ActivityDetailAnggaran.this, UserActivity.class);
                intent2.putExtra("path_dropbox", path_todropbox);
                intent2.putExtra("pa_judul", nama_paket);
                intent2.putExtra("upload_type", "2");
                startActivity(FilesActivityDirect.getIntent(ActivityDetailAnggaran.this, path_todropbox));
                return true;
            case R.id.nav_editkontrak_anggarn :
                Intent intent3 = new Intent(ActivityDetailAnggaran.this, ActivityUpdateDataAnggaran.class);
                intent3.putExtra("pa_pagu", pa_pagu);
                intent3.putExtra("an_id", id_anggaran);
                intent3.putExtra("position", 0);
                intent3.putExtra("pa_nama", nama_paket);
                intent3.putExtra("ke_id", ke_id);
                startActivity(intent3);
                return true;
            case R.id.nav_progress_anggaran :
                Intent intent4 = new Intent(ActivityDetailAnggaran.this, ActivityUpdateDataAnggaran.class);
                intent4.putExtra("pa_pagu", pa_pagu);
                intent4.putExtra("an_id", id_anggaran);
                intent4.putExtra("position", 0);
                intent4.putExtra("pa_nama", nama_paket);
                intent4.putExtra("ke_id", ke_id);
                startActivity(intent4);
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_detail_anggaran, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
