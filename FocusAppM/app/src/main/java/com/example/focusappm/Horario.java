package com.example.focusappm;

import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Horario implements Serializable {

    private String mId;
    private Date mStartTime;
    private Date mEndTime;
    private String mName;
    private String mLocation;
    private int mColor;


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Date getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(Date mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Date getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(Date mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "mId='" + mId + '\'' +
                ", mStartTime=" + mStartTime.toString() +
                ", mEndTime=" + mEndTime.toString() +
                ", mName='" + mName + '\'' +
                '}';
    }

    public WeekViewEvent toWeekViewEvent(){

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, mStartTime.getHours());
        startTime.set(Calendar.MINUTE, mStartTime.getMinutes());
        startTime.set(Calendar.MONTH,mStartTime.getMonth());
        startTime.set(Calendar.YEAR,mStartTime.getYear());

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, mEndTime.getHours());
        endTime.set(Calendar.MINUTE, mEndTime.getMinutes());

        WeekViewEvent event = new WeekViewEvent(1,mName,startTime,endTime);
        event.setColor(Color.CYAN);
        Log.i("cal", "toWeekViewEvent: 1 " + startTime.get(Calendar.YEAR)+" "+startTime.get(Calendar.MONTH)+" "+startTime.get(Calendar.HOUR_OF_DAY)+" "+startTime.get(Calendar.MINUTE));
        Log.i("cal", "toWeekViewEvent: 2 " + endTime.get(Calendar.YEAR)+" "+endTime.get(Calendar.MONTH)+" "+endTime.get(Calendar.HOUR_OF_DAY)+" "+endTime.get(Calendar.MINUTE));
        return event;
    }
}
