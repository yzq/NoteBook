package com.yzq.android.notebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2015/6/17 0017.
 */
public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "start BootAlarm");
        context.startService(i);
    }
}
