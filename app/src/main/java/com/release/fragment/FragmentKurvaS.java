package com.release.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.activity.ActivityKurvaSRencana;
import com.release.activity.ActivityProgressFisik;
import com.release.adapter.KurvaSRencanaAdapter;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.DataResponseRencana;
import com.release.model.Paket;
import com.release.model.Progress;
import com.release.model.Rencana;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentKurvaS extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private TextView tvDate;
    private TextView tvDetailDate;
    private TextView tvProgress;
    private TextView daftar_rencana;
    private ImageView btnDate;
    private EditText edtProgress;
    private Button btnSubmit;
    private KurvaSRencanaAdapter kurvaSRencanaAdapter;

    public static Boolean check_duplicate = false;
    public static Boolean check_limit = false;
    public static Boolean check_if_zero_not_available = false;

    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";

    final String DATE_PICKER_TAG = "DatePicker";
    private ProgressBar loading_progress;

    private ProgressDialog progressDialog;
    private Handler mHandler;

    private LinearLayout lin_keu1;
    private LinearLayout labelis;
    private CardView card1x;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        View view = inflater.inflate(R.layout.fragment_kurvas, container, false);
        tvDate = view.findViewById(R.id.tv_date);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvDetailDate = view.findViewById(R.id.tv_detail_date);
        btnDate = view.findViewById(R.id.btn_date);
        edtProgress = view.findViewById(R.id.edt_progress);
        btnSubmit = view.findViewById(R.id.btn_submit);
        daftar_rencana = view.findViewById(R.id.daftar_rencana);
        lin_keu1 = view.findViewById(R.id.lin_keu1);
        labelis = view.findViewById(R.id.labelis);
        card1x = view.findViewById(R.id.card1x);
        lin_keu1.setVisibility(View.GONE);

        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");

        btnDate.setOnClickListener(this);

        tvDetailDate.setText(dateFormat.format(calendar.getTime()));

        daftar_rencana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityKurvaSRencana.class);
                intent.putExtra("pa_id", pa_id);
                intent.putExtra("pa_nama", pa_judul);
                startActivity(intent);
            }
        });

        edtProgress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text_progress = edtProgress.getText().toString();
                if(text_progress.equals("")){
                    text_progress = "0";
                }
                Double get_progress = Double.valueOf(text_progress.trim());
                if(get_progress > 100){
                    edtProgress.setText("");
                    edtProgress.setError("Warning");
                    edtProgress.setHint("Angka target melebihi batas maksimum");
                    edtProgress.requestFocus();
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text_progress = edtProgress.getText().toString();
                if(text_progress.equals("")){
                    text_progress = "0";
                }
                Double get_progress = Double.valueOf(text_progress.trim());
                if(get_progress > 100){
                    edtProgress.setText("");
                    edtProgress.setError("Warning");
                    edtProgress.setHint("Angka target melebihi batas maksimum");
                    edtProgress.requestFocus();
                    return;
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check_progress = edtProgress.getText().toString().trim().equals("");
                Boolean next = false;
                if(check_progress){
                    edtProgress.setError("Required");
                    edtProgress.setHint("Masukkan rencana target");
                    next = false;
                    return;
                }else{
                    try{
                        Double check_maxlimit = Double.valueOf(edtProgress.getText().toString());
                        if(check_maxlimit > 100){
                            edtProgress.setText("");
                            edtProgress.setError("Required");
                            edtProgress.setHint("Target tidak dapat lebih dari 100");
                            next = false;
                            return;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                next = true;
                if(next){
                    final String get_progres = edtProgress.getText().toString();
                    final String get_tanggal = tvDetailDate.getText().toString();
                    Call<DataResponseRencana> checkrencana = apiInterface.getKurvaS(pa_id);
                    checkrencana.enqueue(new Callback<DataResponseRencana>() {
                        @Override
                        public void onResponse(Call<DataResponseRencana> call, Response<DataResponseRencana> response) {
                            if(response.code() == 200){
                                ArrayList<Rencana> loops = response.body().getData();
                                for(int i = 0; i < loops.size(); i++){
                                    if(loops.get(i).getReProgress().toString().equals(edtProgress.getText().toString())){
                                        check_duplicate = true;
                                    }
                                }
                            }

                            if(check_duplicate == false){
//                              Toasty.success(getActivity(), "Progres " + get_progres + " | " + get_tanggal, Toasty.LENGTH_LONG).show();
                                Call<DataResponseRencana> submitrencana = apiInterface.addNewRencana(pa_id, get_tanggal, get_progres);
                                submitrencana.enqueue(new Callback<DataResponseRencana>() {
                                    @Override
                                    public void onResponse(Call<DataResponseRencana> call, Response<DataResponseRencana> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<DataResponseRencana> call, Throwable t) {
                                        edtProgress.setText("");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(200);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                mHandler.sendMessage(Message.obtain(mHandler, 1));
                                            }
                                        }).start();
                                    }
                                });
                            }else{
                                edtProgress.setText("");
                                edtProgress.setError("Problem");
                                edtProgress.setHint("Target progress sudah terdaftar");
                                check_duplicate = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseRencana> call, Throwable t) {

                        }
                    });


                }
            }
        });

        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                    progressDialog.dismiss();
                    Toasty.success(getActivity(), "Target berhasil ditambah", Toasty.LENGTH_LONG).show();
                    break;
                }
            }
        };

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_date:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(FragmentKurvaS.this, 0);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
                break;
            case R.id.btn_submit:
                break;
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tvDetailDate.setText(dateFormat.format(calendar.getTime()));
    }
}
