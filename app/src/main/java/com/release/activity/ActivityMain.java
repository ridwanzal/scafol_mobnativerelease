package com.release.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.release.R;
import com.release.adapter.AnggaranAdapter;
import com.release.adapter.KegiatanAdapter;
import com.release.adapter.PaketAdapter;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Anggaran;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponseKegiatan;
import com.release.model.DataResponsePaket;
import com.release.model.Kegiatan;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.release.sharedpreferences.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity{
    // Access view resource
    private static String TAG = "ActivityMain";
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_ang;
    private KegiatanAdapter kegiatanAdapter;
    private PaketAdapter paketAdapter;
    private AnggaranAdapter anggaranAdapter;
    private TextView total_paket_info;
    private ProgressBar progressBar;
    private String user_id;
    private String role;
    private String bi_id;
    private String user_fullname;
    private String user_name;
    private String dinas_id;

    private ProgressBar progress_listpaket;
    private ProgressBar progress_listanggaran;

    private SearchView searchView;

    // Service
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        role = user.get(SessionManager.KEY_ROLE);
        dinas_id =  user.get(SessionManager.KEY_DINASID);
        user_id =  user.get(SessionManager.KEY_USERID);
        bi_id = "";
        user_fullname = user.get(SessionManager.KEY_NAME);
        user_name = user.get(SessionManager.KEY_USERNAME);
//        progressBar =  (ProgressBar) findViewById(R.id.progress_bar_paketlist);
        switch (role){
            case "Admin" :
                // user admin
                setContentView(R.layout.recycle_listpaket);
                progress_listpaket = findViewById(R.id.progress_listpaket);
                Snackbar.make(findViewById(R.id.activity_paketlist), "Selamat Datang", Snackbar.LENGTH_LONG);
                Call<DataResponsePaket> call_paket2 = apiInterface.getPaketDinas(dinas_id);
                call_paket2.enqueue(new Callback<DataResponsePaket>() {
                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                        String response_code = new Gson().toJson(response.code()).toString();
                        if(response_code.equals("200")){
                            ArrayList<Paket> data = response.body().getData();
                            Log.w(TAG, "paket data " + new Gson().toJson(data));
                            generatePaketList(response.body().getData());
                            progress_listpaket.setVisibility(View.GONE);
                            recyclerView = findViewById(R.id.recycle_listpaket);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
                break;
            case "Pptk" :
                // user pptk
                String flag_list = getIntent().getStringExtra("flag_list");
                if(flag_list.equals("1")){
                    setContentView(R.layout.recycle_listpaket);
                    progress_listpaket = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Paket");
                    Snackbar.make(findViewById(R.id.activity_paketlist), "Selamat Datang", Snackbar.LENGTH_LONG);
                    Call<DataResponsePaket> call_paket = apiInterface.getPaketPptk(user_id);
                    call_paket.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Paket> data = response.body().getData();
                                if(data.size() == 0){
                                    progress_listpaket.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generatePaketList(response.body().getData());
                                progress_listpaket.setVisibility(View.GONE);
                                recyclerView = findViewById(R.id.recycle_listpaket);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }else if(flag_list.equals("2")){
                    setContentView(R.layout.recycle_listanggaran);
                    progress_listanggaran = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Anggaran");
                    Snackbar.make(findViewById(R.id.activity_paketlist), "Selamat Datang", Snackbar.LENGTH_LONG);
                    Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaranPPTK(user_id);
                    call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
                        @Override
                        public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Anggaran> data = response.body().getData();
                                if(data.size() == 0){
                                    progress_listpaket.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generateAnggaranList(response.body().getData());
                                progress_listanggaran.setVisibility(View.GONE);
                                recyclerView_ang = findViewById(R.id.recycle_listanggaran);
                                recyclerView_ang.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }
                break;
            case "Bidang" :
                // user bidang
                setContentView(R.layout.recycle_listkegiatan);
                if(bi_id.equals("") || bi_id == null){
                    sessionManager.checkLogin();
                }
                Call<DataResponsePaket> call_kegiatan_bidang = apiInterface.getPaketBidang(user_id);
                call_kegiatan_bidang.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            ArrayList<Paket> data = response.body().getData();
                            Log.w(TAG, "kegiatan data " + new Gson().toJson(data));
                            generatePaketList(response.body().getData());
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                break;
            case "Keuangan" :
                break;
        }
    }

    private void generateKegiatanList(ArrayList<Kegiatan> kegiatanList){
        recyclerView = findViewById(R.id.recycle_listkegiatan);
        kegiatanAdapter = new KegiatanAdapter(kegiatanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityMain.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kegiatanAdapter);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void generatePaketList(ArrayList<Paket> paketList){
        recyclerView = findViewById(R.id.recycle_listpaket);

        paketAdapter = new PaketAdapter(getApplicationContext(), paketList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final TextView get_id;
                final TextView get_nama;
                final TextView get_keid;
                final TextView get_pagu;
                String getid_paket = "";
                String get_nama_paket = "";
                String get_kegiatan = "";
                String get_totalpagu = "";

                get_id = view.findViewById(R.id.txt_idpaket);
                get_nama = view.findViewById(R.id.txt_nama_paket);
                get_keid = view.findViewById(R.id.kegiatan_id);
                get_pagu = view.findViewById(R.id.txt_pagu_dummy);

                getid_paket = (String) get_id.getText().toString().trim();
                get_nama_paket = (String) get_nama.getText().toString().trim();
                get_kegiatan = (String) get_keid.getText().toString().trim();
                get_totalpagu = (String) get_pagu.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), ActivityDetailPaket.class);
                intent.putExtra("pa_pagu", get_totalpagu);
                intent.putExtra("pa_id", getid_paket);
                intent.putExtra("pa_nama", get_nama_paket);
                intent.putExtra("ke_id", get_kegiatan);
                intent.putExtra("request", "main");
                startActivity(intent);
            }

            @Override
            public void onItemDoubleCLick(View view, int position) {
                // do nothing here
            }

            @Override
            public void onDoubleClick(View view, int position) {
                // do nothing here
            }
        });

        total_paket_info = findViewById(R.id.total_paket_caption);
//        total_paket_info.setText("Total Paket  : "   + paketAdapter.getItemCount());
        String title = "Total Paket ("   + paketAdapter.getItemCount() + ")";
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityMain.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(paketAdapter);
        recyclerView.setVisibility(View.GONE);
    }

    private void generateAnggaranList(ArrayList<Anggaran> anggaranList){
        recyclerView_ang = findViewById(R.id.recycle_listanggaran);
        anggaranAdapter = new AnggaranAdapter(getApplicationContext(), anggaranList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final TextView get_id;
                final TextView get_nama;
                final TextView get_keid;
                final TextView get_pagu;
                String getid_paket = "";
                String get_nama_paket = "";
                String get_kegiatan = "";
                String get_totalpagu = "";

                get_id = view.findViewById(R.id.txt_idpaket);
                get_nama = view.findViewById(R.id.txt_nama_paket);
                get_keid = view.findViewById(R.id.kegiatan_id);
                get_pagu = view.findViewById(R.id.txt_pagu_dummy);

                getid_paket = (String) get_id.getText().toString().trim();
                get_nama_paket = (String) get_nama.getText().toString().trim();
                get_kegiatan = (String) get_keid.getText().toString().trim();
                get_totalpagu = (String) get_pagu.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), ActivityDetailPaket.class);
                intent.putExtra("pa_pagu", get_totalpagu);
                intent.putExtra("pa_id", getid_paket);
                intent.putExtra("pa_nama", get_nama_paket);
                intent.putExtra("ke_id", get_kegiatan);
                intent.putExtra("request", "main");
                startActivity(intent);
            }

            @Override
            public void onItemDoubleCLick(View view, int position) {
                // do nothing here
            }

            @Override
            public void onDoubleClick(View view, int position) {
                // do nothing here
            }
        });

        total_paket_info = findViewById(R.id.total_paket_caption);
//        total_paket_info.setText("Total Paket  : "   + paketAdapter.getItemCount());
        String title = "Total Anggaran ("   + anggaranAdapter.getItemCount() + ")";
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityMain.this);
        recyclerView_ang.setLayoutManager(layoutManager);
        recyclerView_ang.setAdapter(anggaranAdapter);
        recyclerView_ang.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_paket, menu);
        MenuItem searMenuItem = menu.findItem(R.id.nav_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searMenuItem);
        searchView.setMaxWidth(2147483647);
        searchView.setMinimumHeight(Integer.MAX_VALUE);
        searchView.setQueryHint("Cari Paket");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                paketAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout :
                new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                            sessionManager.logoutUser();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("MainActivity", "Aborting mission...");
                        }
                    })
                    .show();
            case R.id.nav_search :
                break;
            case R.id.nav_profile :
                Intent intent2 = new Intent(ActivityMain.this, ActivityEditProfilPPTK.class);
                intent2.putExtra("user_id", user_id);
                startActivity(intent2);
                break;
            case R.id.nav_dashboard :
                Intent intent = new Intent(ActivityMain.this, ActivityDashboard.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sessionManager.checkLogin();
    }

}
