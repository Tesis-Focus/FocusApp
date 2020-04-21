package com.example.focusappm;

import java.util.Calendar;
import java.util.Date;

public class Horario {
    private String nombre;
    private int diaMes;
    private int horaInicio;
    private int minutoInicio;
    private int AM_PM_Inicio;
    private int horaFin;
    private int minutoFin;
    private int AM_PM_Fin;
    private String idUsaurio;

    public Horario() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDiaMes() {
        return diaMes;
    }

    public void setDiaMes(int diaMes) {
        this.diaMes = diaMes;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(int minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public int getAM_PM_Inicio() {
        return AM_PM_Inicio;
    }

    public void setAM_PM_Inicio(int AM_PM_Inicio) {
        this.AM_PM_Inicio = AM_PM_Inicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getMinutoFin() {
        return minutoFin;
    }

    public void setMinutoFin(int minutoFin) {
        this.minutoFin = minutoFin;
    }

    public int getAM_PM_Fin() {
        return AM_PM_Fin;
    }

    public void setAM_PM_Fin(int AM_PM_Fin) {
        this.AM_PM_Fin = AM_PM_Fin;
    }

    public String getIdUsaurio() {
        return idUsaurio;
    }

    public void setIdUsaurio(String idUsaurio) {
        this.idUsaurio = idUsaurio;
    }
}
