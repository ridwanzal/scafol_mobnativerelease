package com.release.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseDeviceToken extends FirebaseInstanceIdService {
    public FirebaseDeviceToken() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String device_token = FirebaseInstanceId.getInstance().getToken();
        Log.d("device token", device_token);
    }
}
