package com.release.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.release.R;
import com.release.model.Paket;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class ActivityMapDetail extends AppCompatActivity {
    MapView map = null;
    Context context;
    GeoPoint startPoint;
    Marker startMarker;
    private static String TAG = "ActivityMapDetail";

    public ActivityMapDetail(){
        // simple constructor
    }

    public ActivityMapDetail(Context context){
        this.context = context;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_mapdetailpaket);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Paket> object = (ArrayList<Paket>) args.getSerializable("ARRAYLIST");
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        for(int i = 0; i < object.size(); i++){
            String location_name = "";

            if(object.get(i).getPaLokasi() == ""){
                location_name = "Location not set";
            }else{
                location_name = object.get(i).getPaLokasi();
            }
            Double latitude = Double.valueOf(object.get(i).getPaLocLatitude());
            Double longitude = Double.valueOf(object.get(i).getPaLongitude());
            String concat = "Lat : " + object.get(i).getPaLocLatitude()+ ", Long : "  + object.get(i).getPaLongitude() + "";
            getSupportActionBar().setSubtitle(Html.fromHtml("<small>" + concat + "</small>"));

            startPoint = new GeoPoint(latitude, longitude);
            startMarker = new Marker(map);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
