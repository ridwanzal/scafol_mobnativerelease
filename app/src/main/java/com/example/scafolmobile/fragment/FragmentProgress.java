package com.example.scafolmobile.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.scafolmobile.R;
import com.example.scafolmobile.activity.ActivityProgressFisik;
import com.example.scafolmobile.activity.ActivityProgressKeuangan;
import com.example.scafolmobile.adapter.ProgressAdapter;
import com.example.scafolmobile.model.DataResponsePA;
import com.example.scafolmobile.model.DataResponseProgress;
import com.example.scafolmobile.model.Progress;
import com.example.scafolmobile.restapi.ApiClient;
import com.example.scafolmobile.restapi.ApiInterface;
import com.example.scafolmobile.sharedexternalmodule.DatePickerFragment;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProgress extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    TextView pr_fisik_detail;
    TextView pr_keuangan_detail;
    private static String TAG = "FragmentProgress";
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    ImageView date_progresfisik;
    EditText tx_tanggalprogress;

    Button btn_submit_progresfisik;

    EditText prog_target_fisik;
    EditText prog_real_fisik;
    EditText prog_deviasi_fisik;

    ImageView get_deviasi;

    public static int isEditFlag;

    public static Boolean check_duplicate = false;


    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";

    private ProgressBar loading_progress_submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        pr_fisik_detail = view.findViewById(R.id.pr_fisik_detail);
        pr_keuangan_detail = view.findViewById(R.id.pr_keuangan_detail);
        date_progresfisik = view.findViewById(R.id.date_progresfisik);
        tx_tanggalprogress = view.findViewById(R.id.tx_tanggalprogress);
        btn_submit_progresfisik = view.findViewById(R.id.btn_submit_progresfisik);
        get_deviasi = view.findViewById(R.id.get_deviasi);

        prog_target_fisik = view.findViewById(R.id.prog_target_fisik);
        prog_real_fisik = view.findViewById(R.id.prog_real_fisik);
        prog_deviasi_fisik = view.findViewById(R.id.prog_deviasi_fisik);

        loading_progress_submit = view.findViewById(R.id.loading_progress_submit);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");

        ke_id = intent.getStringExtra("ke_id");
        pr_fisik_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityProgressFisik.class);
                intent.putExtra("pa_id", pa_id);
                intent.putExtra("pa_nama", pa_judul);
                startActivity(intent);
            }
        });

        pr_keuangan_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), ActivityProgressKeuangan.class);
                intent2.putExtra("pa_id", pa_id);
                intent2.putExtra("pa_nama", pa_judul);
                startActivity(intent2);
            }
        });

        date_progresfisik.setOnClickListener(this);

        prog_real_fisik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String t = prog_target_fisik.getText().toString().trim();
                String r = prog_real_fisik.getText().toString().trim();
                if(r.equals("") || t.equals("")){
                    t = "0";
                    r = "0";
                }
                Integer deviasi = Integer.valueOf(r) - Integer.valueOf(t);
                prog_deviasi_fisik.setText(deviasi.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String t = prog_target_fisik.getText().toString().trim();
                String r = prog_real_fisik.getText().toString().trim();
                if(r.equals("")|| t.equals("")){
                    t = "0";
                    r = "0";
                }
                Integer deviasi = Integer.valueOf(r) - Integer.valueOf(t);
                prog_deviasi_fisik.setText(deviasi.toString());
            }
        });

        get_deviasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = prog_target_fisik.getText().toString().trim();
                String r = prog_real_fisik.getText().toString().trim();
                if(r.equals("")|| t.equals("")){
                    t = "0";
                    r = "0";
                }
                Integer deviasi = Integer.valueOf(r) - Integer.valueOf(t);
                prog_deviasi_fisik.setText(deviasi.toString());
            }
        });

        btn_submit_progresfisik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check_target_fisik = prog_target_fisik.getText().toString().trim().equals("");
                Boolean check_real_fisik = prog_real_fisik.getText().toString().trim().equals("");
                Boolean check_tanggal = tx_tanggalprogress.getText().toString().trim().equals("");
                Boolean check_deviasi = prog_deviasi_fisik.getText().toString().trim().equals("");
                Boolean check_tanggal_null = tx_tanggalprogress.getText().toString().trim().isEmpty();
                Boolean next = false;
