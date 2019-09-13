package com.release.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.activity.ActivityProgressSerapan;
import com.release.activity.ActivityUpdateDataAnggaran;
import com.release.sharedexternalmodule.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class FragmentProgressAnggaran extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    Context ctx;
    TextView pr_serapan_anggaran;
    ImageView btn_date_anggaran1;
    EditText tx_tanggal_proganggaran;

    private static String ke_id = "";
    private static String an_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";
    public static int isEditFlag;
    private static String TAG = "FragmentProgress";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_anggaran, container, false);
        ctx = getActivity();

        Intent intent = getActivity().getIntent();
        an_id = intent.getStringExtra("an_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");

        pr_serapan_anggaran = view.findViewById(R.id.pr_serapan_anggaran);
        btn_date_anggaran1 = view.findViewById(R.id.btn_date_anggaran1);
        tx_tanggal_proganggaran = view.findViewById(R.id.tx_tanggal_proganggaran);
        pr_serapan_anggaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityProgressSerapan.class);
                intent.putExtra("an_id", an_id);
                intent.putExtra("pa_nama", pa_judul);
                startActivity(intent);
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
