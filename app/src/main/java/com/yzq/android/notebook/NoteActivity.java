package com.yzq.android.notebook;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;


public class NoteActivity extends Activity {

    private Note mNote;
    private EditText mCompanyField;
    private EditText mPlaceField;
    public static final String EXTRA_NOTE_ID = "com.yzq.android.notebook.note_id";
    private boolean exitflag;
    public static final String TAG = "NoteActivity";
    public static final String ACTIONBAR_COLOR = "#33cc77";
    private Button mDateButton;
    private Date mDate;
    private CheckBox mAlarmCheckBox;
    private AlarmManager alarmManager;
    private NoteDatabaseOperator mOperator;
    private NoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ACTIONBAR_COLOR)));

        //获取NoteListActivity传来的Note标识符
        UUID noteId = (UUID)getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        //通过标识符获取到对应的Note对象，从而操作其数据成员
        mNote = NoteLab.get(this).getNote(noteId);
        if (mNote == null) {
            mNote = new Note();
        }
        //若没有记录公司，表明该Note是无效的，退出时，不需要更新数据库中的数据
        if (mNote.getCompany() == null) {
            exitflag = false;
        } else {
            Log.d(TAG, "notes is exit");
            exitflag = true;
        }

        mDateButton = (Button)findViewById(R.id.note_date);
        //获取该NoteActivity对应Note对象的数据：招聘时间
        mDate = mNote.getDate();
        if (mDate!= null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            String mDateFormat = simpleDateFormat.format(mDate);
            mDateButton.setText(mDateFormat);
        }

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dateView = getLayoutInflater().inflate(R.layout.dialog_date, null);
                AlertDialog.Builder datedialog = new AlertDialog.Builder(NoteActivity.this);
                datedialog.setView(dateView);
                datedialog.setTitle("Date");
                //点击“"OK",确认时间设置
                datedialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDate != null) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                            String mDateFormat = simpleDateFormat.format(mDate);
                            mDateButton.setText(mDateFormat);
                            mNote.setDate(mDate);
                            Log.d(TAG, "save date:"+mDate.getTime());

                        }

                        /*if (mNote.isAlarm()) {
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = calendar.get(Calendar.HOUR);
                            int minute = calendar.get(Calendar.MINUTE);
                            long currenttime = new GregorianCalendar(year, month, day, hour, minute).getTimeInMillis();
                            long alarmtime = mNote.getDate().getTime();
                            Log.d(TAG, "date:" + alarmtime);
                            //int alarmid = Integer.parseInt(mNote.getCompany());
                            //Log.d(TAG, "alarmid:" + alarmid);
                            if (alarmtime < currenttime) {
                                Toast.makeText(NoteActivity.this, "alarmtime < currenttiime", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(NoteActivity.this, AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteActivity.this, alarmid, intent, 0);
                                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmtime, pendingIntent);
                                Log.d(TAG, "alarmstart");

                            }
                        }*/
                    }
                });
                datedialog.show();

                //获取当前日期和时间，以生成Date对象
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                mDate = new GregorianCalendar(year, month, day, hour, minute).getTime();

                DatePicker datePicker = (DatePicker)dateView.findViewById(R.id.dialog_date_datePicker);
                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int day) {
                        //修改后的日期生成Date对象
                        mDate = new GregorianCalendar(year, month, day).getTime();


                    }
                });

                TimePicker timePicker = (TimePicker)dateView.findViewById(R.id.dialog_time_timePicker);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hour, int minute) {
                        //设置时间后，生成Date对象
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
                //用户在界面中的公司处输入了数据
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
                //用户在界面中的地点处输入了宣讲会地址
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

            }
        });
        //给左上角图标的左边加上一个返回的图标
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
            return;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        long time;

        if (exitflag){
            NoteLab.get(this).updateNotes(mNote);
        } else {

            NoteLab.get(this).saveNotes(mNote);
            Log.d(TAG, "savenote");
        }

        NoteLab noteLab = NoteLab.get(NoteActivity.this);
        int key = noteLab.queryId(mNote);
        Intent i = new Intent(NoteActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteActivity.this, key, i, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (mDate != null) {
            time = mDate.getTime();
        } else {
            return;
        }
        if (mNote.isAlarm() && (time > new Date().getTime())) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

        }
        else {
            alarmManager.cancel(pendingIntent);
        }


    }
}
