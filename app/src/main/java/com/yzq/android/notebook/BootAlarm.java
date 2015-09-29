package com.yzq.android.notebook;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2015/6/17 0017.
 */
public class BootAlarm extends Activity {
    private NoteDatabaseOperator mOperator;
    private NoteDatabaseHelper dbHelper;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        Intent intent = new Intent(BootAlarm.this, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(BootAlarm.this, 0, intent, 0);
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        //alarmManager.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis()+(50000),
                        //pendingIntent);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, ldate, pendingIntent);
                    }

                }


            } while (cursor.moveToNext());

        }
        cursor.close();
    }

}
