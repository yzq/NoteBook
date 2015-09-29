package com.yzq.android.notebook;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2015/4/20 0020.
 */
public class Note {

   // private static final String JSON_ID = "id";
   // private static final String JSON_COMPANY = "company";


    private String mCompany;
    private UUID mId;
    private Date mDate;
    private String mPlace;
    private Boolean mAlarm;

    public Note() {
        mId = UUID.randomUUID();
        mAlarm = false;
       // Calendar calendar = Calendar.getInstance();
       // calendar.setTime(mDate);


    }

    public Note(String company) {
        mCompany = company;
        mAlarm = false;
    }
    public Note(UUID id, String company, String place) {
        mId = id;
        mCompany = company;
        mPlace = place;
        mAlarm = false;
    }
    public Note(UUID id, String company, Date date) {
        mId = id;
        mCompany = company;
        mDate = date;
        mAlarm = false;
    }

    public Note(UUID id, String company, Date date, String place) {
        mId = id;
        mCompany = company;
        mDate = date;
        mPlace = place;
        mAlarm = false;
    }

    public Note(UUID id, String company, Date date, String place, boolean alarm) {
        mId = id;
        mCompany = company;
        mDate = date;
        mPlace = place;
        mAlarm = alarm;
    }

    /*public Note(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mCompany = json.getString(JSON_COMPANY);
    }*/

    public UUID getId() {
        return mId;
    }

    public String getCompany() {
        return mCompany;
    }

    public Date getDate() {
        return mDate;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setCompany(String company) {
        mCompany = company;
    }
    public void setDate(Date date) {
        mDate = date;
    }
    public void setPlace(String place) {
        mPlace = place;
    }
    public boolean isAlarm() {
        return mAlarm;
    }
    public void setAlarm(boolean alarm) {
        mAlarm = alarm;
    }

}
