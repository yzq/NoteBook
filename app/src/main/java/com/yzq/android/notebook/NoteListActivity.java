package com.yzq.android.notebook;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;



/**
 * Created by Administrator on 2015/4/22 0022.
 */
public class NoteListActivity extends Activity {

    private ArrayList<Note> mNotes;
    public static final String EXTRA_NOTE_ID = "note.id";
    public static final String TAG = "NoteListActivity";
    public static final String ACTIONBAR_COLOR = "#33cc77";
    ListView listView;
    NoteAdapter adapter;

    @TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notelist);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ACTIONBAR_COLOR)));

        mNotes = NoteLab.get(this).getNotes();

        adapter = new NoteAdapter(NoteListActivity.this,
                R.layout.note_item, mNotes);

        listView = (ListView) findViewById(R.id.notelist_view);
        listView.setAdapter(adapter);
        Log.d(TAG, "set adapter");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = mNotes.get(position);

                Intent i = new Intent(NoteListActivity.this, NoteActivity.class);
                i.putExtra(NoteActivity.EXTRA_NOTE_ID, note.getId());
                startActivity(i);
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                private int mposition;
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    mposition = position;


                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_note_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_note:
                            NoteLab noteLab = NoteLab.get(NoteListActivity.this);
                            /*for (int i = adapter.getCount()-1; i >= 0; i--) {
                                noteLab.deleteNote(adapter.getItem(i));
                            }*/
                            Note note = adapter.getItem(mposition);
                            if (note.isAlarm()) {
                                int key = noteLab.queryId(note);
                                Intent i = new Intent(NoteListActivity.this, AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteListActivity.this, key, i, 0);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);
                            }

                            noteLab.deleteNote(note);

                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_note:
                Note note = new Note();
                NoteLab.get(this).addNote(note);

                Intent i = new Intent(NoteListActivity.this, NoteActivity.class);
                i.putExtra(NoteActivity.EXTRA_NOTE_ID, note.getId());
                //i.putExtra(NoteActivity.EXTRA_NOTE_ID, "note");
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();


        //listView.setAdapter(adapter);
        //listView.setAdapter(adapter);
        //adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();

    }
}
