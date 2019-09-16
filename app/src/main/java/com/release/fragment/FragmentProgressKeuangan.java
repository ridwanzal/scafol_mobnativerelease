package com.release.fragment;

import android.app.DatePickerDialog;
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
import com.release.activity.ActivityProgressKeuangan;

public class FragmentProgressKeuangan extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";
    TextView pr_keuangan_detail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_keuangan, container, false);

        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");
        pr_keuangan_detail = view.findViewById(R.id.pr_keuangan_detail);

        pr_keuangan_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), ActivityProgressKeuangan.class);
                intent2.putExtra("pa_id", pa_id);
                intent2.putExtra("pa_nama", pa_judul);
                startActivity(intent2);
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
