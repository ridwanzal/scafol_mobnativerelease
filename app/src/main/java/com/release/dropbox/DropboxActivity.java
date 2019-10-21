package com.release.dropbox;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.android.Auth;

import static android.content.Context.MODE_PRIVATE;

import com.google.gson.Gson;
import com.release.R;
import com.release.model.DataResponsePA;
import com.release.model.DataResponseToken;
import com.release.model.PaketDashboard;
import com.release.model.Token;
import com.release.restapi.ApiClient;
import com.release.restapi.ApiInterface;
import com.release.sharedexternalmodule.formatMoneyIDR;
import com.release.sharedpreferences.SessionManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends AppCompatActivity {
    public static ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    SessionManager sessionManager;
    String accessToken = "";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
//        Log.d("Dropboxactivty", new Gson().toJson(prefs));
//        String accessToken = prefs.getString("access-token", null);
//        if (accessToken == null) {
//            accessToken = "dS7WKoF3tJAAAAAAAAAQpq5ExNdQtJbcwehKmbMl5PIxFLcFYCNwo9HY1uzCUoJn";
//            prefs.edit().putString("access-token", accessToken).apply();
//            if(sessionManager.isLoggedIn()){
////            Call<DataResponsePA> getMyToken = apiInterface.countPaguPPTK(user_id);
//            }
//
//
//            if (accessToken != null) {
//                initAndLoadData(accessToken);
//            }
//        } else {
//            initAndLoadData(accessToken);
//        }

//        Toasty.success(getApplicationContext(), "Token : " + accessToken, Toasty.LENGTH_LONG).show();
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        Boolean islogin = sessionManager.isLoggedIn();
        Log.d("Dropboxactivty", "islogin " +islogin);
        if(islogin){
            Call<DataResponseToken> getmytoken = apiInterface.getRemoteToken();
            getmytoken.enqueue(new Callback<DataResponseToken>() {
                @Override
                public void onResponse(Call<DataResponseToken> call, Response<DataResponseToken> response) {
                    if(response.code()==200){
                        Log.d("Dropboxactivty", "success 200 ");
                        ArrayList<Token> result_token = response.body().getData();
                        for(int i = 0; i < result_token.size(); i++){
                            prefs.edit().putString("access-token", result_token.get(i).getToken()).apply();
                            initAndLoadData(result_token.get(i).getToken());
                        }
                    }

                }

                @Override
                public void onFailure(Call<DataResponseToken> call, Throwable t) {
                    Log.d("Dropboxactivty", "failed 500 ");
                }
            });
        }
        String uid = Auth.getUid();
        String storedUid = prefs.getString("user-id", null);
        if (uid != null && !uid.equals(storedUid)) {
            prefs.edit().putString("user-id", uid).apply();
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        PicassoClient.init(getApplicationContext(), DropboxClientFactory.getClient());
        loadData();
    }

    public void removeDropboxPref(SharedPreferences prefs){
        prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    protected abstract void loadData();

    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
//        Toasty.success(getApplicationContext(), "Token : " + accessToken, Toasty.LENGTH_LONG).show();
        return accessToken != null;
    }
}

