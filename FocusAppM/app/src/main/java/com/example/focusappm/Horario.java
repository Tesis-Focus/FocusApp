package com.example.focusappm;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Horario implements Serializable,Comparable,Cloneable {

    private String idBeneficiario;
    private String idHorario;
    private Date mStartTime;
    private Date mEndTime;
    private String mName;
    private int mColor;
    private boolean actualizado;


    public Horario(String idBeneficiario, String idHorario, Date mStartTime, Date mEndTime, String mName, int mColor, boolean actualizado) {
        this.idBeneficiario = idBeneficiario;
        this.idHorario = idHorario;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mName = mName;
        this.mColor = mColor;
        this.actualizado = actualizado;
    }

    public Horario() {
    }

    public String getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(String idHorario) {
        this.idHorario = idHorario;
    }

    public String getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(String idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
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

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public boolean isActualizado() {
        return actualizado;
    }

    public void setActualizado(boolean actualizado) {
        this.actualizado = actualizado;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "mId='" + getIdBeneficiario() + '\'' +
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
        startTime.set(Calendar.MONTH,mStartTime.getMonth());
        startTime.set(Calendar.YEAR,mStartTime.getYear());

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, mEndTime.getHours());
        endTime.set(Calendar.DAY_OF_MONTH,mEndTime.getDate());
        endTime.set(Calendar.MINUTE, mEndTime.getMinutes());
        endTime.set(Calendar.MONTH,mEndTime.getMonth());
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
            return 1;
        else
            return -1;

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Horario nuevo = new Horario();
        nuevo.setIdHorario("");
        nuevo.setmEndTime((Date) this.mEndTime.clone());
        nuevo.setIdBeneficiario(this.getIdBeneficiario());
        nuevo.setmStartTime((Date) this.mStartTime.clone());
        nuevo.setmName("");
        return nuevo;
    }
}
