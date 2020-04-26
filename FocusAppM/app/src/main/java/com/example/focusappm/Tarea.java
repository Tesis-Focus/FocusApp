package com.example.focusappm;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Date;

public class Tarea extends WeekViewEvent implements Comparable{

    private String idTarea;
    private String nombre;
    private String descripcion;
    private String tema;
    private String complejidad;
    private String area;
    private String clasificacion;
    private Date fechaAsignacion;
    private Date fechaInicio;
    private Date fechaFinalizacion;
    private Date fechaEntrega;
    private String idActividad;
    private String idBeneficiario;
    private float tiempoPromedio;
    private boolean estaMotivado;
    private int prioridad;

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
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

    @Override
    public int compareTo(Object tarea) {
        int prioridadTarea = ((Tarea)tarea).getPrioridad();
        return this.prioridad - prioridadTarea;
    }
}

