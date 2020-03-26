package com.example.focusappm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Actividad {
    private String idActividad;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String motivacion;
    private String desempeño;
    private String fechaInicio;
    private String fechaFinal;
    private Boolean horarioFijo;
    private String idUsaurio;
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

    public String getMotivacion() {
        return motivacion;
    }

    public void setMotivacion(String motivacion) {
        this.motivacion = motivacion;
    }

    public String getDesempeño() {
        return desempeño;
    }

    public void setDesempeño(String desempeño) {
        this.desempeño = desempeño;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Boolean getHorarioFijo() {
        return horarioFijo;
    }

    public void setHorarioFijo(Boolean horarioFijo) {
        this.horarioFijo = horarioFijo;
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
}
