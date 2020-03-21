package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Calendar;

public class VisualizarTareasActivity extends AppCompatActivity {

    ImageButton btnFechaTarea;
    EditText txtFecha;
    Calendar calendario;
    DatePickerDialog datePickerDialog;
    ListView listaTareas;
    Button btnAgrTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_tareas);

        btnFechaTarea = findViewById(R.id.btnFechaTarea);
        txtFecha = findViewById(R.id.txtFecha);
        listaTareas = findViewById(R.id.listaTareas);

        btnFechaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(VisualizarTareasActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        txtFecha.setText(dayOfMonth+"/"+ (month+1) +"/" + year);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });

        btnAgrTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AgregarTareaActivity.class);
                startActivity(intent);
            }
        });

    }
}
