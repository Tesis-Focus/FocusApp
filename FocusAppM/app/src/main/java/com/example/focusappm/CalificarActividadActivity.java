package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalificarActividadActivity extends AppCompatActivity {

    RadioButton radioFaltoTiempo;
    RadioButton radioTiempoAdecuado;
    RadioButton radioSobroTiempo;
    Button btnCalificar;
    TextView txtCalificacion;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    public static final String PATH_ACTIVIDADES = "actividades/";
    public static final String PATH_TAREAS = "tareas/";

    Actividad actividad;
    double multiplicador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_actividad);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        actividad = (Actividad) getIntent().getSerializableExtra("Actividad");

        radioFaltoTiempo = findViewById(R.id.radioFaltoTiempo);
        radioTiempoAdecuado = findViewById(R.id.radioTiempoAdecuado);
        radioSobroTiempo = findViewById(R.id.radioSobraTiempo);
        btnCalificar = findViewById(R.id.btnCalificar);
        txtCalificacion = findViewById(R.id.txtCalificacion);


        radioFaltoTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCalificacion.setError(null);
                multiplicador = 0.05;
            }
        });
        radioSobroTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCalificacion.setError(null);
                multiplicador = -0.05;
            }
        });
        radioTiempoAdecuado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCalificacion.setError(null);
                multiplicador = 0;
            }
        });


        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    actividad.getFechaInicio().setYear(actividad.getFechaInicio().getYear()+1900);
                    actividad.getFechaFinal().setYear(actividad.getFechaFinal().getYear()+1900);
                    actividad.setMultiplicador(actividad.getMultiplicador()+multiplicador);
                    myRef = FirebaseDatabase.getInstance().getReference().child("");
                    myRef = database.getReference(PATH_ACTIVIDADES+actividad.getIdActividad());
                    myRef.setValue(actividad);

                    actualizarTareas();

                    Intent intent = new Intent(getBaseContext(),DetalleActividadActivity.class);
                    intent.putExtra("Actividad", actividad);
                    startActivity(intent);                }
            }
        });
    }

    private void actualizarTareas() {
        myRef = FirebaseDatabase.getInstance().getReference().child("");
        myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Tarea tarea = ds.getValue(Tarea.class);
                    if(actividad.getIdActividad().equalsIgnoreCase(tarea.getIdActividad())) {
                        tarea.setMultiplicador(actividad.getMultiplicador());
                        myRef = FirebaseDatabase.getInstance().getReference().child("");
                        myRef = database.getReference(PATH_TAREAS+tarea.getIdTarea());
                        myRef.setValue(tarea);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validarCampos() {
        boolean esValido=true;
        if(!radioFaltoTiempo.isChecked() && !radioSobroTiempo.isChecked() && !radioTiempoAdecuado.isChecked()){
            esValido = false;
            txtCalificacion.setError("");
        }

        return esValido;
    }
}
