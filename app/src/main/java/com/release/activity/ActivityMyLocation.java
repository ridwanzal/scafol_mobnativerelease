package com.release.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.release.R;
import com.release.model.NominatimReverseMap;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiClientCustom;
import com.release.restapi.ApiInterface;
import com.release.restapi.ApiInterfaceCustom;

public class ActivityMyLocation extends AppCompatActivity {
    MapView mymap = null;
    Context context;
    GeoPoint startPoint;
    Marker startMarker;
    Handler mHandler;
    ProgressDialog progressDialog;
    ApiInterfaceCustom apiInterfaceCustom = ApiClientCustom.getClientCustom().create(ApiInterfaceCustom.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_mapmylocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(ActivityMyLocation.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(ActivityMyLocation.this);
        try{
              mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        // Do it all with location
                        // Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                        // Display in Toast
                        // Toast.makeText(ActivityMyLocation.this,
                        // "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
                        // Toast.LENGTH_LONG).show();
                        mymap = findViewById(R.id.mymap);
                        mymap.setTileSource(TileSourceFactory.MAPNIK);
                        mymap.setBuiltInZoomControls(true);
                        mymap.setAccessibilityPaneTitle("Tes");
                        mymap.setUseDataConnection(true);
                        mymap.setFlingEnabled(true);
                        mymap.setZoomRounding(true);
                        mymap.setMultiTouchControls(true);
                        startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        startMarker = new Marker(mymap);
                        startMarker.setPosition(startPoint);
                        startMarker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                        startMarker.setTextLabelFontSize(14);
                        startMarker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));
                        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_locations_on_black_60dp));
                        startMarker.setVisible(true);
                        Call<NominatimReverseMap> call_reverselatlong = apiInterfaceCustom.reverseLatLang("json", String.valueOf(location.getLatitude()),  String.valueOf(location.getLongitude()), "18", "1");
                        String concat = "Lat : " + location.getLatitude() + ", Long : "  + location.getLongitude() + "";
                        getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + concat + "</small>"));
                        call_reverselatlong.enqueue(new Callback<NominatimReverseMap>() {
                            @Override
                            public void onResponse(Call<NominatimReverseMap> call, Response<NominatimReverseMap> response) {
                                if(response.code() == 200){
                                    startMarker.setTitle(response.body().getDisplay_name());
                                    startMarker.showInfoWindow();
                                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                Thread.sleep(10);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            mHandler.sendMessage(Message.obtain(mHandler, 1));
                                        }
                                    }).start();
                                }
                            }
                            @Override
                            public void onFailure(Call<NominatimReverseMap> call, Throwable t) {

                            }
                        });

                        IMapController mapController = mymap.getController();
                        mapController.setZoom(17);
                        mapController.stopPanning();
                        mapController.setCenter(startPoint);
                        // set marker
                        mymap.getOverlays().add(startMarker);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        progressDialog.dismiss();
                        break;
                }
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
