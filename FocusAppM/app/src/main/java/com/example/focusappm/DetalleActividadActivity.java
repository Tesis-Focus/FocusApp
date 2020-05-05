package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetalleActividadActivity extends AppCompatActivity {

    Actividad actividad;
    EditText nombre;
    EditText descripcion;
    EditText tipo;
    EditText fechaInicio;
    EditText fechaFin;
    EditText desempeño;
    EditText horarioFijo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);

        actividad = (Actividad) getIntent().getSerializableExtra("Actividad");
        nombre = findViewById(R.id.edtxNombreActividad);
        descripcion = findViewById(R.id.edtxtDescripcion);
        tipo = findViewById(R.id.edtxTipo);
        fechaInicio = findViewById(R.id.edtxFechaInicio);
        fechaFin = findViewById(R.id.edttxtFechaFin);
        desempeño = findViewById(R.id.edttxtDesempeño);
        horarioFijo = findViewById(R.id.edttxtHorarioFijo);

        nombre.setText(actividad.getNombre());
        descripcion.setText(actividad.getDescripcion());
        tipo.setText((actividad.getTipo()));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        actividad.getFechaInicio().setMonth(actividad.getFechaInicio().getMonth()-1);
        actividad.getFechaInicio().setYear(actividad.getFechaInicio().getYear()-1900);
        String fechInicio = df.format(actividad.getFechaInicio());
        fechaInicio.setText(fechInicio);

        actividad.getFechaFinal().setMonth(actividad.getFechaFinal().getMonth()-1);
        actividad.getFechaFinal().setYear(actividad.getFechaFinal().getYear()-1900);
        String fechFinal = df.format(actividad.getFechaFinal());
        fechaFin.setText(fechFinal);

         desempeño.setText(actividad.getDesempeño());
        if(actividad.getHorarioFijo()){
            horarioFijo.setText("Si");
        }else{
            horarioFijo.setText("No");
        }

    }
}
