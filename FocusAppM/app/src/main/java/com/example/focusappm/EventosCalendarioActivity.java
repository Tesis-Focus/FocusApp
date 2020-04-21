package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class EventosCalendarioActivity extends AppCompatActivity {

    int horaInicio;
    int minutoInicio;
    int horaFin;
    int minutoFin;
    int AM_PM_Inicio;
    int AM_PM_Fin;
    int diaDeMes;

    ImageButton btnHoraInicio;
    ImageButton btnHoraFin;
    ImageButton btnFecha;
    Calendar calendario;
    EditText nombre;
    EditText etHoraInicio;
    EditText etHoraFin;
    EditText etFecha;
    Button guardar;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_calendario);

        nombre = findViewById(R.id.nombre);
        btnHoraInicio = findViewById(R.id.btnHoraInicio);
        btnHoraFin = findViewById(R.id.btnHoraFin);
        btnFecha = findViewById(R.id.btnFecha);
        etHoraInicio = findViewById(R.id.horaInicio);
        etHoraFin = findViewById(R.id.horaFin);
        etFecha = findViewById(R.id.fecha);
        guardar = findViewById(R.id.guardarEvento);

        btnHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendario = Calendar.getInstance();
                horaInicio = calendario.get(Calendar.HOUR_OF_DAY);
                minutoInicio = calendario.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventosCalendarioActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM ;
                        // minutoInicio = minute;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                            AM_PM_Inicio = 0;

                        } else {
                            hourOfDay= hourOfDay-12;
                            AM_PM = "PM";
                            AM_PM_Inicio = 1;
                        }

                        etHoraInicio.setText(hourOfDay +":"+ minute + " " + AM_PM);
                        minutoInicio = minute;
                        horaInicio = hourOfDay;
                    }
                }, horaInicio, minutoInicio,false);
                timePickerDialog.show();
            }
        });

        btnHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendario = Calendar.getInstance();
                horaFin = calendario.get(Calendar.HOUR_OF_DAY);
                minutoFin = calendario.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventosCalendarioActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                            AM_PM_Fin = 0;
                        } else {
                            hourOfDay= hourOfDay-12;
                            AM_PM = "PM";
                            AM_PM_Fin = 1;
                        }
                        etHoraFin.setText(hourOfDay +":"+ minute + " " + AM_PM);
                        minutoFin = minute;
                        horaFin = hourOfDay;
                    }
                }, horaFin, minutoFin,false);
                timePickerDialog.show();

            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(EventosCalendarioActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,anio,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                etFecha.setText(date);
                diaDeMes = dayOfMonth;

            }
        };

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("calendarAMPMInicio", String.valueOf(AM_PM_Inicio));
                Log.i("calendarAMPMFin", String.valueOf(AM_PM_Fin));
                Log.i("calendarDia", String.valueOf(diaDeMes));
                Intent intent = new Intent();
                intent.putExtra("nombre", nombre.getText().toString());
                intent.putExtra("diaMes", diaDeMes);
                intent.putExtra("horaInicio",horaInicio);
                intent.putExtra("minutoInicio",minutoInicio);
                intent.putExtra("AM_PM_Inicio", AM_PM_Inicio);
                intent.putExtra("AM_PM_Fin", AM_PM_Fin);
                intent.putExtra("horaFin",horaFin);
                intent.putExtra("minutoFin",minutoFin);
                // intent.putExtra("horaFin",etHoraFin.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
