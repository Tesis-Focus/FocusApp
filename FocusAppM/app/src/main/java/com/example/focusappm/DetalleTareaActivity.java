package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetalleTareaActivity extends AppCompatActivity {

    Tarea tarea;
    EditText nombre;
    EditText descripcion;
    EditText fechaEntrega;
    EditText motivacion;
    EditText complejidad;
    EditText clasificacion;
    EditText area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        tarea = (Tarea) getIntent().getSerializableExtra("Tarea");

        nombre = findViewById(R.id.edtxNombreTarea);
        descripcion = findViewById(R.id.edtxtDescripcionTarea);
        fechaEntrega = findViewById(R.id.edtxFechaEntrega);
        motivacion = findViewById(R.id.edttxtMotivacion);
        complejidad = findViewById(R.id.edttxtComplejidad);
        clasificacion = findViewById(R.id.edttxtClasificacion);
        area = findViewById(R.id.edttxtArea);

        nombre.setText(tarea.getNombre());
        descripcion.setText(tarea.getDescripcion());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        tarea.getFechaEntrega().setMonth(tarea.getFechaEntrega().getMonth()-1);
        tarea.getFechaEntrega().setYear(tarea.getFechaEntrega().getYear()-1900);
        String fechEntrega = df.format(tarea.getFechaEntrega());
        fechaEntrega.setText(fechEntrega);
        if(tarea.isEstaMotivado()){
            motivacion.setText("Si");
        }else{
            motivacion.setText("No");
        }
        complejidad.setText(tarea.getComplejidad());
        clasificacion.setText(tarea.getClasificacion());
        area.setText(tarea.getArea());

    }
}
