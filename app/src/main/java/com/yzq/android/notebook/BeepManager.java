package com.yzq.android.notebook;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

public class BeepManager extends Service {

    private long[] vduration = new long[]{500,2000,500,2000,500,2000};

    public BeepManager() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override

    public int onStartCommand (Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(vduration, -1);
                }
                stopSelf();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
