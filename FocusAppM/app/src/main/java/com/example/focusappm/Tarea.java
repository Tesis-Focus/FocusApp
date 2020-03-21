package com.example.focusappm;

import java.util.Date;

public class Tarea {

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
    private Actividad actividad;
    private CalificacionPlanificacion calificacion;

    public Tarea(String nombre, String descripcion, String tema, String complejidad, String area, String clasificacion, Date fechaAsignacion, Date fechaInicio, Date fechaFinalizacion, Date fechaEntrega, Actividad actividad, CalificacionPlanificacion calificacion){

        this.nombre= nombre;
        this.descripcion = descripcion;
        this.tema = tema;
        this.complejidad = complejidad;
        this.area = area;
        this.clasificacion = clasificacion;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.fechaEntrega = fechaEntrega;
        this.actividad = actividad;
        this.calificacion = calificacion;
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

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public CalificacionPlanificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(CalificacionPlanificacion calificacion) {
        this.calificacion = calificacion;
    }
}
