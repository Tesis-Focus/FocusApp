package com.example.focusappm;

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
        WeekViewEvent event = new WeekViewEvent();
        event.setName(this.getmName());
        event.setColor(this.getmColor());
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.set(mStartTime.getYear(),mStartTime.getMonth(),mStartTime.getDate(),mStartTime.getHours(),mStartTime.getMinutes());
        endTime.set(mEndTime.getYear(),mEndTime.getMonth(),mEndTime.getDate(),mEndTime.getHours(),mEndTime.getMinutes());
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setId(1);
        return event;
    }
}
