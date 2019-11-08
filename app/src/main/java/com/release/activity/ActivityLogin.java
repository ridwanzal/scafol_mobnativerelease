package com.release.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.release.R;
import com.release.dropbox.ActivityTag;
import com.release.model.DataResponse;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.release.sharedpreferences.SessionManager;

import com.release.sharedexternalmodule.checkConnection;

public class ActivityLogin extends AppCompatActivity{
    private checkConnection checkConnection;
    private static String TAG = "ActivityMain";
    private TextView txt_uname;
    private TextView txt_pass;
    private TextView tx_helpslogin;
    private Button btn;
    private CheckBox check_pass;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    SessionManager sessionManager;
    private String login_name = "";
    private String login_userid = "";
    private String login_username = "";
    private String login_dinasid = "";
    private String login_email = "";
    private String login_role = "";
    private String login_bidang = "";

    private Boolean check_conn;

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        setContentView(R.layout.activity_logins);
        getSupportActionBar().hide();
        btn = findViewById(R.id.btn_login);
        txt_uname = findViewById(R.id.txt_username);
        txt_pass = findViewById(R.id.txt_password);
        check_pass = findViewById(R.id.check_pass);
        tx_helpslogin = findViewById(R.id.tx_helpslogin);
        relativeLayout = findViewById(R.id.layoutlogins);
        linearLayout = findViewById(R.id.layoutlogins2);

        checkConnection = new checkConnection(getApplicationContext());
        check_conn = checkConnection.test();
        if(check_conn){
//            Toasty.success(getApplicationContext(), "You have internet connection ", Toasty.LENGTH_LONG).show();
        }else{
//            Toasty.error(getApplicationContext(), "You have internet connection ", Toasty.LENGTH_LONG).show();
        }

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
            // BACKGROUND JADI HITAM karena bug di versi N
//            Toast.makeText(this, "Ini versi N BROH", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "INI ANDROID M" + android.os.Build.VERSION.SDK_INT );
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else{
            // BACKGROUND AS IS
            Log.d(TAG, "INI BUKAN ANDROID M" + android.os.Build.VERSION.SDK_INT );
        }

        sessionManager = new SessionManager(getApplicationContext());
        final View parentLayout = findViewById(android.R.id.content);

        tx_helpslogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://scafol.com"));
                startActivity(browserIntent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String result_name = txt_uname.getText().toString().trim();
                String result_pass = txt_uname.getText().toString().trim();
                int PERMISSION_ALL = 1;
                String[] PERMISSIONS = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.CONTROL_LOCATION_UPDATES,
                };
                if(!checkAllPermission(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(ActivityLogin.this, PERMISSIONS, PERMISSION_ALL);
                }else{
                }
                check_conn = checkConnection.test();
                if(check_conn){
                    if(result_name.equals("") || result_pass.equals("")){
                        Snackbar.make(parentLayout, "Silahkan isi data", Snackbar.LENGTH_LONG).show();
                    }else{
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<DataResponse> call= apiInterface.checkLogin(txt_uname.getText().toString(), txt_pass.getText().toString());
                        call.enqueue(new Callback<DataResponse>() {
                            @Override
                            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                                Log.w(TAG, "result response" + new Gson().toJson(response));
                                if(response.isSuccessful() || new Gson().toJson(response.code()).toString().equals("200")){
                                    ArrayList<User> data = response.body().getData();
                                    if(!data.isEmpty()){
                                        for(int i = 0 ; i < data.size(); i++){
                                            login_name = data.get(i).getNama();
                                            login_userid =  data.get(i).getUserId();
                                            login_username = data.get(i).getUsername();
                                            login_dinasid = data.get(i).getDinasId();
                                            login_role = data.get(i).getRole();
                                            login_email = "";
                                            login_bidang = data.get(i).getBiId();
                                            sessionManager.createLoginSessionUsername(
                                                    login_name,
                                                    login_username,
                                                    login_userid,
                                                    login_dinasid,
                                                    login_email,
                                                    login_role,
                                                    login_bidang);
                                        }

                                        Intent intent = new Intent(getApplicationContext(), ActivityDashboard.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }else{
                                    Toasty.error(getApplicationContext(), "Login Failed", Toasty.LENGTH_SHORT, true).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<DataResponse> call, Throwable t) {
                                Log.w(TAG, "result response problem" + t);
                                Log.w(TAG, "result response problem" + call);
                                Toast.makeText(ActivityLogin.this, "System Error, Apps Not Working. Please report this issue", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Snackbar.make(parentLayout, "Tidak ada koneksi Internet", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean checkAllPermission(View.OnClickListener listener, String... permissions){
        if (listener != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
