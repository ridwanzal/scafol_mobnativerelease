package com.release.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.release.R;
import com.release.dropbox.FilesActivity;
import com.release.dropbox.UserActivity;
import com.release.model.DataResponsePaket;
import com.release.model.Paket;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.formatMoneyIDR;
import com.release.sharedpreferences.SessionManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailPaket extends AppCompatActivity {
    private static String TAG = "ActivityDetailPaket";
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private SupportMapFragment mapFragment;

    private TextView text_judul;
    private TextView text_jenis;
    private TextView text_tahun;
    private TextView text_pagu;

    private TextView text_satuan;
    private TextView text_volume;
    private TextView text_tanggal_mulai;
    private TextView text_tanggal_akhir;

    private TextView text_namapptk;
    private TextView text_emailpptk;
    private TextView text_telpptk;
    private TextView text_bidangpptk;

    private TextView text_nilaikontrak;
    private TextView text_progress;

    private Context mContext;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    int PICK_IMAGE_REQUEST = 1;

    private ProgressBar progressBar;
    private CardView cardView;


    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paketdetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        String role = user.get(SessionManager.KEY_ROLE);
        String dinas_id =  user.get(SessionManager.KEY_DINASID);
        String user_id =  user.get(SessionManager.KEY_USERID);

        String bi_id = "";
        final String user_fullname = user.get(SessionManager.KEY_NAME);
        String user_name = user.get(SessionManager.KEY_USERNAME);

        text_namapptk = findViewById(R.id.text_namapptk);

        text_judul = findViewById(R.id.text_judul);
        text_jenis = findViewById(R.id.text_jenis);
        text_tahun = findViewById(R.id.text_tahun);
        text_pagu = findViewById(R.id.text_pagu);

        text_satuan = findViewById(R.id.text_satuan);
        text_volume = findViewById(R.id.text_volume);
        text_tanggal_mulai = findViewById(R.id.text_date_created);
        text_tanggal_akhir = findViewById(R.id.text_date_end);

        text_nilaikontrak = findViewById(R.id.prof_email);
        text_progress = findViewById(R.id.text_progress);

        cardView = findViewById(R.id.map_cards);

        Intent intent = getIntent();
        String id_paket = intent.getStringExtra("pa_id");
        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                Log.w(TAG, "Paket data" + new Gson().toJson(response.body().getData()));
                ArrayList<Paket> paketlist = response.body().getData();
                for(int i = 0; i < paketlist.size(); i++){
                    String name = paketlist.get(i).getPaJudul();
                    String jenis = paketlist.get(i).getPaJenis();
                    String tahun = paketlist.get(i).getPaTahun();
                    String pagu = paketlist.get(i).getPaPagu();
                    String satuan = paketlist.get(i).getPaSatuan();
                    String volume = paketlist.get(i).getPaVolume();
                    String status = paketlist.get(i).getStatus();
                    String tanggal_awal = paketlist.get(i).getDateCreated();
                    String tanggal_akhir = paketlist.get(i).getDateUpdated();
                    String nilai_kontrak = paketlist.get(i).getPaNilaiKontrak();

                    text_judul.setText(checkData(name));
                    text_jenis.setText(checkData(jenis));
                    text_tahun.setText(checkData(tahun));
                    text_pagu.setText("Rp. " + formatMoneyIDR.convertIDR(pagu));

                    text_namapptk.setText(checkData(user_fullname));

                    text_satuan.setText(checkData(satuan));
                    text_volume.setText(checkData(volume));
                    String result = "";
                    String[] result_temp;

                    if(tanggal_awal.equals("")){
                        result = "-";
                    }else{
                        result_temp = tanggal_awal.split(" ");
                        result = result_temp[0];
                    }

                    if(tanggal_akhir.equals("")){
                        result = "-";
                    }else{
                        result_temp = tanggal_akhir.split(" ");
                        result = result_temp[0];
                    }



                    text_tanggal_mulai.setText(result);
                    text_tanggal_akhir.setText(result);
                    text_nilaikontrak.setText("Rp. " + formatMoneyIDR.convertIDR(nilai_kontrak));
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String id_paket = intent.getStringExtra("pa_id");
                Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
                call_paket.enqueue(new Callback<DataResponsePaket>() {
                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                        Log.w(TAG, "Paket data" + new Gson().toJson(response.body().getData()));
                        ArrayList<Paket> paketlist = response.body().getData();
//                        for(int i = 0; i < paketlist.size(); i++){
//                            String name = paketlist.get(i).getPaJudul();
//                            String jenis = paketlist.get(i).getPaJenis();
//                            String tahun = paketlist.get(i).getPaTahun();
//                            String pagu = paketlist.get(i).getPaPagu();
//                            String satuan = paketlist.get(i).getPaSatuan();
//                            String volume = paketlist.get(i).getPaVolume();
//                            String status = paketlist.get(i).getStatus();
//                            String tanggal_awal = paketlist.get(i).getDateCreated();
//                            String tanggal_akhir = paketlist.get(i).getDateUpdated();
//                            String nilai_kontrak = paketlist.get(i).getPaNilaiKontrak();
//                        }
//                        Toast.makeText(ActivityDetailPaket.this, "Show map", Toast.LENGTH_SHORT).show();
                        Intent mapIntent = new Intent(ActivityDetailPaket.this, ActivityMapDetail.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST", paketlist);
                        mapIntent.putExtra("BUNDLE", args);
                        startActivity(mapIntent);
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });

            }
        });
    }

    public static String idrFormat(String data){
        if(data == null || data == "" || data.equals("")){
            return "Rp. -";
        }else{
            return "Rp. " + data;
        }
    }

    public static String checkData(String data){
        if(data == null || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    public static String checkStatus(String status){
        if(status.equals('0')){
            return "-";
        }else{
            return status;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String covertIDR(String money){
        Double uang = Double.valueOf(money);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String convert = kursIndonesia.format(uang);
        return convert;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityDetailPaket.this.finish();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intents = getIntent();
        String id_paket = intents.getStringExtra("pa_id");
        String nama_paket = intents.getStringExtra("pa_nama");
        String ke_id = intents.getStringExtra("ke_id");
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            case R.id.nav_upload :
                String path_todropbox = "";
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String dinas_id =  user.get(SessionManager.KEY_DINASID);
//                mPath = "/files/gov/16731/pa-483/photos";
                path_todropbox = "/files/gov/"+dinas_id+"/pa-"+id_paket+"/photos";
//                startActivity(FilesActivity.getIntent(ActivityDetailPaket.this, path_todropbox));
                Intent intent = new Intent(ActivityDetailPaket.this, UserActivity.class);
                intent.putExtra("path_dropbox", path_todropbox);
                intent.putExtra("pa_judul", nama_paket);
                startActivity(intent);
                return true;
//            case R.id.nav_kurvasrencana :
//                Intent intent = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
//                intent.putExtra("position", 0);
//                intent.putExtra("pa_id", id_paket);
//                intent.putExtra("pa_nama", nama_paket);
//                intent.putExtra("ke_id", ke_id);
//                startActivity(intent);
//                return true;
            case R.id.nav_editkontrak :
                Intent intent2 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent2.putExtra("position", 0);
                intent2.putExtra("pa_id", id_paket);
                intent2.putExtra("pa_nama", nama_paket);
                intent2.putExtra("ke_id", ke_id);
                startActivity(intent2);
                return true;
            case R.id.nav_editlokasi :
                Intent intent3 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent3.putExtra("position", 1);
                intent3.putExtra("pa_id", id_paket);
                intent3.putExtra("pa_nama", nama_paket);
                intent3.putExtra("ke_id", ke_id);
                startActivity(intent3);
                return true;
            case R.id.nav_progress :
                Intent intent4 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent4.putExtra("position", 2);
                intent4.putExtra("pa_id", id_paket);
                intent4.putExtra("pa_nama", nama_paket);
                intent4.putExtra("ke_id", ke_id);
                startActivity(intent4);
                return true;
            case R.id.nav_penyediajasa :
                Intent intent5 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
                intent5.putExtra("position", 3);
                intent5.putExtra("pa_id", id_paket);
                intent5.putExtra("pa_nama", nama_paket);
                intent5.putExtra("ke_id", ke_id);
                startActivity(intent5);
                return true;
//            case R.id.nav_upload :
//                Intent intent6 = new Intent(ActivityDetailPaket.this, ActivityUpdateData.class);
//                intent6.putExtra("position", 5);
//                intent6.putExtra("pa_id", id_paket);
//                intent6.putExtra("pa_nama", nama_paket);
//                intent6.putExtra("ke_id", ke_id);
//                startActivity(intent6);
//                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_detail_paket, menu);
        return true;
    }

    private void fileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
