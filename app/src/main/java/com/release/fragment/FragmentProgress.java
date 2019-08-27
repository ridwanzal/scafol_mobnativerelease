package com.release.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
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

import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.activity.ActivityProgressFisik;
import com.release.activity.ActivityProgressKeuangan;
import com.release.model.DataResponseCatatan;
import com.release.model.DataResponsePaket;
import com.release.model.DataResponseProgress;
import com.release.model.Paket;
import com.release.model.Progress;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DateInfo;
import com.release.sharedexternalmodule.DatePickerFragment;
import com.google.gson.Gson;
import com.release.sharedexternalmodule.formatMoneyIDR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProgress extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    TextView pr_fisik_detail;
    TextView pr_keuangan_detail;
    TextView pr_catatan_fisik;
    TextView text_infokontrak;
    private static String TAG = "FragmentProgress";
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    ImageView date_progresfisik;
    ImageView btn_date_prog_keuangan;
    EditText tx_tanggalprogress;
    EditText tx_tanggal_keuangan;
    EditText keu_pagu;
    EditText keu_kontrak;
    EditText keu_serap;
    EditText keu_sisa;
    EditText keu_sisang;
    EditText keu_ket;

    Button btn_submit_progresfisik;
    Button keu_submit;

    Button btn_submit_catatan;

    EditText prog_target_fisik;
    EditText prog_real_fisik;
    EditText prog_deviasi_fisik;
    EditText textcatatans;

    ImageView get_deviasi;
    Handler mHandler;

    public static int isEditFlag;

    public static Boolean check_duplicate = false;


    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";

    String pagu_value;
    String kontrak_value;

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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        pr_keuangan_detail = view.findViewById(R.id.pr_keuangan_detail);
        date_progresfisik = view.findViewById(R.id.date_progresfisik);
        tx_tanggalprogress = view.findViewById(R.id.tx_tanggalprogress);
        btn_submit_progresfisik = view.findViewById(R.id.btn_submit_progresfisik);
        get_deviasi = view.findViewById(R.id.get_deviasi);
        pr_catatan_fisik = view.findViewById(R.id.pr_catatan_fisik);

        prog_target_fisik = view.findViewById(R.id.prog_target_fisik);
        prog_real_fisik = view.findViewById(R.id.prog_real_fisik);
        prog_deviasi_fisik = view.findViewById(R.id.prog_deviasi_fisik);

        loading_progress_submit = view.findViewById(R.id.loading_progress_submit);
        btn_submit_catatan = view.findViewById(R.id.btn_submit_catatan);
        textcatatans = view.findViewById(R.id.textcatatans);

        btn_date_prog_keuangan = view.findViewById(R.id.btn_date_prog_keuangan);
        tx_tanggal_keuangan = view.findViewById(R.id.tx_tanggal_keuangan);

        keu_pagu = view.findViewById(R.id.keu_pagu);
        keu_kontrak = view.findViewById(R.id.keu_kontrak);
        keu_sisa = view.findViewById(R.id.keu_sisa);
        keu_sisang = view.findViewById(R.id.keu_sisang);
        keu_serap = view.findViewById(R.id.keu_serap);
        keu_ket = view.findViewById(R.id.keu_ket);
        keu_submit = view.findViewById(R.id.keu_submit);

        text_infokontrak = view.findViewById(R.id.text_infokontrak);

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");

        /******************************************************************Progres Keuangan Submission********************************************************************************/

        Call<DataResponsePaket> call_paket = apiInterface.getPaketId(pa_id);
        call_paket.enqueue(new Callback<DataResponsePaket>() {
            @Override
            public void onResponse(Call<DataResponsePaket> call, Response<DataResponsePaket> response) {
                if(response.code() == 200){
                    ArrayList<Paket> paketlist = response.body().getData();
                    for(int i = 0; i < paketlist.size(); i++){
                        keu_pagu.setText(paketlist.get(i).getPaPagu());
                        keu_kontrak.setText(paketlist.get(i).getPaNilaiKontrak());
                        if(paketlist.get(i).getPaNilaiKontrak() == null || paketlist.get(i).getPaNilaiKontrak().toString().equals("0")){
                            text_infokontrak.setText("Nomor kontrak belum diisi, harap isi terlebih dahulu di halaman Edit Kontrak");
                            text_infokontrak.setTextColor(Color.parseColor("#ff0000"));
                            keu_kontrak.setEnabled(false);
                            keu_serap.setEnabled(false);
                            keu_sisa.setEnabled(false);
                            keu_sisang.setEnabled(false);
                            tx_tanggal_keuangan.setEnabled(false);
                            keu_ket.setEnabled(false);
                        }
                        pagu_value = paketlist.get(i).getPaPagu();
                        kontrak_value = paketlist.get(i).getPaNilaiKontrak();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponsePaket> call, Throwable t) {

            }
        });


        keu_serap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text_serap = keu_serap.getText().toString().trim();
                String text_pagu = pagu_value.toString().trim();
                String text_kontrak = kontrak_value.toString().trim();
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
                    Long sisa_kontrak = Long.valueOf(text_kontrak) - Long.valueOf(text_serap);
                    keu_sisa.setText(sisa_kontrak.toString());

                    Long sisa_anggaran = Long.valueOf(text_pagu) - Long.valueOf(text_serap);
                    keu_sisang.setText(sisa_anggaran.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text_serap = keu_serap.getText().toString().trim();
                String text_pagu = pagu_value.toString().trim();
                String text_kontrak = kontrak_value.toString().trim();
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
                    Long sisa_kontrak = Long.valueOf(text_kontrak) - Long.valueOf(text_serap);
                    keu_sisa.setText(sisa_kontrak.toString());

                    Long sisa_anggaran = Long.valueOf(text_pagu) - Long.valueOf(text_serap);
                    keu_sisang.setText(sisa_anggaran.toString());
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
                    Toasty.success(getActivity(), "Yey kamu bisa", Toasty.LENGTH_SHORT).show();
                }
            }
        });




        /******************************************************************Catatan Submission********************************************************************************/

        pr_catatan_fisik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityCatatan.class);
                intent.putExtra("pa_id", pa_id);
                intent.putExtra("pa_nama", pa_judul);
                startActivity(intent);
            }
        });

        btn_submit_catatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean catatan_edit = textcatatans.getText().toString().trim().equals("");
                Boolean next = false;

                if(catatan_edit){
                    textcatatans.setError("Required");
                    textcatatans.setHint("Masukkan catatan anda");
                    next = false;
                    return;
                }else{
                    next = true;
                }

                if(next){
                    progressDialog.show();
                    DateInfo dateInfo = new DateInfo();
                    Call<DataResponseCatatan> callprogress = apiInterface.addCatatan(pa_id, textcatatans.getText().toString(), dateInfo.dateTime(), dateInfo.dateTime());

                    callprogress.enqueue(new Callback<DataResponseCatatan>() {
                        @Override
                        public void onResponse(Call<DataResponseCatatan> call, Response<DataResponseCatatan> response) {
                            Log.d(TAG, "RESULT "  + response);
                        }

                        @Override
                        public void onFailure(Call<DataResponseCatatan> call, Throwable t) {
                            Log.e(TAG, "RESULT failed ");
                            textcatatans.setText("");
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    mHandler.sendMessage(Message.obtain(mHandler, 1));
                                }
                            }).start();
                            mHandler = new Handler(Looper.myLooper()){
                                @Override
                                public void handleMessage(@NonNull Message msg) {
                                    super.handleMessage(msg);
                                    switch (msg.what){
                                        case 1 :
                                            Toasty.success(getActivity(), "Catatan berhasil ditambah", Toasty.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            };

                        }
                    });
                }

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
        btn_date_prog_keuangan.setOnClickListener(this);


        /******************************************************************Progress Fisik Submission********************************************************************************/

        pr_fisik_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityProgressFisik.class);
                intent.putExtra("pa_id", pa_id);
                intent.putExtra("pa_nama", pa_judul);
                startActivity(intent);
            }
        });
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
        }else if(isEditFlag == 2){
            Log.e(TAG, "EDIT 2");
            tx_tanggal_keuangan.setText(dateFormat.format(calendar.getTime()));
            tx_tanggal_keuangan.setError(null);
            tx_tanggal_keuangan.setHint("");
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
            case R.id.btn_date_prog_keuangan :
                Log.d(TAG, "CLick Tanggalssss");
                DatePickerFragment datePickerFragment2 = new DatePickerFragment();
                datePickerFragment2.setTargetFragment(FragmentProgress.this, 1);
                isEditFlag = 2;
                datePickerFragment2.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
                datePickerFragment2.show(getActivity().getSupportFragmentManager(), "DatePickerEDitKontrak");
                break;
        }
    }
}
