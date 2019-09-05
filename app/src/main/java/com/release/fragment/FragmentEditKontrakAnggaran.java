package com.release.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.model.Anggaran;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponsePaket;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DatePickerFragment;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEditKontrakAnggaran extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    Context ctx;
    private static String TAG = "FragmentEditKontrakAnggaran";
    private EditText t_nomorkontrak_ang;
    private TextView t_awalkontrak_ang;
    private TextView t_akhirkontrak_ang;

    private EditText t_nilaikontrak_ang;
    private ImageView btn_date_awal_ang;
    private ImageView btn_date_akhir_ang;
    Button btn_simpan_editkontrak_ang;

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    private String current = "";
    public static int isDateEdit1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editkontrak_anggaran, container, false);
        ctx = getActivity();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        t_nomorkontrak_ang = view.findViewById(R.id.text_nomorkontrak_anggaran);
        t_nilaikontrak_ang = view.findViewById(R.id.text_nilaikontrak_anggaran);

        btn_date_awal_ang = view.findViewById(R.id.btn_date_awal);
        btn_date_akhir_ang = view.findViewById(R.id.btn_date_akhir);
        t_awalkontrak_ang = view.findViewById(R.id.text_awalkontrak_anggaran);
        t_akhirkontrak_ang = view.findViewById(R.id.text_akhirkontrak_anggaran);
        btn_simpan_editkontrak_ang = view.findViewById(R.id.btn_simpan);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");

        t_nomorkontrak_ang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (t_nomorkontrak_ang.getRight() - t_nomorkontrak_ang.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        t_nomorkontrak_ang.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        t_nilaikontrak_ang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (t_nilaikontrak_ang.getRight() - t_nilaikontrak_ang.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        t_nilaikontrak_ang.getText().clear();
                        return true;
                    }
                }
                return false;
            }
        });

        btn_date_awal_ang.setOnClickListener(this);
        btn_date_akhir_ang.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Intent intent = getActivity().getIntent();
        final String id_anggaran = intent.getStringExtra("an_id");
        Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaran(id_anggaran);
        call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
            @Override
            public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                    if(response.code() == 200){
                        ArrayList<Anggaran> ang_list = response.body().getData();
                        for(int i = 0; i < ang_list.size(); i++){
                            String nomor_kontrak = ang_list.get(i).getAnNomorkontrak();
                            String nilai_kontrak = ang_list.get(i).getAnNilaikontrak();
                            String awal_kontrak = ang_list.get(i).getAnAwalkontrak();
                            String akhir_kontrak = ang_list.get(i).getAnAkhirkontrak();

                            t_nomorkontrak_ang.setText(checkData(nomor_kontrak));
                            t_nilaikontrak_ang.setText(formatMoneyIDR.convertIDR(nilai_kontrak));
                            t_awalkontrak_ang.setText(checkData(awal_kontrak));
                            t_akhirkontrak_ang.setText(checkData(akhir_kontrak));
                        }
                    }
            }

            @Override
            public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {

            }
        });

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_date_awal:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(FragmentEditKontrakAnggaran.this, 0);
                isDateEdit1 = 1;
                datePickerFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
            case R.id.btn_date_akhir:
                DatePickerFragment datePickerFragment2 = new DatePickerFragment();
                datePickerFragment2.setTargetFragment(FragmentEditKontrakAnggaran.this, 0);
                isDateEdit1 = 2;
                datePickerFragment2.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
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
            t_awalkontrak_ang.setText(dateFormat.format(calendar.getTime()));
        }else if(isDateEdit1 == 2){
            Log.e(TAG, "EDIT 2");
            t_akhirkontrak_ang.setText(dateFormat.format(calendar.getTime()));

        }
    }
}
