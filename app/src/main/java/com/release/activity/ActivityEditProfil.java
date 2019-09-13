package com.release.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.release.R;
import com.release.model.DataResponse;
import com.release.model.DataResponseDinas;
import com.release.model.DataResponseUsers;
import com.release.model.Dinas;
import com.release.model.User;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedpreferences.SessionManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProfil extends AppCompatActivity {
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private static String TAG = "ActivityEditProfil";
    private TextView prof_username;
    private TextView prof_email;
    private TextView prof_telepon;
    private TextView prof_bagian;
    private TextView prof_nama;
    private TextView prof_dinas;
    SessionManager sessionManager;
    String user_id;
    String role;
    Dialog dialog;

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
        prof_dinas = findViewById(R.id.prof_dinas);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id =  user.get(SessionManager.KEY_USERID);
        role = user.get(SessionManager.KEY_ROLE);

        if(role.toLowerCase().equals("pptk")){

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
    }

    public void openBottomDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_editprofile, null);
        dialog = new BottomSheetDialog(this);
        final EditText prof_email_edit = view.findViewById(R.id.prof_email);
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
                Call<DataResponseUsers> update_user = apiInterface.updateProfile(user_id, get_nama, get_telepon);
                update_user.enqueue(new Callback<DataResponseUsers>() {
                    @Override
                    public void onResponse(Call<DataResponseUsers> call, Response<DataResponseUsers> response) {
                        if(response.code() == 200){
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponseUsers> call, Throwable t) {
                        dialog.dismiss();
                        prof_nama.setText(get_nama.toString().trim());
                        prof_telepon.setText(get_telepon.toString().trim());
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
                finish();
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
