package com.yzq.android.notebook;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, BeepManager.class);
        Toast.makeText(context, "The talk is coming", Toast.LENGTH_LONG).show();
        context.startService(i);
    }
}
