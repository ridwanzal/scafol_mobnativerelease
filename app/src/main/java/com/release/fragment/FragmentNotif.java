package com.release.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;
import com.release.activity.ActivityMain;
import com.release.activity.ActivityProgressSerapan;
import com.release.adapter.KegiatanAdapter;
import com.release.adapter.NotifAdapter;
import com.release.adapter.ProgressAdapter;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponseInfo;
import com.release.model.Info;
import com.release.model.Kegiatan;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotif extends Fragment {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "ActivityProgressFisik";
    private TextView text_default;
    private RecyclerView recyclerView;
    private ProgressAdapter progressAdapter;
    private ProgressBar progressBar;
    private TextView textnofound;
    private NotifAdapter notifAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notif, container, false);
        Call<DataResponseInfo> get_allinfo = apiInterface.getNotificationInfo();
        get_allinfo.enqueue(new Callback<DataResponseInfo>() {
            @Override
            public void onResponse(Call<DataResponseInfo> call, Response<DataResponseInfo> response) {
                if(response.code() == 200){
                    generateNotifList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<DataResponseInfo> call, Throwable t) {

            }
        });


        return view;
    }

    private void generateNotifList(ArrayList<Info> infoArrayList){
        recyclerView = getView().findViewById(R.id.recycle_notification);
        notifAdapter = new NotifAdapter(infoArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notifAdapter);
    }

}
