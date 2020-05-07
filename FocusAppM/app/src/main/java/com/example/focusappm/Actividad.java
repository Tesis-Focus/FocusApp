package com.example.focusappm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Actividad implements Serializable {
    private String idActividad;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String desempeño;
    private String asignatura;
    private Date fechaInicio;
    private Date fechaFinal;
    private String idUsaurio;
    private int color;
    private List<String> areas = new ArrayList<String>();
    ArrayList<Horario> horarios;

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private List<String> idTareas = new ArrayList<String>();

    public Actividad() {

    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDesempeño() {
        return desempeño;
    }

    public void setDesempeño(String desempeño) {
        this.desempeño = desempeño;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getIdUsaurio() {
        return idUsaurio;
    }

    public void setIdUsaurio(String idUsaurio) {
        this.idUsaurio = idUsaurio;
    }

    public List<String> getIdTareas() {
        return idTareas;
    }

    public void setIdTareas(List<String> idTareas) {
        this.idTareas = idTareas;
    }

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
}
