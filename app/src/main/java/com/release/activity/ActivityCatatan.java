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
import com.release.adapter.CatatanAdapter;
import com.release.adapter.ProgressAdapter;
import com.release.model.Catatan;
import com.release.model.DataResponse;
import com.release.model.DataResponseCatatan;
import com.release.model.DataResponseProgress;
import com.release.model.Progress;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCatatan extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private RecyclerView recyclerView;
    private CatatanAdapter catatanAdapter;
    private ProgressBar progressBar;
    private TextView textnofound;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_catatan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final String nama_paket = intent.getStringExtra("pa_nama");
        final String id_paket = intent.getStringExtra("pa_id");
        setTitle("Catatan");
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + nama_paket + "</small>"));
        progressBar = findViewById(R.id.progress_listprogress_catatan);
        textnofound = findViewById(R.id.text_notfound_catatan);
        progressBar.setVisibility(View.VISIBLE);
        textnofound.setVisibility(View.GONE);

        Call<DataResponseCatatan> callcatatan = apiInterface.getCatatan(id_paket);
        callcatatan.enqueue(new Callback<DataResponseCatatan>() {
            @Override
            public void onResponse(Call<DataResponseCatatan> call, Response<DataResponseCatatan> response) {
                if(response.code() == 200){
                    generateProgressList(response.body().getData());
                    textnofound.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    textnofound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DataResponseCatatan> call, Throwable t) {

            }
        });
    }

    private void generateProgressList(ArrayList<Catatan> catatanArrayList){
        recyclerView = findViewById(R.id.recycle_catatan);
        catatanAdapter = new CatatanAdapter(getApplicationContext(), catatanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityCatatan.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(catatanAdapter);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
