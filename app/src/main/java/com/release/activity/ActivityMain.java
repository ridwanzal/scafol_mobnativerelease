package com.release.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.release.R;
import com.release.adapter.AnggaranAdapter;
import com.release.adapter.KegiatanAdapter;
import com.release.adapter.KegiatanAdapterAnggaran;
import com.release.adapter.PaketAdapter;
import com.release.interfacemodule.ItemClickListener;
import com.release.model.Anggaran;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponseKegiatan;
import com.release.model.DataResponseKegiatanAnggaran;
import com.release.model.DataResponsePaket;
import com.release.model.Kegiatan;
import com.release.model.KegiatanTree;
import com.release.model.KegiatanTreeAnggaran;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.release.sharedpreferences.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity{
    // Access view resource
    private static String TAG = "ActivityMain";
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_ang;
    private KegiatanAdapter kegiatanAdapter;
    private KegiatanAdapterAnggaran kegiatanAdapterAnggaran;
    private PaketAdapter paketAdapter;
    private AnggaranAdapter anggaranAdapter;
    private TextView total_paket_info;
    private TextView text_notfound;
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
    private String flag_list;

    // Service
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        role = user.get(SessionManager.KEY_ROLE);
        dinas_id =  user.get(SessionManager.KEY_DINASID);
        user_id =  user.get(SessionManager.KEY_USERID);
        bi_id = user.get(SessionManager.KEY_BIDANG);
        user_fullname = user.get(SessionManager.KEY_NAME);
        user_name = user.get(SessionManager.KEY_USERNAME);
        flag_list = getIntent().getStringExtra("flag_list");
        switch (role){
            case "Admin" :
                // user admin
                if(flag_list.equals("1")){
                    setContentView(R.layout.recycle_listpaket);
                    text_notfound = findViewById(R.id.text_notfound);
                    getSupportActionBar().setTitle("Paket Fisik");
                    progress_listpaket = findViewById(R.id.progress_listpaket);
                    Call<DataResponsePaket> call_paket2 = apiInterface.getPaketDinas(dinas_id);
                    call_paket2.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Paket> data = response.body().getData();
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generatePaketList(response.body().getData(),"admin");
                                progress_listpaket.setVisibility(View.GONE);
                                recyclerView = findViewById(R.id.recycle_listpaket);
                                recyclerView.setVisibility(View.VISIBLE);
                                text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listpaket.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }else if(flag_list.equals("2")){
                    setContentView(R.layout.recycle_listanggaran);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listanggaran = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Paket Non-Fisik");
                    Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaranAdmin(dinas_id);
                    call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
                        @Override
                        public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Anggaran> data = response.body().getData();
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generateAnggaranList(response.body().getData(), "admin");
                                progress_listanggaran.setVisibility(View.GONE);
                                recyclerView_ang = findViewById(R.id.recycle_listanggaran);
                                recyclerView_ang.setVisibility(View.VISIBLE);
                                text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }else if(flag_list.equals("3")){
                    Log.d(TAG, "MASUK SINI");
                    setContentView(R.layout.recycle_listkegiatan);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listanggaran = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Kegiatan");
                    Call<DataResponseKegiatan> call_kegiatan = apiInterface.getKegiatanTree(dinas_id);
                    call_kegiatan.enqueue(new Callback<DataResponseKegiatan>() {
                        @Override
                        public void onResponse(Call<DataResponseKegiatan> call, Response<DataResponseKegiatan> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<KegiatanTree> data = response.body().getData();
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generateKegiatanList(response.body().getData());
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseKegiatan> call, Throwable t) {

                        }
                    });
                }
                break;
            case "Pptk" :
                // user pptk
                if(flag_list.equals("1")){
                    setContentView(R.layout.recycle_listpaket);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listpaket = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Paket Fisik");
                    Call<DataResponsePaket> call_paket = apiInterface.getPaketPptk(user_id);
                    call_paket.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Paket> data = response.body().getData();
                                    Log.w(TAG, "paket data " + new Gson().toJson(data));
                                    generatePaketList(response.body().getData(), "");
                                    progress_listpaket.setVisibility(View.GONE);
                                    recyclerView = findViewById(R.id.recycle_listpaket);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listpaket.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }else if(flag_list.equals("2")){
                    setContentView(R.layout.recycle_listanggaran);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listanggaran = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Paket Non-Fisik");
                    Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaranPPTK(user_id);
                    call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
                        @Override
                        public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Anggaran> data = response.body().getData();
                                    Log.w(TAG, "paket data " + new Gson().toJson(data));
                                    generateAnggaranList(response.body().getData(), "");
                                    progress_listanggaran.setVisibility(View.GONE);
                                    recyclerView_ang = findViewById(R.id.recycle_listanggaran);
                                    recyclerView_ang.setVisibility(View.VISIBLE);
                                    text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }else if(flag_list.equals("3")){
                    Log.d(TAG, "MASUK SINI");
                    setContentView(R.layout.recycle_listkegiatan);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listanggaran = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Kegiatan");
                    Call<DataResponseKegiatan> call_kegiatan = apiInterface.getKegiatanTreePPTK(dinas_id, user_id);
                    call_kegiatan.enqueue(new Callback<DataResponseKegiatan>() {
                        @Override
                        public void onResponse(Call<DataResponseKegiatan> call, Response<DataResponseKegiatan> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<KegiatanTree> data = response.body().getData();
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generateKegiatanList(response.body().getData());
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseKegiatan> call, Throwable t) {

                        }
                    });
                }
                break;
            case "Bidang" :
                // user bidang
                if(flag_list.equals("1")){
                    setContentView(R.layout.recycle_listpaket);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listpaket = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Paket Fisik");
                    if(bi_id.equals("") || bi_id == null){
                        sessionManager.checkLogin();
                    }
                    Call<DataResponsePaket> call_paketbidang = apiInterface.getPaketBidangId(bi_id.toString());
                    call_paketbidang.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            ArrayList<Paket> data = response.body().getData();
                            Log.w(TAG, "kegiatan data " + new Gson().toJson(data));
                            if(response.code() == 200){
                                generatePaketList(response.body().getData(), "");
                                progress_listpaket.setVisibility(View.GONE);
                                recyclerView = findViewById(R.id.recycle_listpaket);
                                recyclerView.setVisibility(View.VISIBLE);
                                text_notfound.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }else if(flag_list.equals("2")){
                    setContentView(R.layout.recycle_listanggaran);
                    text_notfound = findViewById(R.id.text_notfound);
                    progress_listanggaran = findViewById(R.id.progress_listpaket);
                    getSupportActionBar().setTitle("Paket Non-Fisik");
                    Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaranBidang(bi_id, dinas_id);
                    call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
                        @Override
                        public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                            String response_code = new Gson().toJson(response.code()).toString();
                            if(response_code.equals("200")){
                                ArrayList<Anggaran> data = response.body().getData();
                                Log.w(TAG, "paket data " + new Gson().toJson(data));
                                generateAnggaranList(response.body().getData(), "");
                                progress_listanggaran.setVisibility(View.GONE);
                                recyclerView_ang = findViewById(R.id.recycle_listanggaran);
                                recyclerView_ang.setVisibility(View.VISIBLE);
                                text_notfound.setVisibility(View.GONE);
                            }else{
                                progress_listanggaran.setVisibility(View.GONE);
                                text_notfound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }
                break;
            case "Keuangan" :
                break;
        }
    }

    private void generateKegiatanList(ArrayList<KegiatanTree> kegiatanList){
        recyclerView = findViewById(R.id.recycle_listkegiatan);
        kegiatanAdapter = new KegiatanAdapter(getApplicationContext(), kegiatanList);
        String title = "Total Kegiatan ("   + kegiatanAdapter.getItemCount() + ")";
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityMain.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kegiatanAdapter);
        searchView.setQueryHint("Cari Kegiatan");
    }


    private void generateKegiatanAnggaranList(ArrayList<KegiatanTreeAnggaran> kegiatanList){
        recyclerView = findViewById(R.id.recycle_listkegiatan);
        kegiatanAdapterAnggaran = new KegiatanAdapterAnggaran(getApplicationContext(), kegiatanList);
        String title = "Total Kegiatan ("   + kegiatanAdapterAnggaran.getItemCount() + ")";
        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityMain.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kegiatanAdapterAnggaran);
        searchView.setQueryHint("Cari Kegiatan");
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void generatePaketList(ArrayList<Paket> paketList, final String request_role){
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
                switch (request_role){
                    case "admin" :
                        intent.putExtra("request", "admin");
                        break;
                    default:
                        intent.putExtra("request", "main");
                        break;
                }
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

            @Override
            public boolean onItemLongClick(View view, int position, String id_list) {
                return false;
            }

            @Override
            public void onItemClick(View view, int position, String param1) {

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

    private void generateAnggaranList(ArrayList<Anggaran> anggaranList, final String request_role){
        recyclerView_ang = findViewById(R.id.recycle_listanggaran);
        anggaranAdapter = new AnggaranAdapter(getApplicationContext(), anggaranList, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final TextView get_id;
                final TextView get_nama;
                final TextView get_keid;
                final TextView get_pagu;
                String getid_anggaran= "";
                String get_nama_anggaran = "";
                String get_kegiatan = "";
                String get_totalpagu = "";

                get_id = view.findViewById(R.id.txt_idpaket);
                get_nama = view.findViewById(R.id.txt_nama_paket);
                get_keid = view.findViewById(R.id.kegiatan_id);
                get_pagu = view.findViewById(R.id.txt_pagu_dummy);

                getid_anggaran = (String) get_id.getText().toString().trim();
                get_nama_anggaran = (String) get_nama.getText().toString().trim();
                get_kegiatan = (String) get_keid.getText().toString().trim();
                get_totalpagu = (String) get_pagu.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(), ActivityDetailAnggaran.class);
                intent.putExtra("anp_pagu", get_totalpagu);
                intent.putExtra("an_id", getid_anggaran);
                intent.putExtra("an_nama", get_nama_anggaran);
                intent.putExtra("ke_id", get_kegiatan);
                switch (request_role){
                    case "admin" :
                        intent.putExtra("request", "admin");
                        break;
                    default:
                        intent.putExtra("request", "main");
                        break;
                }
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

            @Override
            public boolean onItemLongClick(View view, int position, String id_list) {
                return false;
            }

            @Override
            public void onItemClick(View view, int position, String param1) {

            }
        });

        String title = "Total Anggaran ("   + anggaranAdapter.getItemCount() + ")";
//        Toasty.success(getApplicationContext(), "Total Anggaran : " + anggaranAdapter.getItemCount(), Toasty.LENGTH_LONG).show();
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
        if(flag_list.equals("1")){
            searchView.setQueryHint("Cari Paket");
        }else{
            searchView.setQueryHint("Cari Anggaran");
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onQueryTextChange(String s) {
                if(flag_list.equals("1")){
                    if(searchView.getQueryHint().equals("Cari Kegiatan")){
                        kegiatanAdapter.getFilter().filter(s);
                    }else{
                        paketAdapter.getFilter().filter(s);
                    }
                }else{
                    anggaranAdapter.getFilter().filter(s);
                }
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
            case R.id.nav_filter :
                View view = getLayoutInflater().inflate(R.layout.dialog_chooser_filterproject, null);
                final Dialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                dialog.show();
                TextView open_kegiatan = view.findViewById(R.id.open_kegiatan);
                TextView open_paket = view.findViewById(R.id.open_paket);
                switch (flag_list){
                    case "1" :
                            open_paket.setText("Filter berdasarkan Paket");
                        break;
                    case "2" :
                            open_paket.setText("Filter berdasarkan Paket Non-Fisik");
                        break;
                }

                open_kegiatan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(flag_list.equals("1")){
                            Log.d(TAG, "MASUK SINI");
                            setContentView(R.layout.recycle_listkegiatan);
                            text_notfound = findViewById(R.id.text_notfound);
                            progress_listanggaran = findViewById(R.id.progress_listpaket);
                            getSupportActionBar().setTitle("Kegiatan");
                            Call<DataResponseKegiatan> call_kegiatan = null;
                            switch (role){
                                case "Admin" :
                                    call_kegiatan = apiInterface.getKegiatanTree(dinas_id);
                                    break;
                                case "Bidang" :
                                    call_kegiatan = apiInterface.getKegiatanTreeBidang(dinas_id, bi_id);
                                    break;
                                case "Pptk" :
                                    call_kegiatan = apiInterface.getKegiatanTreePPTK(dinas_id, user_id);
                                    break;
                            }
                            call_kegiatan.enqueue(new Callback<DataResponseKegiatan>() {
                                @Override
                                public void onResponse(Call<DataResponseKegiatan> call, Response<DataResponseKegiatan> response) {
                                    String response_code = new Gson().toJson(response.code()).toString();
                                    if(response_code.equals("200")){
                                        ArrayList<KegiatanTree> data = response.body().getData();
                                        Log.w(TAG, "paket data " + new Gson().toJson(data));
                                        generateKegiatanList(response.body().getData());
                                        progress_listanggaran.setVisibility(View.GONE);
                                        text_notfound.setVisibility(View.GONE);
                                    }else{
                                        progress_listanggaran.setVisibility(View.GONE);
                                        text_notfound.setVisibility(View.VISIBLE);
                                    }
                                }
                                @Override
                                public void onFailure(Call<DataResponseKegiatan> call, Throwable t) {

                                }
                            });
                            dialog.dismiss();
                        }else{
                            Log.d(TAG, "MASUK SINI");
                            setContentView(R.layout.recycle_listkegiatan);
                            text_notfound = findViewById(R.id.text_notfound);
                            progress_listanggaran = findViewById(R.id.progress_listpaket);
                            getSupportActionBar().setTitle("Kegiatan");
                            Call<DataResponseKegiatanAnggaran> call_kegiatan = null;
                            switch (role){
                                case "Admin" :
                                    call_kegiatan = apiInterface.getKegiatanTreeAnggaran(dinas_id);
                                    break;
                                case "Bidang" :
                                    call_kegiatan = apiInterface.getKegiatanTreeAnggaranBidang(dinas_id, bi_id);
                                    break;
                                case "Pptk" :
                                    call_kegiatan = apiInterface.getKegiatanTreeAnggaranPPTK(dinas_id, user_id);
                                    break;
                            }
                            call_kegiatan.enqueue(new Callback<DataResponseKegiatanAnggaran>() {
                                @Override
                                public void onResponse(Call<DataResponseKegiatanAnggaran> call, Response<DataResponseKegiatanAnggaran> response) {
                                    String response_code = new Gson().toJson(response.code()).toString();
                                    if(response_code.equals("200")){
                                        ArrayList<KegiatanTreeAnggaran> data = response.body().getData();
                                        Log.w(TAG, "paket data " + new Gson().toJson(data));
                                        generateKegiatanAnggaranList(response.body().getData());
                                        progress_listanggaran.setVisibility(View.GONE);
                                        text_notfound.setVisibility(View.GONE);
                                    }else{
                                        progress_listanggaran.setVisibility(View.GONE);
                                        text_notfound.setVisibility(View.VISIBLE);
                                    }
                                }
                                @Override
                                public void onFailure(Call<DataResponseKegiatanAnggaran> call, Throwable t) {

                                }
                            });
                            dialog.dismiss();
                        }
                    }
                });

                open_paket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(flag_list.equals("1")){
                                // ini paket
                                setContentView(R.layout.recycle_listpaket);
                                text_notfound = findViewById(R.id.text_notfound);
                                getSupportActionBar().setTitle("Paket Fisik");
                                progress_listpaket = findViewById(R.id.progress_listpaket);
                                Call<DataResponsePaket> call_paket2 = null;
                                switch (role){
                                    case "Admin" :
                                        call_paket2 = apiInterface.getPaketDinas(dinas_id);
                                        break;
                                    case "Bidang" :
                                        call_paket2 = apiInterface.getPaketBidangId(bi_id);
                                        break;
                                    case "Pptk" :
                                        call_paket2 = apiInterface.getPaketPptk(user_id);
                                        break;
                                }

                                call_paket2.enqueue(new Callback<DataResponsePaket>() {
                                    @Override
                                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                                        String response_code = new Gson().toJson(response.code()).toString();
                                        if(response_code.equals("200")){
                                            ArrayList<Paket> data = response.body().getData();
                                            Log.w(TAG, "paket data " + new Gson().toJson(data));
                                            generatePaketList(response.body().getData(), "");
                                            progress_listpaket.setVisibility(View.GONE);
                                            recyclerView = findViewById(R.id.recycle_listpaket);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            text_notfound.setVisibility(View.GONE);
                                        }else{
                                            progress_listpaket.setVisibility(View.GONE);
                                            text_notfound.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                                        Log.e(TAG, t.toString());
                                    }
                                });
                                dialog.dismiss();
                        }else{
                            // ini anggaran
                            setContentView(R.layout.recycle_listanggaran);
                            text_notfound = findViewById(R.id.text_notfound);
                            getSupportActionBar().setTitle("Paket Fisik");
                            progress_listanggaran = findViewById(R.id.progress_listpaket);
                            Call<DataResponseAnggaran> call_paket3 = null;
                            switch (role){
                                case "Admin" :
                                    call_paket3 = apiInterface.getAnggaranAdmin(dinas_id);
                                    break;
                                case "Bidang" :
                                    call_paket3 = apiInterface.getAnggaranBidang(bi_id, dinas_id);
                                    break;
                                case "Pptk" :
                                    call_paket3 = apiInterface.getAnggaranPPTK(user_id);
                                    break;
                            }

                            call_paket3.enqueue(new Callback<DataResponseAnggaran>() {
                                @Override
                                public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                                    String response_code = new Gson().toJson(response.code()).toString();
                                    if(response.code() == 200){
                                        ArrayList<Anggaran> data = response.body().getData();
                                        Log.w(TAG, "paket data " + new Gson().toJson(data));
                                        generateAnggaranList(response.body().getData(), "");
                                        progress_listanggaran.setVisibility(View.GONE);
                                        recyclerView = findViewById(R.id.recycle_listanggaran);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        text_notfound.setVisibility(View.GONE);
                                    }else{
                                        progress_listpaket.setVisibility(View.GONE);
                                        text_notfound.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {
                                    Log.e(TAG, t.toString());
                                }
                            });
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.nav_search :
                break;
            case R.id.nav_profile :
                Intent intent2 = new Intent(ActivityMain.this, ActivityEditProfil.class);
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
        super.onRestart();
        super.onBackPressed();
        onBackPressedRefresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void openBottomDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        Dialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    public void onBackPressedRefresh(){
        Intent intent = new Intent(this, ActivityDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        sessionManager.checkLogin();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
