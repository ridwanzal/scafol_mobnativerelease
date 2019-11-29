package com.release.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.icu.util.Currency;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.model.DataResponsePaket;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DatePickerFragment;
import com.google.gson.Gson;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import faranjit.currency.edittext.CurrencyEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEditKontrak extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "FragmentEditKontrak";
    private EditText t_nomorkontrak;
    private TextView t_awalkontrak;
    private TextView t_akhirkontrak;

    private CurrencyEditText t_nilaikontrak;
    private CurrencyEditText t_nilaipagu;
    private ImageView btn_date_awal;
    private ImageView btn_date_akhir;
    Button btn_simpan;

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    private String current = "";
    Context ctx;

    public static int isDateEdit1;
    Handler mHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editkontrak, container, false);
        ctx = getActivity();
        t_nomorkontrak = view.findViewById(R.id.text_nomorkontrak);
        t_nilaikontrak = (CurrencyEditText) view.findViewById(R.id.text_nilaikontrak);
        t_nilaipagu = view.findViewById(R.id.text_nilaipagu);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");

        t_nomorkontrak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (t_nomorkontrak.getRight() - t_nomorkontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        t_nomorkontrak.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        t_nilaikontrak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (t_nilaikontrak.getRight() - t_nilaikontrak.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        t_nilaikontrak.getText().clear();
                        return true;
                    }
                }

                return false;
            }
        });

        t_awalkontrak = view.findViewById(R.id.text_awalkontrak);
        t_akhirkontrak = view.findViewById(R.id.text_akhirkontrak);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        btn_date_awal = view.findViewById(R.id.btn_date_awal);
        btn_date_akhir = view.findViewById(R.id.btn_date_akhir);

        btn_simpan = view.findViewById(R.id.btn_simpan);
        Intent intent = getActivity().getIntent();
        final String id_paket = intent.getStringExtra("pa_id");
        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(id_paket);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                Log.w(TAG, "Paket data" + new Gson().toJson(response.body().getData()));
                if(response.code() == 200){
                    ArrayList<Paket> paketlist = response.body().getData();
                    for(int i = 0; i < paketlist.size(); i++){
                        String nomor_kontrak = paketlist.get(i).getPaNomorKontrak();
                        String nilai_kontrak = paketlist.get(i).getPaNilaiKontrak();
                        String awal_kontrak = paketlist.get(i).getPaAwalKontrak();
                        String akhir_kontrak = paketlist.get(i).getPaAkhirKontrak();
                        String pagu = paketlist.get(i).getPaPagu();
                        t_nilaipagu.setText(formatMoneyIDR.convertIDR(pagu));
                        t_nomorkontrak.setText(checkData(nomor_kontrak));
                        t_nilaikontrak.setText(formatMoneyIDR.convertIDR(nilai_kontrak));
                        t_awalkontrak.setText(checkData(awal_kontrak));
                        t_akhirkontrak.setText(checkData(akhir_kontrak));
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        t_nilaikontrak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String text_pagu = String.valueOf(t_nilaipagu.getCurrencyDouble());
                    String text_kontrak = String.valueOf(t_nilaikontrak.getCurrencyDouble());
                    Double get_pagu = Double.valueOf(text_pagu);
                    Double get_kontrak = Double.valueOf(text_kontrak);
                    if(get_kontrak > get_pagu){
                        t_nilaikontrak.setError("Melebihi pagu");
                        t_nilaikontrak.requestFocus();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    String text_pagu = String.valueOf(t_nilaipagu.getCurrencyDouble());
                    String text_kontrak = String.valueOf(t_nilaikontrak.getCurrencyDouble());
                    Double get_pagu = Double.valueOf(text_pagu);
                    Double get_kontrak = Double.valueOf(text_kontrak);
                    Log.d(TAG, "Pagu : " + text_pagu);
                    Log.d(TAG, "Kontrak : " + text_kontrak);
                    if(get_kontrak > get_pagu){
                        t_nilaikontrak.setError("Melebihi pagu");
                        t_nilaikontrak.requestFocus();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // submit data to update contract.
                progressDialog.show();
                String get_nomorkontrak = t_nomorkontrak.getText().toString();
//                String get_nilai_kontrak = t_nilaikontrak.getText().toString().replaceAll("[.]","");
                String get_nilai_kontrak = "";
                try{
                    get_nilai_kontrak = String.valueOf(t_nilaikontrak.getCurrencyDouble()).split(",")[0];
                }catch (Exception e){
                    e.printStackTrace();
                }
                String get_awalkontrak = t_awalkontrak.getText().toString();
                String get_akhirkontrak = t_akhirkontrak.getText().toString();
                String pa_id = id_paket;
                Log.d(TAG, "No Kontrak : " + get_nomorkontrak + " Nilai Kontrak : " + get_nilai_kontrak + " | " + id_paket);
                Toasty.success(ctx, "Jumlah nominal : " + get_nilai_kontrak, Toast.LENGTH_SHORT).show();
                Toasty.success(ctx, "Nilai kontrak  + " + get_nilai_kontrak, Toast.LENGTH_SHORT).show();
                Call<DataResponsePaket> call_update = apiInterface.updateKontrak(pa_id, get_nomorkontrak, get_nilai_kontrak, get_awalkontrak, get_akhirkontrak);
                call_update.enqueue(new Callback<DataResponsePaket>() {
                    @Override
                    public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                        if(response.code() == 200){
                            Toasty.success(ctx, "Data berhasil di update", Toast.LENGTH_SHORT).show();
                        }else{
                            Toasty.warning(ctx, "Tidak ada data yang diubah", Toast.LENGTH_SHORT).show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        Thread.sleep(100);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }).start();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponsePaket> call, Throwable t) {
//                        Toast.makeText(ctx, "Berhasil Edit Kontrak", Toast.LENGTH_SHORT).show();
                        Toasty.success(ctx, "Data berhasil di update", Toast.LENGTH_SHORT).show();
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

                    }
                });
            }
        });
        Log.d(TAG, "GET ID PAKET " + id_paket);
        btn_date_awal.setOnClickListener(this);
        btn_date_akhir.setOnClickListener(this);

        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        Toasty.success(getActivity(), "Kontrak berhasil diubah", Toasty.LENGTH_LONG).show();
                        btn_simpan.setVisibility(View.GONE);
                        break;
                }
            }
        };

        return view;
    }

    public static String checkData(String data){
        if(data == null || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.e(TAG, "Date Result edit paket "  + String.valueOf(datePicker.getId()));
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if(isDateEdit1 == 1){
            Log.e(TAG, "EDIT 1");
            t_awalkontrak.setText(dateFormat.format(calendar.getTime()));
        }else if(isDateEdit1 == 2){
            Log.e(TAG, "EDIT 2");
            t_akhirkontrak.setText(dateFormat.format(calendar.getTime()));

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_date_awal:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(FragmentEditKontrak.this, 0);
                isDateEdit1 = 1;
                datePickerFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
            case R.id.btn_date_akhir:
                DatePickerFragment datePickerFragment2 = new DatePickerFragment();
                datePickerFragment2.setTargetFragment(FragmentEditKontrak.this, 0);
                isDateEdit1 = 2;
                datePickerFragment2.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
        }
    }
}
