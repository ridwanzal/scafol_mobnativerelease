package com.release.activity;

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
import com.release.model.Anggaran;
import com.release.model.DataResponseAnggaran;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.util.ArrayList;

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

        String anggaran_id = getIntent().getStringExtra("an_id");
        Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaran(anggaran_id);
        call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
            @Override
            public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                String response_code = new Gson().toJson(response.code()).toString();
                if(response_code.equals("200")){
                    ArrayList<Anggaran> data = response.body().getData();
                    for(int i=0; i < data.size(); i++){
                        text_nama_anggaran.setText(data.get(i).getAnNama());
                        if(data.get(i).getAnNilaikontrak().equals("")){
                            text_kontrak_anggaran.setText("-");
                        }else {
                            text_kontrak_anggaran.setText(formatMoneyIDR.convertIDR(data.get(i).getAnNilaikontrak()));
                        }
                        text_nomorkontrak_anggaran.setText(data.get(i).getAnNomorkontrak());
                        text_akhirkontrak_anggaran.setText(data.get(i).getAnAkhirkontrak());
                        text_anggaran_pagu.setText("Rp. " + formatMoneyIDR.convertIDR(data.get(i).getAnpPagu()));
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
        switch (item.getItemId()){
            case android.R.id.home :
                finish();

                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_detail_anggaran, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
