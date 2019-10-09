package com.release.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ApiInterfaceCustom apiInterfaceCustom = ApiClientCustom.getClientCustom().create(ApiInterfaceCustom.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_mapmylocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkLocationPermission();
        progressDialog = new ProgressDialog(ActivityMyLocation.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        setMyLocation();
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
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // location-related task you need to do.
                        //Request location updates:
                        setMyLocation();
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) { 

                        }



                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public void setMyLocation(){
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
                        mymap.setTilesScaledToDpi(true);
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
                                                Thread.sleep(300);
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
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Allow to get your current location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ActivityMyLocation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
