package com.release.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import com.google.gson.Gson;
import com.release.R;
import com.release.model.DataResponsePaket;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedpreferences.SessionManager;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMapDashboard extends AppCompatActivity {
    SessionManager sessionManager;
    String user_id;
    String dians_id;
    MapView dashmap = null;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapdashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");

        dashmap = findViewById(R.id.dashmap);
        dashmap.setTileSource(TileSourceFactory.MAPNIK);
        dashmap.setMultiTouchControls(true);

        Call<DataResponsePaket> call_paket = apiInterface.getPaketPptk(user_id);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                String response_code = new Gson().toJson(response.code()).toString();
                String title = "Total Paket (" + String.valueOf(response.body().getData().size()) + ")";
                if(response.code() == 200){
                    getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + title + "</small>"));

                    for(int i = 0; i < response.body().getData().size(); i++){
                        Double latitude = Double.valueOf(response.body().getData().get(i).getPaLocLatitude());
                        Double longitude = Double.valueOf(response.body().getData().get(i).getPaLongitude());
                        GeoPoint point = new GeoPoint(latitude, longitude);
                        Marker marker = new Marker(dashmap);
                        marker.setPosition(point);
                        marker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                        marker.setTextLabelFontSize(14);
                        marker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));
                        marker.setIcon(getResources().getDrawable(R.drawable.ic_locations_on_black_60dp));
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        marker.setTitle(response.body().getData().get(i).getPaLokasi());
                        marker.setVisible(true);
                        marker.setPanToView(true);
                        IMapController mapController = dashmap.getController();
                        mapController.setZoom(14);
                        mapController.stopPanning();
                        mapController.setCenter(point);
                        // set marker
                        dashmap.getOverlays().add(marker);
//                        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
//                            @Override
//                            public boolean onMarkerClick(Marker marker, MapView mapView) {
//                                Toasty.success(getApplicationContext(), "Ak ngeklik kamu", Toasty.LENGTH_SHORT).show();
//                                return true;
//                            }
//                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
            default :
                return super.onOptionsItemSelected(item);
        }
    }

}
