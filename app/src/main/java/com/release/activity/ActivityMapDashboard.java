package com.release.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.release.R;
import com.release.model.DataResponseDinas;
import com.release.model.DataResponsePaket;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.Kriteria;
import com.release.sharedpreferences.SessionManager;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMapDashboard extends AppCompatActivity {
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    Handler mHandler;
    String user_id;
    String dinas_id;
    String bi_id;
    IMapController mapController;
    MapView dashmap = null;
    ImageView center_to_map;
    Dialog dialoginfo;
    FrameLayout main_mapdash_container;


    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        setContentView(R.layout.activity_mapdashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.LOCATION_HARDWARE,
                Manifest.permission.CONTROL_LOCATION_UPDATES,
        };
        if(!checkAllPermission(getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(ActivityMapDashboard.this, PERMISSIONS, PERMISSION_ALL);
        }else{
        }

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        dashmap = findViewById(R.id.dashmap);
        main_mapdash_container = findViewById(R.id.main_mapdash_container);
        dashmap.setTileSource(TileSourceFactory.MAPNIK);
        dashmap.setMultiTouchControls(true);
        dashmap.setFlingEnabled(true);
        dashmap.setUseDataConnection(true);
        dashmap.setZoomRounding(true);
        dashmap.canZoomIn();
        dashmap.canZoomOut();
        dashmap.computeScroll();
        center_to_map = findViewById(R.id.center_to_map);
        mapController = dashmap.getController();
        main_mapdash_container.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(ActivityMapDashboard.this);

        String role;
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        role = user.get(SessionManager.KEY_ROLE);
        dinas_id =  user.get(SessionManager.KEY_DINASID);
        bi_id = user.get(SessionManager.KEY_BIDANG);

        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);

        center_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<DataResponseDinas> call_dinas = apiInterface.getDinasDetail(dinas_id);
                call_dinas.enqueue(new Callback<DataResponseDinas>() {
                    @Override
                    public void onResponse(Call<DataResponseDinas> call, Response<DataResponseDinas> response) {
                        if(response.code() == 200){
                            for(int i = 0; i < response.body().getData().size(); i++){
                                final Double latitude;
                                final Double longitude;
                                latitude =Double.valueOf(response.body().getData().get(i).getLatitude());
                                longitude =Double.valueOf(response.body().getData().get(i).getLongitude());
                                final GeoPoint point = new GeoPoint(latitude, longitude);
                                mapController.setCenter(point);
                                mapController.setZoom(11);
                                mapController.zoomOut();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponseDinas> call, Throwable t) {

                    }
                });
            }
        });


        switch (role){
            case  "Admin" :
                progressDialog.show();
                progressDialog.setMessage("Loading");
                Call<DataResponsePaket> call_dinas = apiInterface.getMapDinas(dinas_id);
                call_dinas.enqueue(new Callback<DataResponsePaket>() {
                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                        String title = "Total Paket (" + String.valueOf(response.body().getData().size()) + ")";
                        if(response.code() == 200){
                            getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
                            for(int i = 0; i < response.body().getData().size(); i++){
                                    final Double latitude;
                                    final Double longitude;

                                    Boolean check3 = response.body().getData().get(i).getPaLocLatitude().equals("0");
                                    Boolean check4 = response.body().getData().get(i).getPaLongitude().equals("0");

                                    if(check3){
                                        latitude = 	-2.990934;
                                    }else{
                                        latitude = Double.valueOf(response.body().getData().get(i).getPaLocLatitude());
                                    }

                                    if(check4){
                                        longitude = 104.756554;
                                    }else{
                                        longitude = Double.valueOf(response.body().getData().get(i).getPaLongitude());
                                    }

                                    GeoPoint point = new GeoPoint(latitude, longitude);
                                    Marker marker = new Marker(dashmap);
                                    final String pa_id = response.body().getData().get(i).getPaId();
                                    final String pa_nama = response.body().getData().get(i).getPaJudul();
                                    final String ke_id = response.body().getData().get(i).getKeId();
                                    final String pr_tanggal = response.body().getData().get(i).getPrTanggal();
                                    final String pr_real = response.body().getData().get(i).getPrReal();
                                    final String pr_target = response.body().getData().get(i).getPrTarget();

                                    marker.setPosition(point);
                                    marker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                                    marker.setTextLabelFontSize(2);
                                    marker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));

                                    if(pr_tanggal != null && pr_real != null && pr_target != null){
                                        String kriteria = Kriteria.get_kriteria(pr_tanggal, pr_real, pr_target);
                                        switch (kriteria.toLowerCase()){
                                            case "kritis" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_kritis));
                                                break;
                                            case "terlambat" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_lambat));
                                                break;
                                            case "wajar" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_wajar));
                                                break;
                                            case "baik" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
                                                break;
                                            case "selesai" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
                                                break;
                                            case "belum mulai" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
                                                break;
                                        }
                                    }else{
                                        marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
                                    }

                                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    marker.setTitle(pa_nama+ " - " + response.body().getData().get(i).getPaLokasi());
                                    marker.setVisible(true);
                                    marker.setPanToView(true);
                                    mapController = dashmap.getController();
                                    mapController.setZoom(11);
                                    mapController.stopPanning();
                                    mapController.setCenter(point);
                                    // set marker
                                    dashmap.getOverlays().add(marker);
                                    marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker, MapView mapView) {
                                            marker.setSnippet(latitude + ", " + longitude);
                                            marker.showInfoWindow();
                                            Intent intent = new Intent(getApplicationContext(), ActivityDetailPaket.class);
                                            intent.putExtra("pa_id", pa_id);
                                            intent.putExtra("pa_nama", pa_nama);
                                            intent.putExtra("ke_id", ke_id);
                                            intent.putExtra("request", "map_dash");
                                            startActivity(intent);
                                            return true;
                                        }
                                    });
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                Thread.sleep(100);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            mHandler.sendMessage(Message.obtain(mHandler, 2));
                                        }
                                    }).start();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                    }
                });
            break;
            case  "Bidang" :
                    progressDialog.show();
                    progressDialog.setMessage("Loading");
                    Call<DataResponsePaket> call_bidang = apiInterface.getMapBidang(bi_id);
                    call_bidang.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            String title = "Total Paket (" + String.valueOf(response.body().getData().size()) + ")";
                            if(response.code() == 200){
                                getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
                                for(int i = 0; i < response.body().getData().size(); i++){
                                    final Double latitude;
                                    final Double longitude;

                                    Boolean check3 = response.body().getData().get(i).getPaLocLatitude().equals("0");
                                    Boolean check4 = response.body().getData().get(i).getPaLongitude().equals("0");

                                    if(check3){
                                        latitude = 	-2.990934;
                                    }else{
                                        latitude = Double.valueOf(response.body().getData().get(i).getPaLocLatitude());
                                    }

                                    if(check4){
                                        longitude = 104.756554;
                                    }else{
                                        longitude = Double.valueOf(response.body().getData().get(i).getPaLongitude());
                                    }

                                    GeoPoint point = new GeoPoint(latitude, longitude);
                                    Marker marker = new Marker(dashmap);
                                    final String pa_id = response.body().getData().get(i).getPaId();
                                    final String pa_nama = response.body().getData().get(i).getPaJudul();
                                    final String ke_id = response.body().getData().get(i).getKeId();
                                    final String pr_tanggal = response.body().getData().get(i).getPrTanggal();
                                    final String pr_real = response.body().getData().get(i).getPrReal();
                                    final String pr_target = response.body().getData().get(i).getPrTarget();

                                    marker.setPosition(point);
                                    marker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                                    marker.setTextLabelFontSize(2);
                                    marker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));

                                    if(pr_tanggal != null && pr_real != null && pr_target != null){
                                        String kriteria = Kriteria.get_kriteria(pr_tanggal, pr_real, pr_target);
                                        switch (kriteria.toLowerCase()){
                                            case "kritis" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_kritis));
                                                break;
                                            case "terlambat" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_lambat));
                                                break;
                                            case "wajar" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_wajar));
                                                break;
                                            case "baik" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
                                                break;
                                            case "selesai" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
                                                break;
                                            case "belum mulai" :
                                                marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
                                                break;
                                        }
                                    }else{
                                        marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
                                    }

                                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    marker.setTitle(pa_nama+ " - " + response.body().getData().get(i).getPaLokasi());
                                    marker.setVisible(true);
                                    marker.setPanToView(true);
                                    mapController = dashmap.getController();
                                    mapController.setZoom(11);
                                    mapController.stopPanning();
                                    mapController.setCenter(point);
                                    // set marker
                                    dashmap.getOverlays().add(marker);
                                    marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker, MapView mapView) {
                                            marker.setSnippet(latitude + ", " + longitude);
                                            marker.showInfoWindow();
                                            Intent intent = new Intent(getApplicationContext(), ActivityDetailPaket.class);
                                            intent.putExtra("pa_id", pa_id);
                                            intent.putExtra("pa_nama", pa_nama);
                                            intent.putExtra("ke_id", ke_id);
                                            intent.putExtra("request", "map_dash");
                                            startActivity(intent);
                                            return true;
                                        }
                                    });
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                Thread.sleep(100);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            mHandler.sendMessage(Message.obtain(mHandler, 2));
                                        }
                                    }).start();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                        }
                    });
                break;
            case  "Pptk" :
                progressDialog.show();
                progressDialog.setMessage("Loading");
                Call<DataResponsePaket> callpptk = apiInterface.getMapPPTK(user_id);
                callpptk.enqueue(new Callback<DataResponsePaket>() {

                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                        String title = "Total Paket (" + String.valueOf(response.body().getData().size()) + ")";
                        if(response.code() == 200){
                            getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));
                            for(int i = 0; i < response.body().getData().size(); i++){
                                final Double latitude;
                                final Double longitude;

                                Boolean check3 = response.body().getData().get(i).getPaLocLatitude().equals("0");
                                Boolean check4 = response.body().getData().get(i).getPaLongitude().equals("0");

                                if(check3){
                                    latitude = 	-2.990934;
                                }else{
                                    latitude = Double.valueOf(response.body().getData().get(i).getPaLocLatitude());
                                }

                                if(check4){
                                    longitude = 104.756554;
                                }else{
                                    longitude = Double.valueOf(response.body().getData().get(i).getPaLongitude());
                                }

                                GeoPoint point = new GeoPoint(latitude, longitude);
                                Marker marker = new Marker(dashmap);
                                final String pa_id = response.body().getData().get(i).getPaId();
                                final String pa_nama = response.body().getData().get(i).getPaJudul();
                                final String ke_id = response.body().getData().get(i).getKeId();
                                final String pr_tanggal = response.body().getData().get(i).getPrTanggal();
                                final String pr_real = response.body().getData().get(i).getPrReal();
                                final String pr_target = response.body().getData().get(i).getPrTarget();

                                marker.setPosition(point);
                                marker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                                marker.setTextLabelFontSize(2);
                                marker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));

                                if(pr_tanggal != null && pr_real != null && pr_target != null){
                                    String kriteria = Kriteria.get_kriteria(pr_tanggal, pr_real, pr_target);
                                    switch (kriteria.toLowerCase()){
                                        case "kritis" :
                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_kritis));
                                            break;
                                        case "terlambat" :
                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_lambat));
                                            break;
                                        case "wajar" :
                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_wajar));
                                            break;
                                        case "baik" :
                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
                                            break;
                                        case "selesai" :
                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
                                            break;
                                        case "belum mulai" :
                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
                                            break;
                                    }
                                }else{
                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
                                }

                                if (!response.body().getData().get(i).getPaLokasi().equals("")) {
                                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    marker.setTitle(pa_nama + " - " + response.body().getData().get(i).getPaLokasi());
                                    marker.setVisible(true);
                                    marker.setPanToView(true);
                                    mapController = dashmap.getController();
                                    mapController.setZoom(11);
                                    mapController.stopPanning();
                                    mapController.setCenter(point);
                                    // set marker
                                    dashmap.getOverlays().add(marker);
                                    marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker, MapView mapView) {
                                            marker.setSnippet(latitude + ", " + longitude);
                                            marker.showInfoWindow();
                                            Intent intent = new Intent(getApplicationContext(), ActivityDetailPaket.class);
                                            intent.putExtra("pa_id", pa_id);
                                            intent.putExtra("pa_nama", pa_nama);
                                            intent.putExtra("ke_id", ke_id);
                                            intent.putExtra("request", "map_dash");
                                            startActivity(intent);
                                            return true;
                                        }
                                    });
                                }

                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            Thread.sleep(100);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        mHandler.sendMessage(Message.obtain(mHandler, 2));
                                    }
                                }).start();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                    }
                });
                break;
            default:
                    progressDialog.show();
                    progressDialog.setMessage("Loading");
                    Call<DataResponsePaket> call_paket = apiInterface.getMapPPTK(user_id);
                    call_paket.enqueue(new Callback<DataResponsePaket>() {
                        @Override
                        public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
//                            Toasty.success(getApplicationContext(), "Size of array : "  + response.body().getData().size(), Toasty.LENGTH_LONG).show();
                            String title = "Total Paket (" + String.valueOf(response.body().getData().size()) + ")";
                            if(response.code() == 200){
                                getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));

                                for(int i = 0; i < response.body().getData().size(); i++){
                                        Double latitude;
                                        Double longitude;
                                        Boolean check3 = response.body().getData().get(i).getPaLocLatitude().equals("0");
                                        Boolean check4 = response.body().getData().get(i).getPaLongitude().equals("0");

                                        if(check3){
                                            latitude = 	-2.990934;
                                        }else{
                                            latitude = Double.valueOf(response.body().getData().get(i).getPaLocLatitude());
                                        }

                                        if(check4){
                                            longitude = 104.756554;
                                        }else{
                                            longitude = Double.valueOf(response.body().getData().get(i).getPaLongitude());
                                        }

//                                        Toast.makeText(ActivityMapDashboard.this, "" + response.body().getData().get(i).getPaLongitude(), Toast.LENGTH_SHORT).show();
                                        GeoPoint point = new GeoPoint(latitude, longitude);
                                        Marker marker = new Marker(dashmap);
                                        final String pa_id = response.body().getData().get(i).getPaId();
                                        final String pa_nama = response.body().getData().get(i).getPaJudul();
                                        final String ke_id = response.body().getData().get(i).getKeId();
                                        final String pr_tanggal = response.body().getData().get(i).getPrTanggal();
                                        final String pr_real = response.body().getData().get(i).getPrReal();
                                        final String pr_target = response.body().getData().get(i).getPrTarget();
                                        final String pa_awal_kontrak = response.body().getData().get(i).getPaAwalKontrak();

                                        marker.setPosition(point);
                                        marker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                                        marker.setTextLabelFontSize(14);
                                        marker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));