//                Toast.makeText(getActivity(), "KE ID : " + ke_id, Toast.LENGTH_SHORT).show();
                if(check_target_fisik){
                    prog_target_fisik.setError("Required");
                    prog_target_fisik.setHint("Masukkan persentase target");
                    next = false;
                    return;
                }

                if(check_real_fisik){
                    prog_real_fisik.setError("Required");
                    prog_real_fisik.setHint("Masukkan persentase realisasi");
                    next = false;
                    return;
                }

                if(check_deviasi){
                    prog_deviasi_fisik.setError("Required");
                    prog_deviasi_fisik.setHint("Masukkan persentase target dan realisasi");
                    next = false;
                    return;
                }


                if(check_tanggal || check_tanggal_null == null){
                    tx_tanggalprogress.setError("Required");
                    tx_tanggalprogress.setHint("Input Date YYYY-MM-dd");
                    next = false;
                    return;
                }

                next = true;
                if(next){
                    loading_progress_submit.setVisibility(View.VISIBLE);
                    btn_submit_progresfisik.setEnabled(false);
                    final String t = prog_target_fisik.getText().toString().trim();
                    final String r = prog_real_fisik.getText().toString().trim();
                    final String d = prog_deviasi_fisik.getText().toString();
                    final String date = tx_tanggalprogress.getText().toString();
                    final String ke_id = FragmentProgress.ke_id;
                    final String pa_id = FragmentProgress.pa_id;

                    Call<DataResponseProgress> callprogress = apiInterface.getProgressByPaket(pa_id);
                    callprogress.enqueue(new Callback<DataResponseProgress>() {
                        @Override
                        public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                            try {
                                Log.d(TAG, "-===========-> " + new Gson().toJson(response));
                                    if(response.code() == 404){
                                        check_duplicate = false;
                                    }else{
                                        ArrayList<Progress> loops = response.body().getData();
                                        for(int i = 0; i < loops.size(); i++){
                                            if(loops.get(i).getPr_target().toString().equals(prog_target_fisik.getText().toString().trim())){
                                                check_duplicate = true;
                                                Log.d(TAG, "Ada yang sama : " + check_duplicate);
                                            }
                                        }
                                    }

                                Log.d(TAG, "Check duplicate " + check_duplicate);
                                if(check_duplicate == false){
                                        Call<DataResponseProgress> call_addnewprog = apiInterface.addNewProgress(pa_id, t, r, d, date, ke_id);
                                        call_addnewprog.enqueue(new Callback<DataResponseProgress>() {
                                            @Override
                                            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
                                                Log.d(TAG,"RESULT=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + new Gson().toJson(response));
                                                try{
                                                    Log.d(TAG, "Result SUCCESS" + new Gson().toJson(response));
                                                    loading_progress_submit.setVisibility(View.GONE);
                                                    btn_submit_progresfisik.setEnabled(true);
                                                }catch (Exception e){
                                                    Log.d(TAG, e.toString());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DataResponseProgress> call, Throwable t) {
                                                Log.d(TAG, "throwable ->>>>>>>>>>>>>>>>>>>>>> " + t);
                                                Toast.makeText(getActivity(), "Ditambahkan", Toast.LENGTH_SHORT).show();
                                                loading_progress_submit.setVisibility(View.GONE);
                                                btn_submit_progresfisik.setEnabled(true);
                                                prog_target_fisik.setText("");
                                                prog_real_fisik.setText("");
                                                prog_deviasi_fisik.setText("");
                                                tx_tanggalprogress.setText("");
                                                prog_target_fisik.setHint("");
                                                prog_real_fisik.setText("");
                                                prog_deviasi_fisik.setText("");
                                            }
                                        });
                                }else{
                                    loading_progress_submit.setVisibility(View.GONE);
                                    prog_target_fisik.setText("");
                                    prog_target_fisik.setError("Problem");
                                    prog_target_fisik.setHint("Target progress sudah terdaftar");
                                    prog_real_fisik.setText("");
                                    prog_real_fisik.setError("Problem");
                                    prog_real_fisik.setHint("Masukkan persentase realisasi");
                                    prog_deviasi_fisik.setText("");
                                    btn_submit_progresfisik.setEnabled(true);
                                }
                            }catch (Exception e){
                                Log.e(TAG, e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseProgress> call, Throwable t) {
                        }
                    });
                }

            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.e(TAG, "Date Result edit paket "  + String.valueOf(datePicker.getId()));
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if(isEditFlag == 1){
            Log.e(TAG, "EDIT 1");
            tx_tanggalprogress.setText(dateFormat.format(calendar.getTime()));
            tx_tanggalprogress.setError(null);
            tx_tanggalprogress.setHint("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_progresfisik :
                final String examp = "sdsdsdsd";
                Log.d(TAG, "CLick Tanggalssss");
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(FragmentProgress.this, 0);
                isEditFlag = 1;
                datePickerFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
        }
    }
}
