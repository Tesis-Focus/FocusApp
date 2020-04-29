package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventosCalendarioActivity extends AppCompatActivity {

    int horaInicio;
    int minutoInicio;
    int horaFin;
    int minutoFin;
    int AM_PM_Inicio;
    int AM_PM_Fin;
    int diaDeMes;

    boolean isPmStart;
    boolean isPmEnd;

    ImageButton btnHoraInicio;
    ImageButton btnHoraFin;
    ImageButton btnFecha;
    Calendar calendario;
    EditText etHoraInicio;
    EditText etHoraFin;
    EditText etFecha;
    Button guardar;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_calendario);

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
                        horaInicio = hourOfDay;

                        String minutoForInicio = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);

                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                            AM_PM_Inicio = 0;
                            isPmStart = false;

                        } else {
                            hourOfDay= hourOfDay-12;
                            AM_PM = "PM";
                            AM_PM_Inicio = 1;
                            isPmStart = true;
                        }

                        etHoraInicio.setText(hourOfDay +":"+ minutoForInicio + " " + AM_PM);
                        minutoInicio = minute;

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
                        horaFin = hourOfDay;
                        String minutoForFin = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);

                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                            AM_PM_Fin = 0;
                            isPmEnd = false;
                        } else {
                            hourOfDay= hourOfDay-12;
                            AM_PM = "PM";
                            AM_PM_Fin = 1;
                            isPmEnd =true;
                        }
                        etHoraFin.setText(hourOfDay +":"+ minutoForFin  + " " + AM_PM);
                        minutoFin = minute;

                    }
                }, horaFin, minutoFin,false);
                timePickerDialog.show();

            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerFecha();
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

                Date startTime = null;
                Date endTime = null;

                try {
                    startTime = new SimpleDateFormat("dd/MM/yyyy").parse(etFecha.getText().toString());
                    endTime = new SimpleDateFormat("dd/MM/yyyy").parse(etFecha.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                startTime.setYear(startTime.getYear()+1900);
                startTime.setMonth(startTime.getMonth()+1);
                startTime.setHours(horaInicio);
                startTime.setMinutes(minutoInicio);

                endTime.setYear(endTime.getYear()+1900);
                endTime.setMonth(endTime.getMonth()+1);
                endTime.setHours(horaFin);
                endTime.setMinutes(minutoFin);


                Horario horario = new Horario();
                horario.setmName("Disponible");
                horario.setmColor(Color.CYAN);
                horario.setmStartTime(startTime);
                horario.setmEndTime(endTime);

                Intent i = new Intent();
                i.putExtra("horario",horario);
                setResult(RESULT_OK, i);
                finish();

            }
        });
    }

    private void obtenerFecha() {

        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);

                etFecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();

    }
}
