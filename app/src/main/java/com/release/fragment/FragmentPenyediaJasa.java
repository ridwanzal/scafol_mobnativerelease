package com.release.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.release.R;

import com.release.adapter.KontraktorAdapter;
import com.release.model.Anggaran;
import com.release.model.DataResponsePJ;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseUsers;
import com.release.model.Kontraktor;
import com.release.model.Paket;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.google.gson.Gson;
import com.release.sharedpreferences.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// perbaikan

public class FragmentPenyediaJasa extends Fragment {
    private RecyclerView recyclerView;
    private static String TAG = "FragmentPenyediaJasa";
    private KontraktorAdapter kontraktorAdapter;
    private Button btn_tambahkontraktor;
    private AutoCompleteTextView addlist_penyedia;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private SessionManager sessionManager;
    private String dinas_id;
    private String id_paket;
    private Button submit_kontraktor_paket;
    private TextView text_judul;
    ArrayAdapter<Kontraktor> adapter1;
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    private RelativeLayout rel_penyedia;
    private LinearLayout lin_keu1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penyedia, container, false);
        Intent intent = getActivity().getIntent();
        id_paket = intent.getStringExtra("pa_id");
        addlist_penyedia = view.findViewById(R.id.auto_listpenyedia);
        text_judul = view.findViewById(R.id.text_judul);
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        submit_kontraktor_paket = view.findViewById(R.id.submit_kontraktor_paket);
        HashMap<String, String> user = sessionManager.getUserDetails();
        dinas_id = user.get(SessionManager.KEY_DINASID);
        rel_penyedia = view.findViewById(R.id.lin_penyedia);
        lin_keu1 = view.findViewById(R.id.lin_keu1);

        final List<String> temp_kontraktor = new ArrayList<String>();

        addlist_penyedia.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (addlist_penyedia.getRight() - addlist_penyedia.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        addlist_penyedia.getText().clear();
                        return true;
                    }
                }
            return false;
            }
        });

        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                if(response.code() == 200){
                    ArrayList<Paket> paketlist = response.body().getData();
                    for(int i = 0; i < paketlist.size(); i++){
                        if(paketlist.get(i).getPaNilaiKontrak().toString().equals("0")){
                            rel_penyedia.setVisibility(View.GONE);
                            lin_keu1.setVisibility(View.VISIBLE);
                        }else{
                            rel_penyedia.setVisibility(View.VISIBLE);
                            lin_keu1.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {
            }
        });


        Call<DataResponsePJ> callkontrak = apiInterface.getPenyediaJasa(dinas_id);
        callkontrak.enqueue(new Callback<DataResponsePJ>() {
            @Override
            public void onResponse(Call<DataResponsePJ> call, Response<DataResponsePJ> response) {
                if(response.code() == 200){
                    ArrayList<Kontraktor> data = response.body().getData();
                    for(int i = 0; i< data.size(); i++){
                        String kontraktor_name = data.get(i).getKoNama();
                        temp_kontraktor.add(kontraktor_name);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePJ> call, Throwable t) {

            }
        });

        Call<DataResponsePJ> callkontrak_info = apiInterface.getPenyediaJasaByPaket(id_paket);
        callkontrak_info.enqueue(new Callback<DataResponsePJ>() {
            @Override
            public void onResponse(Call<DataResponsePJ> call, Response<DataResponsePJ> response) {
                Log.d(TAG, "callkontrak_info | " + response.code());
                if(response.code() == 200){
                    ArrayList<Kontraktor> data = response.body().getData();
                    for(int i = 0; i < data.size(); i++){
                        text_judul.setText(data.get(i).getKoNama());
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePJ> call, Throwable t) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.listautocomplete, temp_kontraktor);
        addlist_penyedia.setThreshold(1);
        addlist_penyedia.setAdapter(adapter);
        submit_kontraktor_paket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_text = addlist_penyedia.getText().toString().trim();
                if(check_text.equals("")){
                    addlist_penyedia.setError("Required");
                    addlist_penyedia.setHint("Ketik Nama Perusahaan");
                }else{
                    Log.d(TAG, "Click FragmentPenyediaJasa");
                    new AlertDialog.Builder(getActivity())
                        .setMessage("Apakah Anda yakin ingin menambahkan ?.")
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String get_penyedia = addlist_penyedia.getText().toString().trim();
                                Call<DataResponsePJ> update_penyedia = apiInterface.submitPenyedia(id_paket, get_penyedia);
                                update_penyedia.enqueue(new Callback<DataResponsePJ>() {
                                    @Override
                                    public void onResponse(Call<DataResponsePJ> call, Response<DataResponsePJ> response) {
                                        Log.d(TAG, "Response code : " + response.code());
                                        Log.d(TAG, "Request code : " + call);
                                        if(response.code() == 200){

                                        }
                                        text_judul.setText(addlist_penyedia.getText().toString().trim());
                                        addlist_penyedia.getText().clear();
                                    }

                                    @Override
                                    public void onFailure(Call<DataResponsePJ> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .create()
                        .show();
                }

            }
        });

        return view;
    }
}
