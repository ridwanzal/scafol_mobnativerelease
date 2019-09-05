package com.release.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.activity.ActivityProgressSerapan;
import com.release.activity.ActivityUpdateDataAnggaran;

import es.dmoral.toasty.Toasty;

public class FragmentProgressAnggaran extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    Context ctx;
    TextView pr_serapan_anggaran;

    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_anggaran, container, false);
        ctx = getActivity();

        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");

        pr_serapan_anggaran = view.findViewById(R.id.pr_serapan_anggaran);
        pr_serapan_anggaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.success(ctx, "Contoh :  ", Toasty.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ActivityProgressSerapan.class);
                intent.putExtra("pa_id", pa_id);
                intent.putExtra("pa_nama", pa_judul);
                startActivity(intent);
            }
        });
        return view;
    }
}
