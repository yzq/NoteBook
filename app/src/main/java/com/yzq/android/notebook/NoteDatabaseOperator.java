package com.yzq.android.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2015/5/6 0006.
 */
public class NoteDatabaseOperator {
    private static final String TAG = "NoteDatabaseOperator";

    private NoteDatabaseHelper dbHelper;

    public NoteDatabaseOperator(Context context, String dbName, int dbVersion) {
        dbHelper = new NoteDatabaseHelper(context, dbName, null, dbVersion );
    }

    public NoteDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void saveNotes(Note n) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", n.getId().toString());
        values.put("company", n.getCompany());
        if (n.getDate()!= null)
            values.put("date", n.getDate().getTime());

        values.put("place", n.getPlace());
        values.put("alarm", n.isAlarm());
        db.insert("Note", null, values);
        values.clear();


    }

    public void updateNotes(Note n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String mid = n.getId().toString();
        ContentValues values = new ContentValues();
        values.put("company", n.getCompany());

        if (n.getDate() != null)
            values.put("date", n.getDate().getTime());

        values.put("place", n.getPlace());
        values.put("alarm", n.isAlarm());
        db.update("Note", values, "id = ?", new String[]{mid});
        //db.execSQL("update Note set company = ? where id =?", new String[] { n.getCompany(), mid });
    }

    public ArrayList<Note> loadNotes() {
        boolean IsAlarm = false;
        ArrayList<Note> notes = new ArrayList<Note>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Note", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                UUID id = UUID.fromString(cursor.getString(cursor.getColumnIndex("id")));
                String company = cursor.getString(cursor.getColumnIndex("company"));
                String place = cursor.getString(cursor.getColumnIndex("place"));
                int alarm = cursor.getInt(cursor.getColumnIndex("alarm"));
                if (alarm == 0)
                    IsAlarm = false;
                else
                    IsAlarm = true;
                //int  idate = cursor.getInt(cursor.getColumnIndex("date"));
                long idate = cursor.getLong(cursor.getColumnIndex("date"));
                long ldate;

                if (idate == 0) {
                    notes.add(new Note(id, company, place));
                }
                else {
                    ldate = idate;
                    Log.d(TAG, "loaddate:"+ldate);

                    Date date = new Date(ldate);
                    notes.add(new Note(id, company, date, place, IsAlarm));
                }

            } while (cursor.moveToNext());
            //db.rawQuery(“select * from Note”, null);
        }
        cursor.close();

        return notes;
    }

    public void deleteNote(Note n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String mid = n.getId().toString();
        db.delete("Note", "id = ?", new String[]{mid});
        //db.execSQL("delete from Note where id =?", new String[] { mid });

    }

    public int queryId(Note n) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String mid = n.getId().toString();
        Cursor cursor = db.query("Note", null, "id = ?", new String[]{mid}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("key"));
        }
        else {
            return -1;
        }

    }
}
