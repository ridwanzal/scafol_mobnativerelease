package com.release.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.release.interfacemodule.ItemClickListener;
import com.release.model.DataResponseCatatan;
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
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
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

    private void generateRencanaList(final ArrayList<Rencana> rencanaArrayList){
        recyclerView = findViewById(R.id.recycle_listrencana);
        kurvaSRencanaAdapter = new KurvaSRencanaAdapter(getApplicationContext(), rencanaArrayList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, final int position, final String id_list) {
                new AlertDialog.Builder(ActivityKurvaSRencana.this)
                        .setTitle("Hapus Rencana")
                        .setMessage("Anda yakin ingin menghapus item ini ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                Call<DataResponseRencana> removeCatatan = apiInterface.removeKurvaS(id_list);
                                removeCatatan.enqueue(new Callback<DataResponseRencana>() {
                                    @Override
                                    public void onResponse(Call<DataResponseRencana> call, Response<DataResponseRencana> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<DataResponseRencana> call, Throwable t) {

                                    }
                                });
                                rencanaArrayList.remove(position);
                                kurvaSRencanaAdapter.notifyItemRemoved(position);
                                kurvaSRencanaAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                return true;
            }

            @Override
            public void onItemDoubleCLick(View view, int position) {

            }

            @Override
            public void onDoubleClick(View view, int position) {

            }

            @Override
            public void onItemClick(View view, int position, String param1) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityKurvaSRencana.this);
        kurvaSRencanaAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kurvaSRencanaAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
