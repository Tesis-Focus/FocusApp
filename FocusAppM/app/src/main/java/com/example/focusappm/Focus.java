package com.example.focusappm;

import java.util.ArrayList;
import java.util.List;

public class Focus {
    private List<Usuario> Usuarios = new ArrayList<Usuario>() ;

    public Focus( ) {

    }

    public List<Usuario> getUsuarios() {
        return Usuarios;
    }

    public void setIdUsuarios(List<Usuario> Usuarios) {
        this.Usuarios = Usuarios;
    }
}
