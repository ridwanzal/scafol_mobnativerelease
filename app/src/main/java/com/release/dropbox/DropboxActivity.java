package com.release.dropbox;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.android.Auth;

import static android.content.Context.MODE_PRIVATE;
import com.release.R;

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
            accessToken = "dS7WKoF3tJAAAAAAAAACgajSXM4QXFnHM04wHvK3bTx3xmRLtXU8mgc5EXu0W9ir";
            if (accessToken != null) {
                prefs.edit().putString("access-token", accessToken).apply();
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
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

    protected abstract void loadData();

    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }
}
