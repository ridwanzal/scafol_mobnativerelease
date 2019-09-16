package com.release.sharedexternalmodule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class checkConnection {
    Context mContext;
    public checkConnection(Context mContext){
        this.mContext = mContext;
    }

    public boolean test(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else{
            connected = false;
        }

        return connected;
    }
}
