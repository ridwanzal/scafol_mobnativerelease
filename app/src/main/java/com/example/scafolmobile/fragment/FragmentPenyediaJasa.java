package com.example.scafolmobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scafolmobile.R;
import com.example.scafolmobile.activity.ActivityDetailPaket;
import com.example.scafolmobile.activity.ActivityUpdateData;
import com.example.scafolmobile.adapter.KontraktorAdapter;
import com.example.scafolmobile.model.DataResponsePaket;
import com.example.scafolmobile.model.DataResponseUsers;
import com.example.scafolmobile.model.Paket;
import com.example.scafolmobile.model.User;
import com.example.scafolmobile.restapi.ApiClient;
import com.example.scafolmobile.restapi.ApiInterface;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// perbaikan

public class FragmentPenyediaJasa extends Fragment {
    private RecyclerView recyclerView;
    private static String TAG = "FragmentPenyediaJasa";
    private KontraktorAdapter kontraktorAdapter;
    private Button btn_tambahkontraktor;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penyedia, container, false);
        Intent intent = getActivity().getIntent();
        final String id_paket = intent.getStringExtra("pa_id");
        btn_tambahkontraktor = view.findViewById(R.id.btn_tambahkontraktor);
        recyclerView = getActivity().findViewById(R.id.recycle_listuser);
        Call<DataResponseUsers> callkontrak = apiInterface.getKontrak();
        callkontrak.enqueue(new Callback<DataResponseUsers>() {
            @Override
            public void onResponse(Call<DataResponseUsers> call, Response<DataResponseUsers> response) {
                try{
                    Log.d(TAG, "Response Call : " + call.toString());
                    Log.d(TAG, "Response : " + new Gson().toJson(response));
                    generatUserList(response.body().getData());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DataResponseUsers> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        btn_tambahkontraktor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void generatUserList(ArrayList<User> userArrayList){
        recyclerView = getActivity().findViewById(R.id.recycle_listuser);
        kontraktorAdapter = new KontraktorAdapter(userArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(kontraktorAdapter);
    }
}
