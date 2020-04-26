package com.example.focusappm;

import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Horario implements Serializable, Comparable {

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
        startTime.set(Calendar.DAY_OF_MONTH,mStartTime.getDate());
        startTime.set(Calendar.MINUTE, mStartTime.getMinutes());
        startTime.set(Calendar.MONTH,mStartTime.getMonth()-1);
        startTime.set(Calendar.YEAR,mStartTime.getYear());

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, mEndTime.getHours());
        endTime.set(Calendar.DAY_OF_MONTH,mEndTime.getDate());
        endTime.set(Calendar.MINUTE, mEndTime.getMinutes());
        endTime.set(Calendar.MONTH,mEndTime.getMonth()-1);
        endTime.set(Calendar.YEAR,mEndTime.getYear());

        WeekViewEvent event = new WeekViewEvent(1,getmName(),startTime,endTime);
        event.setColor(Color.LTGRAY);

        //Log.i("cal", "toWeekViewEvent: Start anio: " + startTime.get(Calendar.YEAR)+" mes: "+startTime.get(Calendar.MONTH)+" dia del mes : " + startTime.get(Calendar.DAY_OF_MONTH)+" hora del dia "+startTime.get(Calendar.HOUR_OF_DAY)+" minuto: "+startTime.get(Calendar.MINUTE));
        //Log.i("cal", "toWeekViewEvent: End anio: " + endTime.get(Calendar.YEAR)+" mes: "+endTime.get(Calendar.MONTH)+" dia del mes : " + endTime.get(Calendar.DAY_OF_MONTH)+" hora del dia "+endTime.get(Calendar.HOUR_OF_DAY)+" minuto: "+endTime.get(Calendar.MINUTE));
        return event;
    }

    @Override
    public int compareTo(Object o) {

        Date thisStart, comparatedStart;
        Horario horarioCompare = (Horario)o;
        thisStart = this.getmStartTime();
        comparatedStart = horarioCompare.getmStartTime();

        if(thisStart.before(comparatedStart))
            return -1;
        else
            return 1;

    }
}
