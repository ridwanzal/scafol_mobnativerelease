package com.release.fragment;

import android.app.DatePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.activity.ActivityProgressKeuangan;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.Paket;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DatePickerFragment;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import faranjit.currency.edittext.CurrencyEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProgressKeuangan extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private static String TAG = "FragmentProgressKeuangan";
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";
    public static int isEditFlag;
    TextView pr_keuangan_detail;
    ImageView btn_date_prog_keuangan;
    EditText tx_tanggal_keuangan;
    CurrencyEditText keu_pagu;
    CurrencyEditText keu_kontrak;
    CurrencyEditText keu_serap;
    CurrencyEditText keu_sisa;
    CurrencyEditText keu_sisang;
    EditText keu_ket;
    Button keu_submit;
    TextView text_infokontrak;
    LinearLayout lin_keu2;

    String pagu_value;
    String kontrak_value;
    String serap;

    private ProgressBar loading_progress_submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_keuangan, container, false);

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");
        pr_keuangan_detail = view.findViewById(R.id.pr_keuangan_detail);
        btn_date_prog_keuangan = view.findViewById(R.id.btn_date_prog_keuangan);
        tx_tanggal_keuangan = view.findViewById(R.id.tx_tanggal_keuangan);

        loading_progress_submit = view.findViewById(R.id.loading_progress_submit);
        keu_pagu = (CurrencyEditText)  view.findViewById(R.id.keu_pagu);
        keu_kontrak = (CurrencyEditText)  view.findViewById(R.id.keu_kontrak);
        keu_sisa = (CurrencyEditText)  view.findViewById(R.id.keu_sisa);
        keu_sisang = (CurrencyEditText)  view.findViewById(R.id.keu_sisang);
        keu_serap = (CurrencyEditText) view.findViewById(R.id.keu_serap);
        keu_ket = view.findViewById(R.id.keu_ket);
        keu_submit = view.findViewById(R.id.keu_submit);

        text_infokontrak = view.findViewById(R.id.text_infokontrak);

        lin_keu2 = view.findViewById(R.id.lin_keu2);

        pr_keuangan_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), ActivityProgressKeuangan.class);
                intent2.putExtra("pa_id", pa_id);
                intent2.putExtra("pa_nama", pa_judul);
                startActivity(intent2);
            }
        });

        btn_date_prog_keuangan.setOnClickListener(this);

        tx_tanggal_keuangan.setText(dateFormat.format(calendar.getTime()));

        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(pa_id);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                if(response.code() == 200){
                    ArrayList<Paket> paketlist = response.body().getData();
                    for(int i = 0; i < paketlist.size(); i++){
                        keu_pagu.setText(formatMoneyIDR.convertIDR(paketlist.get(i).getPaPagu()));
                        keu_kontrak.setText(formatMoneyIDR.convertIDR(paketlist.get(i).getPaNilaiKontrak().trim()));
                        pagu_value = paketlist.get(i).getPaPagu().trim();
                        kontrak_value = paketlist.get(i).getPaNilaiKontrak().trim();
                        if(paketlist.get(i).getPaNilaiKontrak().toString().equals("0")){
                            text_infokontrak.setText("Nomor kontrak belum diisi, harap isi terlebih dahulu di halaman Edit Kontrak");
                            text_infokontrak.setTextColor(Color.parseColor("#ff0000"));
                            lin_keu2.setVisibility(View.GONE);
                            keu_kontrak.setEnabled(false);
                            keu_serap.setEnabled(false);
                            keu_sisa.setEnabled(false);
                            keu_sisang.setEnabled(false);
                            tx_tanggal_keuangan.setEnabled(false);
                            keu_ket.setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {

            }
        });

        Call<DataResponseProgress> callprogress = apiInterface.getProgressByPaketKeuanganSerap(pa_id);
        callprogress.enqueue(new Callback<DataResponseProgress>() {
            @Override
            public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
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
            public void onFailure(Call<DataResponseProgress> call, Throwable t) {
            }
        });

        keu_serap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{

                    String text_serap = String.valueOf(keu_serap.getCurrencyDouble()).split(",")[0];
                    String text_pagu = pagu_value.toString();
                    String text_kontrak = kontrak_value.toString();

                    if(text_pagu.equals("")){
                        text_pagu = "0";
                    }
                    if(text_kontrak.equals("")){
                        text_kontrak = "0";
                    }

                    if(text_serap.equals("") || charSequence.equals("")){
                        text_serap = "0";
                        keu_sisang.setText("");
                        keu_sisa.setText("");
                    }else{
                    Double sisa_kontrak = Double.valueOf(text_kontrak) - (Double.valueOf(text_serap) + Double.valueOf(serap));
                    keu_sisa.setText(formatMoneyIDR.convertIDR(sisa_kontrak.toString()));

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
                    String text_serap = String.valueOf(keu_serap.getCurrencyDouble()).split(",")[0];
                    String text_pagu = pagu_value.toString();
                    String text_kontrak = kontrak_value.toString();

                    if(text_pagu.equals("")){
                        text_pagu = "0";
                    }
                    if(text_kontrak.equals("")){
                        text_kontrak = "0";
                    }

                    if(text_serap.equals("") || editable.equals("")){
                        text_serap = "0";
                        keu_sisang.setText("");
                        keu_sisa.setText("");
                    }else{
                    Double sisa_kontrak = Double.valueOf(text_kontrak) - (Double.valueOf(text_serap) + Double.valueOf(serap));
                    keu_sisa.setText(formatMoneyIDR.convertIDR(sisa_kontrak.toString()));

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
                Boolean check_tanggal = tx_tanggal_keuangan.getText().toString().equals("");

                Boolean next = false;
                if(check_isi){
                    keu_serap.setError("Required");
                    keu_serap.setHint("Masukkan data serapan");
                    next = false;
                    return;
                }

                if(check_tanggal){
                    tx_tanggal_keuangan.setError("Required");
                    tx_tanggal_keuangan.setHint("Masukkan data serapan");
                    next = false;
                    return;
                }

                next = true;
                if(next){
                    final String s = keu_serap.getText().toString();
                    final String k = keu_ket.getText().toString();
                    final String dt = tx_tanggal_keuangan.getText().toString();
                    Call<DataResponseProgress> call_addnewprog_keu = apiInterface.addNewProgressKeuangan(pa_id, s, k, dt, ke_id);
                    call_addnewprog_keu.enqueue(new Callback<DataResponseProgress>() {
                        @Override
                        public void onResponse(Call<DataResponseProgress> call, Response<DataResponseProgress> response) {
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
                        public void onFailure(Call<DataResponseProgress> call, Throwable t) {
                            Log.d(TAG, "throwable ->>>>>>>>>>>>>>>>>>>>>> " + t);
                            Toast.makeText(getActivity(), "Ditambahkan", Toast.LENGTH_SHORT).show();
                            loading_progress_submit.setVisibility(View.GONE);
                            keu_submit.setEnabled(true);
                            keu_serap.setText("");
                            tx_tanggal_keuangan.setText("");
                            keu_ket.setText("");
                        }
                    });
                }
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_date_prog_keuangan :
                Log.d(TAG, "CLick Tanggalssss");
                DatePickerFragment datePickerFragment2 = new DatePickerFragment();
                datePickerFragment2.setTargetFragment(FragmentProgressKeuangan.this, 1);
                isEditFlag = 2;
                datePickerFragment2.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
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
        if(isEditFlag == 2){
            Log.e(TAG, "EDIT 2");
            tx_tanggal_keuangan.setText(dateFormat.format(calendar.getTime()));
            tx_tanggal_keuangan.setError(null);
            tx_tanggal_keuangan.setHint("");
        }
    }
}