//                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd");

                                        try{
                                            String thisdate = sdf.format(new Date());
                                            Date date1 = sdf.parse(pr_tanggal);
                                            Date date2 = sdf.parse(thisdate);



                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

//                                        if(pr_tanggal != null && (pr_real != null || !pr_real.equals("0")) && pr_real != null){
//                                            String kriteria = Kriteria.get_kriteria(pr_tanggal, pr_real, pr_real);
//                                            switch (kriteria.toLowerCase()){
//                                                case "kritis" :
//                                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_kritis));
//                                                    break;
//                                                case "terlambat" :
//                                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_lambat));
//                                                    break;
//                                                case "wajar" :
//                                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_wajar));
//                                                    break;
//                                                case "baik" :
//                                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
//                                                    break;
//                                                case "selesai" :
//                                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_baik));
//                                                    break;
//                                                case "belum mulai" :
//                                                    marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
//                                                    break;
//                                            }
//                                        }else{
//                                            marker.setIcon(getResources().getDrawable(R.drawable.ic_map_belum));
//                                        }

                                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                        marker.setTitle(pa_nama+ " - " + response.body().getData().get(i).getPaLokasi());
                                        marker.setVisible(true);
                                        marker.setPanToView(true);
                                        mapController = dashmap.getController();
                                        mapController.setZoom(14);
                                        mapController.stopPanning();
                                        mapController.setCenter(point);
                                        // set marker
                                        dashmap.getOverlays().add(marker);
                                        new Thread(new Runnable() {
                                            public void run() {
                                                try {
                                                    Thread.sleep(100);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                mHandler.sendMessage(Message.obtain(mHandler, 1));
                                            }
                                        }).start();

                                        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                                marker.showInfoWindow();
                                                Intent intent = new Intent(getApplicationContext(), ActivityDetailPaket.class);
                                                intent.putExtra("pa_id", pa_id);
                                                intent.putExtra("pa_nama", pa_nama);
                                                intent.putExtra("ke_id", ke_id);
                                                intent.putExtra("request", "map_dash");
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                        }
                    });
                break;
        }

        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        progressDialog.dismiss();
                        main_mapdash_container.setVisibility(View.VISIBLE);
                        break;
                    case 2 :
                        progressDialog.dismiss();
                        main_mapdash_container.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    private boolean checkAllPermission(Context context, String... permissions){
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
            case R.id.nav_mapdashinfo :
                openBottomDialog();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_mapinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dashmap.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dialoginfo != null){
            dialoginfo.dismiss();
        }
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dashmap.onPause();
        if(dialoginfo != null){
            dialoginfo.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialoginfo != null){
            dialoginfo.dismiss();
        }
    }

    public void openBottomDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_dashmapinfo, null);
        dialoginfo = new BottomSheetDialog(this);
        dialoginfo.setContentView(view);
        dialoginfo.show();
    }
}
