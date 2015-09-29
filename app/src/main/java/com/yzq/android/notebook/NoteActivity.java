package com.yzq.android.notebook;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;


public class NoteActivity extends Activity {
    private Note mNote;
    private EditText mCompanyField;
    private EditText mPlaceField;
    private View view;
    public static final String EXTRA_NOTE_ID = "com.yzq.android.notebook.note_id";
    private boolean exitflag;
    public static final String TAG = "NoteActivity";
    private Button mDateButton;
    private Date mDate;
    private CheckBox mAlarmCheckBox;
    private AlarmManager alarmManager;

    //TargetApi(11)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        UUID noteId = (UUID)getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        mNote = NoteLab.get(this).getNote(noteId);
      //  mNote = new Note();

        if (mNote == null) {
            mNote = new Note();
        }
        if (mNote.getCompany() == null) {
            exitflag = false;
        } else {
            Log.d(TAG, "notes is exit");
            exitflag = true;
        }


        mDateButton = (Button)findViewById(R.id.note_date);
        mDate = mNote.getDate();
        if (mDate!= null) {
            mDateButton.setText(mDate.toString());
           // mDateButton.setText(new DateFormat().format("yyyy-MM-dd kk:mm", mDate.toString()));
        }

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dateView = getLayoutInflater().inflate(R.layout.dialog_date, null);
                AlertDialog.Builder datedialog = new AlertDialog.Builder(NoteActivity.this);
                datedialog.setView(dateView);
                datedialog.setTitle("Date");
               // datedialog.setPositiveButton("OK",null);
                datedialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDate != null) {
                            mDateButton.setText(mDate.toString());
                          //  mDateButton.setText(DateFormat.format("yyyy-MM-dd kk:mm", mDate.toString()));
                            mNote.setDate(mDate);
                            Log.d(TAG, "save date:"+mDate.getTime());

                        }

                        if (mNote.isAlarm()) {
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = calendar.get(Calendar.HOUR);
                            int minute = calendar.get(Calendar.MINUTE);
                            long currenttime = new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis();
                            long alarmtime = mNote.getDate().getTime();
                            Log.d(TAG, "date:" + alarmtime);
                            if (alarmtime < currenttime) {
                                Toast.makeText(NoteActivity.this, "alarmtime < currenttiime", Toast.LENGTH_SHORT).show();
                                //isChecked = false;
                            } else {
                                Intent intent = new Intent(NoteActivity.this, AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteActivity.this, 0, intent, 0);
                                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis()+(50000),
                                //pendingIntent);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntent);
                                Log.d(TAG, "alarmstart");

                            }
                        }
                    }
                });
                datedialog.show();

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                mDate = new GregorianCalendar(year, month, day, hour, minute).getTime();
               // int hour = calendar.get(Calendar.HOUR);
                //int minute = calendar.get(Calendar.MINUTE);
                //Toast.makeText(NoteActivity.this, "year:"+year+", month:"+month+", day:"+day, Toast.LENGTH_SHORT).show();
                //Toast.makeText(NoteActivity.this, month, Toast.LENGTH_SHORT);
                //Toast.makeText(NoteActivity.this, day, Toast.LENGTH_SHORT);

                DatePicker datePicker = (DatePicker)dateView.findViewById(R.id.dialog_date_datePicker);
                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int day) {
                        mDate = new GregorianCalendar(year, month, day).getTime();
                        //mDateButton.setText(mDate.toString());


                    }
                });

                TimePicker timePicker = (TimePicker)dateView.findViewById(R.id.dialog_time_timePicker);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hour, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(mDate);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        mDate = new GregorianCalendar(year, month, day, hour, minute).getTime();

                    }

                });
            }
        });


        mCompanyField = (EditText)findViewById(R.id.company);
        mCompanyField.setText(mNote.getCompany());
        mCompanyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i2, int i3) {

                mNote.setCompany(c.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPlaceField = (EditText)findViewById(R.id.place);
        mPlaceField.setText(mNote.getPlace());
        mPlaceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setPlace(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAlarmCheckBox = (CheckBox)findViewById(R.id.note_alarm);
        mAlarmCheckBox.setChecked(mNote.isAlarm());
        mAlarmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNote.setAlarm(isChecked);
                Log.d(TAG, "test:" + isChecked);
                if (mNote.isAlarm()) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    long currenttime = new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis();
                    long alarmtime = mNote.getDate().getTime();
                    Log.d(TAG, "date:" + alarmtime);
                    if (alarmtime < currenttime) {
                        Toast.makeText(NoteActivity.this, "alarmtime < currenttiime", Toast.LENGTH_SHORT).show();
                        //isChecked = false;
                    } else {
                        Intent intent = new Intent(NoteActivity.this, AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteActivity.this, 0, intent, 0);
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        //alarmManager.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis()+(50000),
                        //pendingIntent);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntent);
                        Log.d(TAG, "alarmstart");
                    }
                }
            }
        });



        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;

            case R.id.menu_item_save_note:

                //NoteLab.get(this).addNote(mNote);
                //NoteLab.get(this).saveNotes(mNote);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNote.getCompany() == null) {
            NoteLab.get(this).deleteNote(mNote);
        }
        else if (exitflag){
            NoteLab.get(this).updateNotes(mNote);
        } else {

            NoteLab.get(this).saveNotes(mNote);
            Log.d(TAG, "savenote");
        }
    }
}
