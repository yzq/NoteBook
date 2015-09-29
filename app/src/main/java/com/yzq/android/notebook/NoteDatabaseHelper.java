package com.yzq.android.notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/5/5 0005.
 */
public class NoteDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "NoteDatabaseHelper";
    public static final String CREATE_NOTE = "create table Note ("
            + "key integer primary key autoincrement, "
            + "id text,"
            + "company text,"
            + "date integer,"
            + "place text,"
            + "alarm integer)";

    private Context mContext;

    public NoteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE);
        Log.d(TAG, "Create table succeeded");
        //Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
