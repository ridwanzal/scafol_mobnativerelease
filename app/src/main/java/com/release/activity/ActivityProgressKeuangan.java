package com.release.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.release.R;
import com.release.adapter.ProgressAdapter;
import com.release.adapter.ProgressAdapterKeuangan;
import com.release.model.DataResponseProgress;
import com.release.model.Progress;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProgressKeuangan extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "ActivityProgressKeuangan";
    private TextView text_default;
    private RecyclerView recyclerView;
    private ProgressAdapterKeuangan progressAdapter;
    private ProgressBar progressBar;
    private TextView textnofound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_progress_keuangan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String nama_paket = intent.getStringExtra("pa_nama");
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + nama_paket + "</small>"));
        final String id_paket = intent.getStringExtra("pa_id");
        progressBar = findViewById(R.id.progress_listprogresskeuangan);
        textnofound = findViewById(R.id.text_notfoundkeu);
        progressBar.setVisibility(View.VISIBLE);
        textnofound.setVisibility(View.GONE);
        // Toast.makeText(this, "paket id : "  + id_paket, Toast.LENGTH_SHORT).show();
        Call<DataResponseProgress> callprogress = apiInterface.getProgressByPaketKeuangan(id_paket);
        callprogress.enqueue(new Callback<DataResponseProgress>() {
            @Override
            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                try {
                    Log.d(TAG, "-===========-> " + new Gson().toJson(response));
                    if(response.code() == 200 ){
                        generateProgressList(response.body().getData());
                        textnofound.setVisibility(View.GONE);
//                            progressAdapter = new ProgressAdapter(getApplicationContext(), response.body().getData());
//                            progressAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        textnofound.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    textnofound.setVisibility(View.GONE);
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailure(Call<DataResponseProgress> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textnofound.setVisibility(View.GONE);
            }
        });
    }

    private void generateProgressList(ArrayList<Progress> progressArrayList){
        recyclerView = findViewById(R.id.recycle_listprogress3);
        progressAdapter = new ProgressAdapterKeuangan(getApplicationContext(), progressArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityProgressKeuangan.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(progressAdapter);
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
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
