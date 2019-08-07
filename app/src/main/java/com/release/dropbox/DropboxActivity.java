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
            accessToken = "sl.AIe6Xyztf7ii9o-xW4g_lKv8LEwwi8w89KvhBO0qLjn-4YoST8_BBmIIccs9oePHi3YwQK_ctxVDsBpsw_60qvLY5FAWmZXu146K8i2ApgfNGGWBbxQben0eWa0ycKdobWDlIfch";
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
