package com.release.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.release.service.ServiceReminder;
import com.release.sharedexternalmodule.ReminderCore;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG ="" ;
    ReminderCore reminderCore;
    @Override
    public void onReceive(Context context, Intent intent) {
//        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.d(TAG, "Receiver Notif Masuk");
            reminderCore = new ReminderCore(context, ServiceReminder.class, "Silahkan Update Progress");
            reminderCore.run();
//        }
    }
}
