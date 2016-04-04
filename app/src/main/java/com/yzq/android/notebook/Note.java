package com.yzq.android.notebook;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2015/4/20 0020.
 * Note类处于MVC模式的模型层，用于存储记事本记录的各项数据
 */
public class Note {

    //公司名称
    private String mCompany;
    //唯一标识符
    private UUID mId;
    //宣讲会时间
    private Date mDate;
    //宣讲会地点
    private String mPlace;
    //宣讲会时间提醒标志
    private Boolean mAlarm;

    public Note() {
        mId = UUID.randomUUID();
        mAlarm = false;
    }
    
    public Note(UUID id, String company, String place) {
        mId = id;
        mCompany = company;
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
