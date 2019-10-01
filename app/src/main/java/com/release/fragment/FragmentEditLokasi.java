package com.release.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.release.R;
import com.release.activity.ActivityMapDetail;
import com.release.model.DataResponsePaket;
import com.release.model.NominatimReverseMap;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiClientCustom;
import com.release.restapi.ApiInterface;
import com.release.restapi.ApiInterfaceCustom;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentEditLokasi extends Fragment {
    public static String TAG = "FragmentEditLokasi";
    MapView map = null;
    MapEventsReceiver mapEventsReceiver;
    Context context;
    GeoPoint startPoint;
    ProgressBar progressBar;
    Marker startMarker;
    EditText tx_latitude;
    EditText tx_longitude;
    ImageView setmylocation;
    EditText tx_locname;
    Button btn_changelocation;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    ApiInterfaceCustom apiInterfaceCustom = ApiClientCustom.getClientCustom().create(ApiInterfaceCustom.class);
    Retrofit retrofit_custom;
    Handler mHandler;
    String id_paket;
    IMapController mapController;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_editlokasi, container, false);
        final Context ctx = getActivity();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        tx_latitude = view.findViewById(R.id.edittext_lat);
        tx_longitude = view.findViewById(R.id.edittext_longitude);
        tx_locname = view.findViewById(R.id.edittext_namalokasi);
        setmylocation = view.findViewById(R.id.setmylocation);
        btn_changelocation = view.findViewById(R.id.btn_changelocation);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        map = (MapView) view.findViewById(R.id.map2);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setZoomRounding(true);
        map.setTilesScaledToDpi(true);
        mapController = map.getController();
        mapController.setZoom(11);
        startPoint = new GeoPoint(-2.9547949, 104.6929245);
        mapController.setCenter(startPoint);
        Intent intent = getActivity().getIntent();
        final String id_paket = intent.getStringExtra("pa_id");
        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                Log.w(TAG, "GET DATA PAKETSSSSSSSSSSSSSSSSS" + new Gson().toJson(response.body().getData()));
                ArrayList<Paket> paketlist = response.body().getData();
                for(int i = 0; i < paketlist.size(); i++){
                    String location_name = "";
                    if(paketlist.get(i).getPaLokasi() == ""){
                        location_name = "Lokasi belum di set";
                    }else{
                        location_name = paketlist.get(i).getPaLokasi();
                    }
                    Double latitude = Double.valueOf(paketlist.get(i).getPaLocLatitude());
                    Double longitude = Double.valueOf(paketlist.get(i).getPaLongitude());
                    tx_locname.setText(paketlist.get(i).getPaLokasi());
                    tx_latitude.setText(paketlist.get(i).getPaLocLatitude());
                    tx_longitude.setText(paketlist.get(i).getPaLongitude());
                    startPoint = new GeoPoint(latitude, longitude);
                    startMarker = new Marker(map);
                    startMarker.setPosition(startPoint);
                    startMarker.setTextLabelBackgroundColor(getResources().getColor(R.color.colorMain));
                    startMarker.setTextLabelFontSize(9);
                    startMarker.setTextLabelForegroundColor(getResources().getColor(R.color.colorMain));
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    startMarker.setIcon(getResources().getDrawable(R.drawable.ic_locations_on_black_60dp));
                    startMarker.setTitle(location_name);
                    startMarker.showInfoWindow();
                    startMarker.setDraggable(true);
                    startMarker.setVisible(true);
                    startMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDrag(Marker marker) {
//                            Log.d(TAG, "ON DRAG SOMETHING " + marker.getPosition());
                            String data_map = marker.getPosition().toString();
                            String[] result = data_map.split(",");
                            Log.d(TAG, "ON DRAG SOMETHING " + result[0] + " | " + result[1]);
                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            progressBar.setVisibility(View.VISIBLE);
                            String data_map = marker.getPosition().toString();
                            String[] result = data_map.split(",");
                            Log.d(TAG, "ON DRAG SOMETHING " + result[0] + " | " + result[1]);
                            startPoint = new GeoPoint(Double.valueOf(result[0]), Double.valueOf(result[1]));
                            tx_latitude.setText(result[0]);
                            tx_longitude.setText(result[1]);
                            btn_changelocation.setVisibility(View.VISIBLE);
                            Call<NominatimReverseMap> call_reverselatlong = apiInterfaceCustom.reverseLatLang("json", result[0],  result[1], "18", "1");
                            call_reverselatlong.enqueue(new Callback<NominatimReverseMap>() {
                                @Override
                                public void onResponse(Call<NominatimReverseMap> call, Response<NominatimReverseMap> response) {
                                    Log.d(TAG, "INI RESPONSE BORORORORORORO===========>" + new Gson().toJson(response));
                                    if(response.isSuccessful()){
                                        Log.d(TAG, "INI BERHASIL ================> " + response.body().getDisplay_name());
                                        tx_locname.setText(response.body().getDisplay_name());
                                        startMarker.setTitle(response.body().getDisplay_name());
                                        startMarker.showInfoWindow();
                                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onFailure(Call<NominatimReverseMap> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }
                    });
                    mapController.setCenter(startPoint);
                    map.getOverlays().add(startMarker);
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        setmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMyLocation();
            }
        });


        btn_changelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressBar.setVisibility(View.VISIBLE);
                String get_lat = tx_latitude.getText().toString();
                String get_long = tx_longitude.getText().toString();
                String get_name = tx_locname.getText().toString();
                Log.d(TAG, "lATITUDE : " + get_lat + " LONGITUDE : " + get_long + " | " + id_paket);
                Call<DataResponsePaket> call_update = apiInterface.updateMap(id_paket, get_name, get_lat, get_long);
                call_update.enqueue(new Callback<DataResponsePaket>() {
                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                            Log.d(TAG, "Response Result Success------------------------> : " + response.body());
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Response Result Success------------------------> : " + response.body());
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(ctx, "Berhasil Ubah Lokasi", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(500);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                                mHandler.sendMessage(Message.obtain(mHandler, 1));
                            }
                        }).start();
                        mHandler = new Handler(Looper.myLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what){
                                    case 1 :
                                        Toasty.success(getActivity(), "Lokasi berhasil diubah", Toasty.LENGTH_LONG).show();
                                        btn_changelocation.setVisibility(View.GONE);
                                        break;
                                    case 2 :
                                        Toasty.success(getActivity(), "Lokasi berhasil diubah", Toasty.LENGTH_LONG).show();
                                        btn_changelocation.setVisibility(View.GONE);
                                        break;
                                    case 3 :
                                        Toasty.success(getActivity(), "Lokasi berhasil diubah", Toasty.LENGTH_LONG).show();
                                        btn_changelocation.setVisibility(View.GONE);
                                        break;
                                }
                            }
                        };
                    }
                });
            }
        });
        return view;
    }


    public void setMyLocation(){
        progressBar.setVisibility(View.VISIBLE);
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            mFusedLocation.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null){
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
                        call_reverselatlong.enqueue(new Callback<NominatimReverseMap>() {
                            @Override
                            public void onResponse(Call<NominatimReverseMap> call, Response<NominatimReverseMap> response) {
                                if(response.code() == 200){
                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View views = inflater.inflate(R.layout.dialog_mapdetail, null);
                                    startMarker.setTitle(response.body().getDisplay_name());
                                    tx_locname.setText(response.body().getDisplay_name().toString());
                                    tx_longitude.setText(String.valueOf(location.getLongitude()));
                                    tx_latitude.setText(String.valueOf(location.getLatitude()));
                                    startMarker.showInfoWindow();
                                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                    btn_changelocation.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onFailure(Call<NominatimReverseMap> call, Throwable t) {

                            }
                        });

                        mapController.setZoom(17);
                        mapController.stopPanning();
                        mapController.setCenter(startPoint);
                        // set marker
                        map.getOverlays().add(startMarker);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.right_menu_paketmap, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.nav_pinmylocation).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onResume();
    }
}
