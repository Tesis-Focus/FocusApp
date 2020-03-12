package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Calendar;

public class AgregarTareaActivity extends AppCompatActivity {

    EditText txtFechaEntrega;
    ImageButton btnFechaEntrega;
    EditText txtNombTarea;
    EditText txtDescripTarea;
    EditText txtTemaTarea;
    Spinner sprComplejidad;
    Spinner sprActividad;
    Button btnGuardarTarea;
    Calendar calendario;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        txtFechaEntrega = findViewById(R.id.txtFechaEntrega);
        btnFechaEntrega = findViewById(R.id.btnFechaEntrega);
        txtNombTarea = findViewById(R.id.txtNombTarea);
        txtDescripTarea = findViewById(R.id.txtDescripTarea);
        txtTemaTarea = findViewById(R.id.txtTemaTarea);
        sprComplejidad = findViewById(R.id.sprComplejidad);
        sprActividad = findViewById(R.id.sprActividad);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);

        btnFechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AgregarTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        txtFechaEntrega.setText(dayOfMonth+"/"+ (month+1) +"/" + year);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapterComplej = ArrayAdapter.createFromResource(this, R.array.Complejidad, android.R.layout.simple_spinner_item);
        sprComplejidad.setAdapter(adapterComplej);

        ArrayAdapter<CharSequence> adapterActiv = ArrayAdapter.createFromResource(this, R.array.Actividad, android.R.layout.simple_spinner_item);
        sprActividad.setAdapter(adapterActiv);

        /*
        btnGuardarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMain3 = new Intent(v.getContext(), Main3Activity.class);
                startActivity(intentMain3);
            }
        });

         */

    }
}
