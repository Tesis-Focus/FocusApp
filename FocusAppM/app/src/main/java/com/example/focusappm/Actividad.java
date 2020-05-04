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
    private boolean estaMotivado;
    private String desempeño;
    private Date fechaInicio;
    private Date fechaFinal;
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

    public boolean isEstaMotivado() {
        return estaMotivado;
    }

    public void setEstaMotivado(boolean estaMotivado) {
        this.estaMotivado = estaMotivado;
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
