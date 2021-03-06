package com.release.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.files.FileMetadata;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.release.R;
import com.release.dropbox.ActivityTag;
import com.release.dropbox.DropboxActivity;
import com.release.dropbox.DropboxClientFactory;
import com.release.dropbox.FilesActivity;
import com.release.dropbox.FilesActivityDirect;
import com.release.dropbox.PicassoClient;
import com.release.dropbox.UploadFileTask;
import com.release.model.DataResponse;
import com.release.model.DataResponseDinas;
import com.release.model.DataResponseUsers;
import com.release.model.Dinas;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedpreferences.SessionManager;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProfil extends AppCompatActivity {
    private DropboxActivity dropboxActivity;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 91;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "ActivityEditProfil";
    private TextView prof_username;
    private TextView prof_email;
    private TextView prof_telepon;
    private TextView prof_bagian;
    private TextView prof_nama;
    private TextView prof_dinas;
    private ImageView prof_logo_dinas;
    private static final int PICKFILE_REQUEST_CODE = 1;
    private SessionManager sessionManager;
    private String user_id;
    private String role;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private Handler mHandler;
    private RelativeLayout main_container_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        setContentView(R.layout.activity_editprofilepptk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prof_username = findViewById(R.id.prof_username);
        prof_email = findViewById(R.id.prof_email);
        prof_telepon = findViewById(R.id.prof_telepon);
        prof_bagian = findViewById(R.id.prof_role);
        prof_nama = findViewById(R.id.prof_namas);
        prof_dinas = findViewById(R.id.prof_dinas);
        prof_logo_dinas = findViewById(R.id.prof_logo_dinas);
        main_container_profile = findViewById(R.id.main_container_profile);
        main_container_profile.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(ActivityEditProfil.this);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id =  user.get(SessionManager.KEY_USERID);
        role = user.get(SessionManager.KEY_ROLE);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        if(role.toLowerCase().equals("pptk")){
//            prof_logo_dinas.setVisibility(View.GONE);
        }

        Call<DataResponse> call_user = apiInterface.getUserById(user_id);
        call_user.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.code() == 200){
                    ArrayList<User> list = response.body().getData();
                    for(int i = 0; i < list.size(); i++){
                        prof_username.setText(list.get(i).getUsername());
                        prof_bagian.setText(list.get(i).getRole().toUpperCase());
                        prof_telepon.setText(checkData(list.get(i).getTelephone()));
                        prof_nama.setText(checkData(list.get(i).getNama()));
                        prof_email.setText(checkData(list.get(i).getEmail()));
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });

        Call<DataResponseDinas> call_dinas = apiInterface.getDinas(user_id);
        call_dinas.enqueue(new Callback<DataResponseDinas>() {
            @Override
            public void onResponse(Call<DataResponseDinas> call, Response<DataResponseDinas> response) {
                if(response.code() == 200){
                    ArrayList<Dinas> list = response.body().getData();
                    for(int i = 0; i < list.size(); i++){
                        prof_dinas.setText(list.get(i).getDinasNama());
                    }
                }
            }
            @Override
            public void onFailure(Call<DataResponseDinas> call, Throwable t) {

            }
        });

        prof_logo_dinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // requste permission read storage
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,MY_PERMISSIONS_REQUEST_STORAGE);
//                launchFilePicker();
            }
        });
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(Message.obtain(mHandler, 1));
            }
        }).start();
        mHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1 :
                        progressDialog.dismiss();
                        main_container_profile.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ActivityEditProfil.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityEditProfil.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ActivityEditProfil.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ActivityEditProfil.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            launchFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    //Request location updates:
                    launchFilePicker();
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                    }



                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICKFILE_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Testing: " + data.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "request code pick file gambar" +  data.toString());
                Log.d(TAG, data.toString());
            }
        }
    }

    private void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    public void openBottomDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_editprofile, null);
        dialog = new BottomSheetDialog(this);
        final EditText prof_email_edit = view.findViewById(R.id.prof_email_edit);
        final EditText prof_telepon_edit = view.findViewById(R.id.prof_telepon_edit);
        final EditText prof_nama_edit = view.findViewById(R.id.prof_namas);
        final TextView submit_profile_edit= view.findViewById(R.id.submit_profile);
        final ImageView imagelogo = view.findViewById(R.id.imagelogo);
        Call<DataResponse> call_user = apiInterface.getUserById(user_id);
        call_user.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if(response.code() == 200){
                    ArrayList<User> list = response.body().getData();
                    for(int i = 0; i < list.size(); i++){
                        prof_telepon_edit.setText(checkData(list.get(i).getTelephone()));
                        prof_nama_edit.setText(checkData(list.get(i).getNama()));
                        prof_email_edit.setText(checkData(list.get(i).getEmail()));
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {

            }
        });


        submit_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String get_nama = prof_nama_edit.getText().toString().trim();
                final String get_telepon = prof_telepon_edit.getText().toString().trim();
                final String get_email = prof_email_edit.getText().toString().trim();
                Call<DataResponseUsers> update_user = apiInterface.updateProfile(user_id, get_nama, get_email, get_telepon);
                update_user.enqueue(new Callback<DataResponseUsers>() {
                    @Override
                    public void onResponse(Call<DataResponseUsers> call, Response<DataResponseUsers> response) {
                        Log.d(TAG, "Response : " + new Gson().toJson(response));
                        if(response.code() == 200){
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponseUsers> call, Throwable t) {
                        dialog.dismiss();
                        prof_nama.setText(get_nama.toString().trim());
                        prof_telepon.setText(get_telepon.toString().trim());
                        prof_email.setText(get_email.toString().trim());
                        HashMap<String, String> user_update = sessionManager.getUserDetails();
                        user_update.put(sessionManager.KEY_NAME, "");
                        user_update.put(sessionManager.KEY_NAME, get_nama.toString().trim());
                    }
                });
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressedRefresh();
                return true;
            case R.id.nav_editprofil :
                openBottomDialog();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_editprofil, menu);
        return true;
    }

    public static String checkData(String data){
        if(data == null || data == "" || data.equals("")){
            return "-";
        }else{
            return data;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ActivityDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
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

    public void onBackPressedRefresh(){
        Intent intent = new Intent(this, ActivityDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        sessionManager.checkLogin();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
