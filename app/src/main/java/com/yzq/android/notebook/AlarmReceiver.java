package com.yzq.android.notebook;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, Alarm.class);

        


        Toast.makeText(context, "The talk is coming", Toast.LENGTH_LONG).show();
    }
}
