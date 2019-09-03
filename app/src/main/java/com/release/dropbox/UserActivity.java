package com.release.dropbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.dropbox.core.android.Auth;
import com.release.dropbox.internal.*;
import com.dropbox.core.v2.users.FullAccount;

import com.release.R;


/**
 * Activity that shows information about the currently logged in user
 */
public class UserActivity extends DropboxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user);

        Button loginButton = (Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.startOAuth2Authentication(UserActivity.this, getString(R.string.app_key));
            }
        });

        Button filesButton = (Button)findViewById(R.id.files_button);
        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result_path = "";
                String result_detail = "";
                Intent intent = getIntent();
                String id_paket = intent.getStringExtra("path_dropbox");
                String nama_paket = intent.getStringExtra("pa_judul");
                String upload_type =  intent.getStringExtra("upload_type");
                if(id_paket == ""){
                    result_path = "";
                }else{
                    result_path = id_paket;
                }

                if(nama_paket == ""){
                    result_detail = "";
                }else{
                    result_detail = nama_paket;
                }
                startActivity(FilesActivity.getIntent(UserActivity.this, result_path));
//                startActivity(FilesActivity.getIntent(UserActivity.this, result_path, result_detail));
            }
        });

        Button openWithButton = (Button)findViewById(R.id.open_with);
        openWithButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openWithIntent = new Intent(UserActivity.this, OpenWithActivity.class);
                startActivity(openWithIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasToken()) {
            findViewById(R.id.login_button).setVisibility(View.GONE);
            findViewById(R.id.email_text).setVisibility(View.VISIBLE);
            findViewById(R.id.name_text).setVisibility(View.VISIBLE);
            findViewById(R.id.type_text).setVisibility(View.VISIBLE);
            findViewById(R.id.files_button).setEnabled(true);
            findViewById(R.id.open_with).setEnabled(true);
        } else {
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            findViewById(R.id.email_text).setVisibility(View.GONE);
            findViewById(R.id.name_text).setVisibility(View.GONE);
            findViewById(R.id.type_text).setVisibility(View.GONE);
            findViewById(R.id.files_button).setEnabled(false);
            findViewById(R.id.open_with).setEnabled(false);
        }
    }

    @Override
    protected void loadData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                if(result.getEmail().equals("scafoltk@gmail.com")){

                }
//               ((TextView) findViewById(R.id.email_text)).setText(result.getEmail());
//               ((TextView) findViewById(R.id.name_text)).setText(result.getName().getDisplayName());
//               ((TextView) findViewById(R.id.type_text)).setText(result.getAccountType().name());

                ((TextView) findViewById(R.id.sync_status_condc)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.sync_status_con)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.files_button)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.centang_ok)).setVisibility(View.VISIBLE);
                ((ProgressBar) findViewById(R.id.progress_bardropboxuseract)).setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                ((TextView) findViewById(R.id.sync_status_condc)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.sync_status_con)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.files_button)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.centang_ok)).setVisibility(View.GONE);
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }

}
