package com.release.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.gson.Gson;
import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.activity.ActivityDetailAnggaran;
import com.release.activity.ActivityProgressSerapan;
import com.release.activity.ActivityUpdateDataAnggaran;
import com.release.model.Anggaran;
import com.release.model.DataResponseAnggaran;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.DataResponseSerapan;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DatePickerFragment;
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

public class FragmentProgressAnggaran extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    Context ctx;
    TextView pr_serapan_anggaran;
    ImageView btn_date_anggaran1;
    EditText tx_tanggal_proganggaran;
    CurrencyEditText keu_serap;
    CurrencyEditText keu_pagu;
    CurrencyEditText keu_sisang;
    EditText keu_ket;
    Button keu_submit;

    String pagu_value;
    String serap;

    private ProgressBar loading_progress_submit;
    private static String ke_id = "";
    private static String an_id = "";
    private static String an_nama = "";
    private static String anp_pagu = "";
    public static int isEditFlag;
    private static String TAG = "FragmentProgressAnggaran";
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_anggaran, container, false);
        ctx = getActivity();

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Intent intent = getActivity().getIntent();
        an_id = intent.getStringExtra("an_id");
        an_nama = intent.getStringExtra("an_nama");
        anp_pagu = intent.getStringExtra("anp_pagu");
        ke_id = intent.getStringExtra("ke_id");

        loading_progress_submit = view.findViewById(R.id.loading_progress_submit);
        pr_serapan_anggaran = view.findViewById(R.id.pr_serapan_anggaran);
        btn_date_anggaran1 = view.findViewById(R.id.btn_date_anggaran1);
        keu_pagu = view.findViewById(R.id.keu_pagu);
        keu_serap = view.findViewById(R.id.keu_serap);
        keu_sisang = view.findViewById(R.id.keu_sisang);
        keu_submit = view.findViewById(R.id.keu_submit);
        keu_ket = view.findViewById(R.id.keu_ket);
        tx_tanggal_proganggaran = view.findViewById(R.id.tx_tanggal_proganggaran);
        pr_serapan_anggaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityProgressSerapan.class);
                intent.putExtra("an_id", an_id);
                intent.putExtra("an_nama", an_nama);
                startActivity(intent);
            }
        });

        tx_tanggal_proganggaran.setText(dateFormat.format(calendar.getTime()));

        Call<DataResponseAnggaran> call_anggaran = apiInterface.getAnggaran(an_id);
        call_anggaran.enqueue(new Callback<DataResponseAnggaran>() {
            @Override
            public void onResponse(Call<DataResponseAnggaran> call, Response<DataResponseAnggaran> response) {
                if(response.code() == 200){
                    ArrayList<Anggaran> anggaranlist = response.body().getData();
                    for(int i = 0; i < anggaranlist.size(); i++){
                        keu_pagu.setText(formatMoneyIDR.convertIDR(anggaranlist.get(i).getAnpPagu()));
                        pagu_value = anggaranlist.get(i).getAnpPagu();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponseAnggaran> call, Throwable t) {

            }
        });

        Call<DataResponseSerapan> callserapan = apiInterface.getSerapanSum(an_id);
        callserapan.enqueue(new Callback<DataResponseSerapan>() {
            @Override
            public void onResponse(Call<DataResponseSerapan> call, Response<DataResponseSerapan> response) {
                if(response.code() == 200){
                    Log.d(TAG, "Reponse : " + new Gson().toJson(response));
                    Log.d(TAG, "Data Terkait: " + response.body().getData());
                    for(int i = 0; i < response.body().getData().size(); i++){
                        serap = response.body().getData().get(i).getJumlah();
                        if(serap == null){
                            serap = "0";
                        }
                    }
                    Log.d(TAG, "Serapan :" + serap);
                }
            }

            @Override
            public void onFailure(Call<DataResponseSerapan> call, Throwable t) {
            }
        });

        keu_serap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
//                    String text_serap = String.valueOf(keu_serap.getCurrencyDouble()).split(",")[0];
                    String text_serap = formatMoneyIDR.reverseIDR(keu_serap.getCurrencyDouble());
                    String text_pagu = pagu_value.toString().trim();
                    if(text_pagu.equals("")){
                        text_pagu = "0";
                    }

                    if(text_serap.equals("") || charSequence.equals("")){
                        text_serap = "0";
                        keu_sisang.setText("");
                    }else{
                        Double sisa_anggaran = Double.valueOf(text_pagu) - (Double.valueOf(text_serap) + Double.valueOf(serap));
                        keu_sisang.setText(formatMoneyIDR.convertIDR(sisa_anggaran.toString()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
//                    String text_serap = String.valueOf(keu_serap.getCurrencyDouble()).split(",")[0];
                    String text_serap = formatMoneyIDR.reverseIDR(keu_serap.getCurrencyDouble());
                    String text_pagu = pagu_value.toString().trim();
                    if(text_pagu.equals("")){
                        text_pagu = "0";
                    }

                    if(text_serap.equals("") || editable.equals("")){
                        text_serap = "0";
                        keu_sisang.setText("");
                    }else{
                        Double sisa_anggaran = Double.valueOf(text_pagu) - (Double.valueOf(text_serap) + Double.valueOf(serap));
                        keu_sisang.setText(formatMoneyIDR.convertIDR(sisa_anggaran.toString()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        keu_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check_isi = keu_serap.getText().toString().trim().equals("");
                Boolean check_tanggal = tx_tanggal_proganggaran.getText().toString().equals("");

                Boolean next = false;
                if(check_isi){
                    keu_serap.setError("Required");
                    keu_serap.setHint("Masukkan data serapan");
                    next = false;
                    return;
                }

                if(check_tanggal){
                    tx_tanggal_proganggaran.setError("Required");
                    tx_tanggal_proganggaran.setHint("Masukkan data serapan");
                    next = false;
                    return;
                }

                next = true;
                String s = "";
                String ss = "";
                if(next){
                    try{
                         s = String.valueOf(keu_serap.getCurrencyDouble());
                         ss =  String.valueOf(keu_sisang.getCurrencyDouble());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    final String k = keu_ket.getText().toString();
                    final String dt = tx_tanggal_proganggaran.getText().toString();
                    Call<DataResponseSerapan> call_addnewserap_keu = apiInterface.addSerapan(an_id, s, ss, dt, k, ke_id);
                    call_addnewserap_keu.enqueue(new Callback<DataResponseSerapan>() {
                        @Override
                        public void onResponse(Call<DataResponseSerapan> call, Response<DataResponseSerapan> response) {
                            Log.d(TAG,"RESULT=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + new Gson().toJson(response));
                            try{
                                Log.d(TAG, "Result SUCCESS" + new Gson().toJson(response));
                                loading_progress_submit.setVisibility(View.GONE);
                                keu_submit.setEnabled(true);
                            }catch (Exception e){
                                Log.d(TAG, e.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponseSerapan> call, Throwable t) {
                            Log.d(TAG, "throwable ->>>>>>>>>>>>>>>>>>>>>> " + t);
                            Toast.makeText(getActivity(), "Ditambahkan", Toast.LENGTH_SHORT).show();
                            loading_progress_submit.setVisibility(View.GONE);
                            keu_submit.setEnabled(true);
                            Intent intent4 = new Intent(getActivity(), ActivityUpdateDataAnggaran.class);
                            intent4.putExtra("anp_pagu", anp_pagu);
                            intent4.putExtra("an_id", an_id);
                            intent4.putExtra("position", 1);
                            intent4.putExtra("an_nama", an_nama);
                            intent4.putExtra("ke_id", ke_id);
                            startActivity(intent4);
                        }
                    });
                }
            }
        });

        btn_date_anggaran1.setOnClickListener(this);
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
            tx_tanggal_proganggaran.setText(dateFormat.format(calendar.getTime()));
            tx_tanggal_proganggaran.setError(null);
            tx_tanggal_proganggaran.setHint("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_date_anggaran1 :
                final String examp = "sdsdsdsd";
                Log.d(TAG, "CLick Tanggalssss");
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(FragmentProgressAnggaran.this, 0);
                isEditFlag = 1;
                datePickerFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
        }
    }


}
