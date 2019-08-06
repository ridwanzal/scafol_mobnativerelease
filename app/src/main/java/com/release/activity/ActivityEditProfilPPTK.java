package com.release.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.release.R;
import com.release.model.DataResponse;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedpreferences.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProfilPPTK extends AppCompatActivity {
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "ActivityEditProfilPPTK";
    private EditText prof_username;
    private EditText prof_email;
    private EditText prof_telepon;
    private EditText prof_bagian;
    private TextView prof_nama;
    SessionManager sessionManager;
    String user_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofilepptk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prof_username = findViewById(R.id.prof_username);
        prof_email = findViewById(R.id.prof_email);
        prof_telepon = findViewById(R.id.prof_telepon);
        prof_bagian = findViewById(R.id.prof_role);
        prof_nama = findViewById(R.id.prof_namas);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id =  user.get(SessionManager.KEY_USERID);

        Call<DataResponse> call_user = apiInterface.getUserById(user_id);
        call_user.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                ArrayList<User> list = response.body().getData();
                for(int i = 0; i < list.size(); i++){
                    prof_username.setText(list.get(i).getUsername());
                    prof_bagian.setText(list.get(i).getRole().toUpperCase());
                    prof_telepon.setText(checkData(list.get(i).getTelephone()));
                    prof_email.setText(checkData(list.get(i).getTelephone()));
                    prof_nama.setText(checkData(list.get(i).getNama()));
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public static String checkData(String data){
        if(data == null || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
