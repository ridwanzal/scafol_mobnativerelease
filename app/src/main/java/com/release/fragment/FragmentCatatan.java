package com.release.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.release.R;
import com.release.activity.ActivityCatatan;
import com.release.model.DataResponseCatatan;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.DateInfo;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCatatan extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private static String TAG = "FragmentCatatan";
    TextView pr_catatan_fisik;
    private static String ke_id = "";
    private static String pa_id = "";
    private static String pa_judul = "";
    private static String pa_pagu = "";
    private Button btn_submit_catatan;
    private EditText textcatatans;
    private ProgressDialog progressDialog;
    private Handler mHandler;
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        pa_id = intent.getStringExtra("pa_id");
        pa_judul = intent.getStringExtra("pa_nama");
        pa_pagu = intent.getStringExtra("pa_pagu");
        ke_id = intent.getStringExtra("ke_id");


        View view = inflater.inflate(R.layout.fragment_catatan, container, false);
        pr_catatan_fisik = view.findViewById(R.id.pr_catatan_fisik);
        textcatatans = view.findViewById(R.id.textcatatans);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        btn_submit_catatan = view.findViewById(R.id.btn_submit_catatan);
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

        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}