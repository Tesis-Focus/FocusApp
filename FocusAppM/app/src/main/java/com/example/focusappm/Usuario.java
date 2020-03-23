package com.example.focusappm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {
    private String id;
    private String nombres;
    private String apellidos;
    private Date fechaNacimiento;
    private String rol;
    private String email;
    private List<String> idActividades = new ArrayList<String>() ;

    public Usuario() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getIdActividades() { return idActividades;    }

    public void setIdActividades(List<String> idActividades) {  this.idActividades = idActividades;  }
}
