package com.example.scafolmobile.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.scafolmobile.R;
import com.example.scafolmobile.tabcompupdate.SectionPagerAdapterUpdate;
import com.google.android.material.tabs.TabLayout;

public class ActivityUpdateData extends AppCompatActivity {
    private TextView titlebardatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_updatedata);
        SectionPagerAdapterUpdate sectionPagerAdapterUpdate = new SectionPagerAdapterUpdate(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionPagerAdapterUpdate);
        Integer get_fromdetail = getIntent().getIntExtra("position", 0);
        String  get_namafromdetail = getIntent().getStringExtra("pa_nama");
//        Toast.makeText(this, "get from detail " + get_fromdetail, Toast.LENGTH_SHORT).show();
        viewPager.setCurrentItem(get_fromdetail);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabs.setupWithViewPager(viewPager);
        titlebardatas = findViewById(R.id.titlebardatas);
        if(get_namafromdetail == ""){
            titlebardatas.setText("Update Detail Paket");
        }else{
            titlebardatas.setText(get_namafromdetail);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
