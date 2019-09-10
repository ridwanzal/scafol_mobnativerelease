package com.release.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.adapter.ProgressAdapterKeuangan;
import com.release.adapter.ProgressAdapterSerapan;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponseSerapan;
import com.release.model.Progress;
import com.release.model.Serapan;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProgressSerapan extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    String nama_paket;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ProgressAdapterSerapan progressAdapterSerapan;
    private String id_anggaran;
    private TextView textnofound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.recycle_progress_serapan);
        Intent intent = getIntent();
        nama_paket = intent.getStringExtra("pa_nama");
        id_anggaran = intent.getStringExtra("an_id");
        textnofound = findViewById(R.id.text_notfoundkeu);
        progressBar = findViewById(R.id.progress_listprogressserapan);
        progressBar.setVisibility(View.VISIBLE);
        textnofound.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Progres Serapan Paket Non-Fisik");
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + nama_paket + "</small>"));
        Call<DataResponseSerapan> callserapan = apiInterface.getSerapan(id_anggaran);
        callserapan.enqueue(new Callback<DataResponseSerapan>() {
            @Override
            public void onResponse(Call<DataResponseSerapan> call, Response<DataResponseSerapan> response) {
                if(response.code() == 200){
                    genereteSerapanList(response.body().getData());
                    textnofound.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    textnofound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DataResponseSerapan> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void genereteSerapanList(ArrayList<Serapan> serapanArrayList){
        recyclerView = findViewById(R.id.recycle_listprogress4);
        progressAdapterSerapan = new ProgressAdapterSerapan(getApplicationContext(), serapanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityProgressSerapan.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(progressAdapterSerapan);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
