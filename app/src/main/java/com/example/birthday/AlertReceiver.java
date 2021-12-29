package com.example.birthday;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {
    String LOG_TAG = "Kostia_Birthday";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive() called");
        Log.d(LOG_TAG, "1...");
        context.startService(new Intent(context, MyService.class));
        Log.d(LOG_TAG, "End of receive");
    }
}
