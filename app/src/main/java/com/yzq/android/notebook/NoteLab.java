package com.yzq.android.notebook;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2015/4/20 0020.
 */
public class NoteLab {
    private static final String TAG = "NoteLab";
    public static final String DBNAME = "NoteStore.db";
    private ArrayList<Note> mNotes;
    private static NoteLab sNoteLab;
    private Context mAppContext;
    private NoteDatabaseOperator mOperator;

    private NoteLab(Context appContext) {
        mAppContext = appContext;
        mOperator = new NoteDatabaseOperator(mAppContext, DBNAME, 1);
        try {
            mNotes = mOperator.loadNotes();
            Log.d(TAG, "loading notes: ");
        } catch (Exception e) {
            mNotes = new ArrayList<Note>();
            Log.e(TAG, "Error loading notes: ", e);
        }
    }

    public static NoteLab get(Context c) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(c.getApplicationContext());
        }
        return sNoteLab;
    }

    public ArrayList<Note> getNotes() {
        return mNotes;
    }

    public Note getNote(UUID id) {
        for (Note n : mNotes) {
            if (n.getId().equals(id))
                return n;
        }
        return null;
    }

    public void addNote(Note n) {
        mNotes.add(n);
    }
    public void deleteNote(Note n) {
        mOperator.deleteNote(n);
        mNotes.remove(n);
    }

    public boolean saveNotes(Note n) {
        try {
            //mOperator.saveNotes(mNotes);
            mOperator.saveNotes(n);
            Log.d(TAG, "notes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving notes: ", e);
            return false;
        }
    }

    public void updateNotes(Note n) {
        mOperator.updateNotes(n);

    }
}
