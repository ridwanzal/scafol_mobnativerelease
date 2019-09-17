package com.release.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.adapter.CatatanAdapter;
import com.release.adapter.ProgressAdapter;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Catatan;
import com.release.model.DataResponse;
import com.release.model.DataResponseCatatan;
import com.release.model.DataResponseProgress;
import com.release.model.Progress;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCatatan extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private RecyclerView recyclerView;
    private CatatanAdapter catatanAdapter;
    private ProgressBar progressBar;
    private TextView textnofound;
    private Toolbar toolbar;
    private androidx.appcompat.view.ActionMode actionMode;

    String id_paket;
    String nama_paket;

    ArrayList<Catatan> mData = new ArrayList<>();

    // action mode
    public static boolean isInActionMode = false;
    public static ArrayList<Catatan> selectionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_catatan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        nama_paket = intent.getStringExtra("pa_nama");
        id_paket = intent.getStringExtra("pa_id");
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

    private void generateProgressList(final ArrayList<Catatan> catatanArrayList){
        recyclerView = findViewById(R.id.recycle_catatan);
        catatanAdapter = new CatatanAdapter(getApplicationContext(), catatanArrayList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public boolean onItemLongClick(View view, final int position, final String list_id) {
                if(actionMode != null){
                    return false;
                }

                new AlertDialog.Builder(ActivityCatatan.this)
                        .setTitle("Hapus Catatan")
                        .setMessage("Anda yakin ingin menghapus item ini ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                Call<DataResponseCatatan> removeCatatan = apiInterface.removeCatatan(list_id);
                                removeCatatan.enqueue(new Callback<DataResponseCatatan>() {
                                    @Override
                                    public void onResponse(Call<DataResponseCatatan> call, Response<DataResponseCatatan> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<DataResponseCatatan> call, Throwable t) {

                                    }
                                });
                                catatanArrayList.remove(position);
                                catatanAdapter.notifyItemRemoved(position);
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
        });
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

    public void prepareToolbar(int position){
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.right_menu_delete);
        isInActionMode = true;
        catatanAdapter.notifyDataSetChanged();
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void prepareSelection(int position) {
        if (!selectionList.contains(mData.get(position))) {
            selectionList.add(mData.get(position));
        } else {
            selectionList.remove(mData.get(position));
        }

        updateViewCounter();
    }

    private void updateViewCounter() {
        int counter = selectionList.size();
        if (counter == 1) {
            // edit
            toolbar.getMenu().getItem(0).setVisible(true);
        } else {
            toolbar.getMenu().getItem(0).setVisible(false);
        }

        toolbar.setTitle(counter + " item(s) selected");
    }

    public void clearActionMode() {
        isInActionMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.right_menu_delete);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        toolbar.setTitle(R.string.app_name);
        selectionList.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public int getCheckedLastPosition() {
        ArrayList<Catatan> dataSet = catatanAdapter.getDataSet();
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).equals(selectionList.get(0))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catatanAdapter = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
