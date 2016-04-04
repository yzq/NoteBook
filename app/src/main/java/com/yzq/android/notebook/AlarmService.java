package com.yzq.android.notebook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class AlarmService extends Service {

    private NoteDatabaseOperator mOperator;
    private NoteDatabaseHelper dbHelper;
    private AlarmManager alarmManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mOperator = new NoteDatabaseOperator(this, NoteLab.DBNAME, 1);
        dbHelper = mOperator.getDbHelper();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Note", null, null, null, null, null, null);
        Date currentTime = new Date();
        if (cursor.moveToFirst()) {
            do {
                //UUID id = UUID.fromString(cursor.getString(cursor.getColumnIndex("id")));
                // String company = cursor.getString(cursor.getColumnIndex("company"));
                // String place = cursor.getString(cursor.getColumnIndex("place"));
                int alarm = cursor.getInt(cursor.getColumnIndex("alarm"));
                long   ldate = cursor.getLong(cursor.getColumnIndex("date"));

                if (alarm == 1) {
                    if (ldate < currentTime.getTime()) {
                        continue;
                    } else {

                        Intent i = new Intent(AlarmService.this, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmService.this, cursor.getInt(cursor.getColumnIndex("key")), i, 0);
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        //alarmManager.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis()+(50000),
                        //pendingIntent);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, ldate, pendingIntent);

                        return super.onStartCommand(intent, flags, startId);
                    }

                }


            } while (cursor.moveToNext());

        }
        cursor.close();

        return super.onStartCommand(intent, flags, startId);
    }
}
