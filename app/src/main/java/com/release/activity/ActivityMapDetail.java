package com.release.activity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.release.R;
import com.release.model.DataResponsePaket;
import com.release.model.NominatimReverseMap;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiClientCustom;
import com.release.restapi.ApiInterface;
import com.release.restapi.ApiInterfaceCustom;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMapDetail extends AppCompatActivity {
    MapView map = null;
    Context context;
    GeoPoint startPoint;
    Marker startMarker;
    Handler mHandler;
    ProgressDialog progressDialog;
    EditText nama_lokasi_edited;
    EditText latitude_edited;
    EditText longitude_edited;
    Button btn_location_edited;
    Dialog dialog_editlocation;
    String pa_id;

    private static String TAG = "ActivityMapDetail";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ApiInterfaceCustom apiInterfaceCustom = ApiClientCustom.getClientCustom().create(ApiInterfaceCustom.class);
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public ActivityMapDetail(){
        // simple constructor
    }

    public ActivityMapDetail(Context context){
        this.context = context;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_mapdetailpaket);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(ActivityMapDetail.this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Paket> object = (ArrayList<Paket>) args.getSerializable("ARRAYLIST");
        progressDialog.show();
        progressDialog.setMessage("Loading");
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        startMarker = new Marker(map);
        for(int i = 0; i < object.size(); i++){
            String location_name = "";
            if(object.get(i).getPaLokasi() == ""){
                location_name = "Lokasi belum di set";
            }else{
                location_name = object.get(i).getPaLokasi();
            }
            pa_id = object.get(i).getPaId();
            Double latitude = Double.valueOf(object.get(i).getPaLocLatitude());
            Double longitude = Double.valueOf(object.get(i).getPaLongitude());
            String concat = "Lat : " + object.get(i).getPaLocLatitude()+ ", Long : "  + object.get(i).getPaLongitude() + "";
            getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + concat + "</small>"));

            startPoint = new GeoPoint(latitude, longitude);
            startMarker.setPosition(startPoint);
            startMarker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
            startMarker.setTextLabelFontSize(14);
            startMarker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setIcon(getResources().getDrawable(R.drawable.ic_locations_on_black_60dp));
            startMarker.setTitle(location_name);
            startMarker.showInfoWindow();
            startMarker.setVisible(true);
        }

        mapController.stopPanning();
        mapController.setCenter(startPoint);

        // set marker
        map.getOverlays().add(startMarker);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(Message.obtain(mHandler, 1));
            }
        }).start();


        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        progressDialog.dismiss();
                        break;
                    case 2 :
                        progressDialog.dismiss();
                        break;
                    case 3 :
                        progressDialog.dismiss();
                        Toasty.success(getApplicationContext(), "Lokasi berhasil diubah", Toasty.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_paketmap, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                return true;
            case R.id.nav_pinmylocation :
                progressDialog.show();
                checkLocationPermission();
                setMyLocation();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    public void setMyLocation(){
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(ActivityMapDetail.this);
        try{
            mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null){
                        map = findViewById(R.id.map);
                        map.setTileSource(TileSourceFactory.MAPNIK);
                        map.setBuiltInZoomControls(true);
                        map.setUseDataConnection(true);
                        map.setFlingEnabled(true);
                        map.setZoomRounding(true);
                        map.setTilesScaledToDpi(true);
                        map.setMultiTouchControls(true);
                        startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
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
                                    LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View views = inflater.inflate(R.layout.dialog_mapdetail, null);
                                    nama_lokasi_edited = views.findViewById(R.id.nama_lokasi_edited);
                                    latitude_edited = views.findViewById(R.id.latitude_edited);
                                    longitude_edited = views.findViewById(R.id.longitude_edited);
                                    btn_location_edited  = views.findViewById(R.id.btn_location_edited);

                                    startMarker.setTitle(response.body().getDisplay_name());
                                    nama_lokasi_edited.setText(response.body().getDisplay_name().toString());
                                    longitude_edited.setText(String.valueOf(location.getLongitude()));
                                    latitude_edited.setText(String.valueOf(location.getLatitude()));
                                    startMarker.showInfoWindow();
                                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    dialog_editlocation = new BottomSheetDialog(ActivityMapDetail.this);
                                    dialog_editlocation.setContentView(views);
                                    dialog_editlocation.show();
                                    btn_location_edited.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            progressDialog.show();
                                            String get_lat = latitude_edited.getText().toString();
                                            String get_long = longitude_edited.getText().toString();
                                            String get_name = nama_lokasi_edited.getText().toString();

                                            Call<DataResponsePaket> call_update = apiInterface.updateMap(pa_id, get_name, get_lat, get_long);
                                            call_update.enqueue(new Callback<DataResponsePaket>() {
                                                @Override
                                                public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                                                }

                                                @Override
                                                public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try{
                                                                Thread.sleep(500);
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                            progressDialog.dismiss();
                                                            mHandler.sendMessage(Message.obtain(mHandler, 3));
                                                        }
                                                    }).start();
                                                }
                                            });

                                        }
                                    });


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

                        IMapController mapController = map.getController();
                        mapController.setZoom(17);
                        mapController.stopPanning();
                        mapController.setCenter(startPoint);
                        // set marker
                        map.getOverlays().add(startMarker);
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(10);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mHandler.sendMessage(Message.obtain(mHandler, 2));
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
        Boolean check_permission = false;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){

        }else{
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
                                    ActivityCompat.requestPermissions(ActivityMapDetail.this,
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
                check_permission = false;
            } else {
                check_permission = true;
            }
        }
        return check_permission;
    }


}
