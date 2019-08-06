package com.release.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.sharedexternalmodule.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentKurvaS extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private TextView tvDate;
    private TextView tvDetailDate;
    private TextView tvProgress;
    private ImageView btnDate;
    private EditText edtProgress;
    private Button btnSubmit;

    final String DATE_PICKER_TAG = "DatePicker";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        View view = inflater.inflate(R.layout.fragment_kurva_s, container, false);
        tvDate = view.findViewById(R.id.tv_date);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvDetailDate = view.findViewById(R.id.tv_detail_date);
        btnDate = view.findViewById(R.id.btn_date);
        edtProgress = view.findViewById(R.id.edt_progress);
        btnSubmit = view.findViewById(R.id.btn_submit);

        btnDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        tvDetailDate.setText(dateFormat.format(calendar.getTime()));

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
