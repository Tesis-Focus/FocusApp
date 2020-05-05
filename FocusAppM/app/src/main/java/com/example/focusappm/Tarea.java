package com.example.focusappm;
import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tarea extends WeekViewEvent implements Comparable, Serializable {

    private String idTarea;
    private String nombre;
    private String descripcion;
    private String tema;
    private String complejidad;
    private String clasificacion;
    private Date fechaAsignacion;
    private Date fechaEntrega;
    private String idActividad;
    private String idBeneficiario;
    private float tiempoPromedio;
    private boolean estaMotivado;
    private int prioridad;
    private List<Horario> horarios = new ArrayList<Horario>();
    private List<String> areas = new ArrayList<String>();

    public Tarea(){

    }

    public boolean isEstaMotivado() {
        return estaMotivado;
    }

    public void setEstaMotivado(boolean estaMotivado) {
        this.estaMotivado = estaMotivado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
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

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(String complejidad) {
        this.complejidad = complejidad;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public float getTiempoPromedio() {
        return tiempoPromedio;
    }

    public void setTiempoPromedio(float tiempoPromedio) {
        this.tiempoPromedio = tiempoPromedio;
    }

    public String getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(String idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    @Override
    public int compareTo(Object tarea) {
        int prioridadTarea = ((Tarea)tarea).getPrioridad();
        return this.prioridad - prioridadTarea;
    }
}

