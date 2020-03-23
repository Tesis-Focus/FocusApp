package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AgregarTareaActivity extends AppCompatActivity {

    EditText txtFechaEntrega;
    ImageButton btnFechaEntrega;
    EditText txtNombTarea;
    EditText txtDescripTarea;
    EditText txtTemaTarea;
    Spinner sprComplejidad;
    Spinner sprClasificacion;
    Spinner sprArea;
    Spinner sprActividad;
    Button btnGuardarTarea;
    Calendar calendario;
    DatePickerDialog datePickerDialog;
    private String complej;
    private String clasif;
    private String activ;
    private String area;


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
        sprClasificacion = findViewById(R.id.sprClasificacion);
        sprArea = findViewById(R.id.sprArea);
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

        sprComplejidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                complej = (String) sprComplejidad.getSelectedItem();
                Toast.makeText(getBaseContext(), "Item: " + sprComplejidad.getSelectedItem() + " " + complej,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<CharSequence> adapterClasif = ArrayAdapter.createFromResource(this, R.array.Clasificacion, android.R.layout.simple_spinner_item);
        sprClasificacion.setAdapter(adapterClasif);

        sprClasificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                clasif = (String) sprClasificacion.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterActiv = ArrayAdapter.createFromResource(this, R.array.Bajo_Medio_Alto, android.R.layout.simple_spinner_item);
        sprActividad.setAdapter(adapterActiv);

        sprActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                activ = (String) sprActividad.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterArea = ArrayAdapter.createFromResource(this, R.array.Area, android.R.layout.simple_spinner_item);
        sprArea.setAdapter(adapterArea);

        sprArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = (String) sprArea.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnGuardarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tarea tarea = new Tarea();
                tarea.setNombre(txtNombTarea.getText().toString());
                tarea.setDescripcion(txtDescripTarea.getText().toString());
                tarea.setTema(txtTemaTarea.getText().toString());
                tarea.setComplejidad(complej);
                System.out.println("Complejidad" + complej);
                tarea.setClasificacion(clasif);
                System.out.println("Clasificacion" + clasif);
                tarea.setArea(area);
                tarea.setFechaInicio("00/00/0000");
                tarea.setFechaAsignacion("00/00/0000");  //El dia que ingresa la tarea
                tarea.setFechaEntrega(txtFechaEntrega.getText().toString());
                tarea.setFechaFinalizacion("00/00/0000");
                //Log.i("Nombre",txtNombTarea.getText().toString());
            }
        });

    }
}
