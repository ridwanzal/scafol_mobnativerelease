package com.example.scafolmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scafolmobile.R;
import com.example.scafolmobile.adapter.PaketAdapter;
import com.example.scafolmobile.adapter.ProgressAdapter;
import com.example.scafolmobile.interfacemodule.ItemClickListener;
import com.example.scafolmobile.model.DataResponsePA;
import com.example.scafolmobile.model.DataResponseProgress;
import com.example.scafolmobile.model.Progress;
import com.example.scafolmobile.restapi.ApiClient;
import com.example.scafolmobile.restapi.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProgressFisik extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "ActivityProgressFisik";
    private TextView text_default;
    private RecyclerView recyclerView;
    private ProgressAdapter progressAdapter;
    private ProgressBar progressBar;
    private TextView textnofound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String nama_paket = intent.getStringExtra("pa_nama");
        setTitle(nama_paket);
        setContentView(R.layout.recycle_progress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String id_paket = intent.getStringExtra("pa_id");
        progressBar = findViewById(R.id.progress_listprogress);
        textnofound = findViewById(R.id.text_notfound);
        progressBar.setVisibility(View.VISIBLE);
        textnofound.setVisibility(View.GONE);
//        Toast.makeText(this, "paket id : "  + id_paket, Toast.LENGTH_SHORT).show();
        Call<DataResponseProgress> callprogress = apiInterface.getProgressByPaket(id_paket);
        callprogress.enqueue(new Callback<DataResponseProgress>() {
            @Override
            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                try {
                    Log.d(TAG, "-===========-> " + new Gson().toJson(response));
                        if(response.body().getData()  != null ){
                            generateProgressList(response.body().getData());
                            textnofound.setVisibility(View.GONE);
                            progressAdapter = new ProgressAdapter(getApplicationContext(), response.body().getData());
                            progressAdapter.notifyDataSetChanged();
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
        recyclerView = findViewById(R.id.recycle_listprogress2);
        progressAdapter = new ProgressAdapter(getApplicationContext(), progressArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityProgressFisik.this);
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
