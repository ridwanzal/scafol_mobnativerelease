package com.release.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.adapter.DinasAdapter;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.DataResponseCatatan;
import com.release.model.DataResponseDaerah;
import com.release.model.DataResponseDinas;
import com.release.model.Dinas;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDinas extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private RecyclerView recyclerView;
    private DinasAdapter dinasAdapter;
    private TextView textnofound;
    private ProgressBar progressBar;
    String daerah_id;
    private static String TAG = "ActivityDinas";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        setContentView(R.layout.recycle_dinas);
        Intent intent = getIntent();
        daerah_id = intent.getStringExtra("daerah_id");
//        Toast.makeText(this, "=========== daerah_id " + daerah_id, Toast.LENGTH_SHORT).show();
        setTitle("Daftar Dinas");
        textnofound = findViewById(R.id.text_notfound_dinas);
        textnofound.setVisibility(View.GONE);
        Call<DataResponseDaerah> call_daerah = apiInterface.getNamaDaerah(daerah_id);
        call_daerah.enqueue(new Callback<DataResponseDaerah>() {
            @Override
            public void onResponse(Call<DataResponseDaerah> call, Response<DataResponseDaerah> response) {
                if(response.code() == 200);
                for(int i = 0; i < response.body().getData().size(); i++){
//                    getSupportActionBar().setSubtitle(response.body().getData().get(i).getDaerahNama());
                    getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + response.body().getData().get(i).getDaerahNama() + "</small>"));
                }
            }

            @Override
            public void onFailure(Call<DataResponseDaerah> call, Throwable t) {

            }
        });


        Call<DataResponseDinas> call_dinas = apiInterface.getDinasSuper(daerah_id);
        call_dinas.enqueue(new Callback<DataResponseDinas>() {
            @Override
            public void onResponse(Call<DataResponseDinas> call, Response<DataResponseDinas> response) {
                if(response.code() == 200){
//                    Toast.makeText(ActivityDinas.this, "Response " + response.code(), Toast.LENGTH_SHORT).show();
                    generateDinas(response.body().getData());
                    Log.d(TAG, response.body().getData().toString());
                }
            }

            @Override
            public void onFailure(Call<DataResponseDinas> call, Throwable t) {

            }
        });
    }

    private void generateDinas(final ArrayList<Dinas> dinasArrayList){
        recyclerView = findViewById(R.id.recycle_dinas);
        dinasAdapter = new DinasAdapter(getApplicationContext(), dinasArrayList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemClick(View view, int position, String param1) {
            }

            @Override
            public boolean onItemLongClick(View view, int position, String id_list) {
                return false;
            }

            @Override
            public void onItemDoubleCLick(View view, int position) {

            }

            @Override
            public void onDoubleClick(View view, int position) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityDinas.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dinasAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
