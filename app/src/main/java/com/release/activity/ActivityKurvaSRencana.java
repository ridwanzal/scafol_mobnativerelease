package com.release.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.adapter.KurvaSRencanaAdapter;
import com.release.adapter.ProgressAdapterSerapan;
import com.release.model.DataResponseRencana;
import com.release.model.DataResponseSerapan;
import com.release.model.Rencana;
import com.release.model.Serapan;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityKurvaSRencana extends AppCompatActivity {
    private static String TAG = "ActivityKurvaSRencana";
    public static  ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private KurvaSRencanaAdapter kurvaSRencanaAdapter;
    private String id_paket;
    private String nama_paket;
    private TextView textnofound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        id_paket = intent.getStringExtra("pa_id");
        nama_paket = intent.getStringExtra("pa_nama");
        setContentView(R.layout.recycle_rencana);
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + nama_paket + "</small>"));
        textnofound = findViewById(R.id.text_notfound);
        progressBar = findViewById(R.id.progress_listprogress);
        progressBar.setVisibility(View.VISIBLE);
        textnofound.setVisibility(View.GONE);
        Call<DataResponseRencana> callkurva = apiInterface.getKurvaS(id_paket);
        callkurva.enqueue(new Callback<DataResponseRencana>() {
            @Override
            public void onResponse(Call<DataResponseRencana> call, Response<DataResponseRencana> response) {
                if(response.code() == 200){
                    generateRencanaList(response.body().getData());
                    textnofound.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    textnofound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DataResponseRencana> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textnofound.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void generateRencanaList(ArrayList<Rencana> serapanArrayList){
        recyclerView = findViewById(R.id.recycle_listrencana);
        kurvaSRencanaAdapter = new KurvaSRencanaAdapter(getApplicationContext(), serapanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityKurvaSRencana.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kurvaSRencanaAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
