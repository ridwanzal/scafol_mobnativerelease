package com.release.dropbox;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.android.Auth;

import static android.content.Context.MODE_PRIVATE;
import com.release.R;

import es.dmoral.toasty.Toasty;

/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
//            accessToken = Auth.getOAuth2Token();
//            accessToken = "sl.AIelT6L_gdPLSdivhV5y0etHvw6vVFFoc9oHCI0-Awyx5IgcY-ocBo3bZ93cx64jj9VGSjDY5Yt2OlQwRCQCuGCLOMKr4hps0XZC2HlMpT-daejSuZFKzEWlIRAnWpHXcBGCNyHX";
            accessToken = "dS7WKoF3tJAAAAAAAAAQTsT3dy9PKF5NrTSA701dKLeo27cezgb52g6dtSSDBm_t";
            prefs.edit().putString("access-token", accessToken).apply();
            if (accessToken != null) {
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
        }

//        Toasty.success(getApplicationContext(), "Token : " + accessToken, Toasty.LENGTH_LONG).show();
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

    protected abstract void loadData();

    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
//        Toasty.success(getApplicationContext(), "Token : " + accessToken, Toasty.LENGTH_LONG).show();
        return accessToken != null;
    }
}
