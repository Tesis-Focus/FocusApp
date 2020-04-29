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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;
import static java.util.Calendar.getInstance;

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
    CheckBox chbxLun,chbxMar,chbxMie,chbxJue,chbxVie,chbxSab,chbxDom;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_calendario);

        chbxDom = findViewById(R.id.chbxDom);
        chbxLun = findViewById(R.id.chbxLun);
        chbxMar = findViewById(R.id.chbxMar);
        chbxMie = findViewById(R.id.chbxMier);
        chbxJue = findViewById(R.id.chbxJue);
        chbxVie = findViewById(R.id.chbxVie);
        chbxSab = findViewById(R.id.chbxSab);
        btnHoraInicio = findViewById(R.id.btnHoraInicio);
        btnHoraFin = findViewById(R.id.btnHoraFin);
        btnFecha = findViewById(R.id.btnFecha);
        etHoraInicio = findViewById(R.id.horaInicio);
        etHoraFin = findViewById(R.id.horaFin);
        etFecha = findViewById(R.id.fecha);
        guardar = findViewById(R.id.guardarEvento);
        Calendar cal = Calendar.getInstance();

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

                        etHoraInicio.setText(hourOfDay +":"+ minute + " " + AM_PM);
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
                        etHoraFin.setText(hourOfDay +":"+ minute + " " + AM_PM);
                        minutoFin = minute;

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
                Date dia = null;
                try {
                     dia = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(dia);
                chbxLun.setChecked(false);
                chbxMar.setChecked(false);
                chbxMie.setChecked(false);
                chbxJue.setChecked(false);
                chbxSab.setChecked(false);
                chbxDom.setChecked(false);
                chbxVie.setChecked(false);

                switch (cal.get(Calendar.DAY_OF_WEEK)){
                    case Calendar.SUNDAY: chbxDom.setChecked(true);
                        break;
                    case Calendar.MONDAY: chbxLun.setChecked(true);
                        break;
                    case Calendar.TUESDAY: chbxMar.setChecked(true);
                        break;
                    case Calendar.WEDNESDAY: chbxMie.setChecked(true);
                        break;
                    case Calendar.THURSDAY: chbxJue.setChecked(true);
                        break;
                    case Calendar.FRIDAY: chbxVie.setChecked(true);
                        break;
                    case Calendar.SATURDAY: chbxSab.setChecked(true);
                        break;
                }

            }
        };

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Date startTime = null;
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
                horario.setmEndTime(endTime);*/
                ArrayList<Horario> horarios = new ArrayList<>();
                horarios = obtenerHorarios();
                Intent i = new Intent();
                i.putExtra("horarios",horarios);
                setResult(RESULT_OK, i);
                finish();

            }
        });
    }

    private ArrayList<Horario> obtenerHorarios(){
        ArrayList<Horario> horarios = new ArrayList<>();
        Date fecha = null;
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(etFecha.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar fechaCal  = getInstance();
        fechaCal.setTime(fecha);
        switch (fechaCal.get(Calendar.DAY_OF_WEEK)){
            case MONDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(0));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(1));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(2));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(3));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(4));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(5));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(6));}
                break;
            case TUESDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(6));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(0));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(1));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(2));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(3));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(4));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(5));}
                break;
            case WEDNESDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(5));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(6));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(0));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(1));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(2));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(3));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(4));}
                break;
            case THURSDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(4));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(5));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(6));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(0));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(1));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(2));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(3));}
                break;
            case FRIDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(3));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(4));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(5));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(6));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(0));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(1));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(2));}
                break;
            case SATURDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(2));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(3));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(4));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(5));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(6));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(0));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(1));}
                break;
            case SUNDAY:
                if(chbxLun.isChecked()){horarios.add(crearHorario(1));}
                if(chbxMar.isChecked()){horarios.add(crearHorario(2));}
                if(chbxMie.isChecked()){horarios.add(crearHorario(3));}
                if(chbxJue.isChecked()){horarios.add(crearHorario(4));}
                if(chbxVie.isChecked()){horarios.add(crearHorario(5));}
                if(chbxSab.isChecked()){horarios.add(crearHorario(6));}
                if(chbxDom.isChecked()){horarios.add(crearHorario(0));}
                break;
        }
        return horarios;
    }

    private Horario crearHorario(int dias){
        Date startTime = null;
        Date endTime = null;

        try {
            startTime = new SimpleDateFormat("dd/MM/yyyy").parse(etFecha.getText().toString());
            endTime = new SimpleDateFormat("dd/MM/yyyy").parse(etFecha.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startTime.setYear(startTime.getYear()+1900);
        //startTime.setMonth(startTime.getMonth());
        startTime.setDate(startTime.getDate()+dias);
        startTime.setHours(horaInicio);
        startTime.setMinutes(minutoInicio);

        endTime.setYear(endTime.getYear()+1900);
        //endTime.setMonth(endTime.getMonth());
        endTime.setDate(endTime.getDate()+dias);
        endTime.setHours(horaFin);
        endTime.setMinutes(minutoFin);

        Log.i("otro", "\n fecha fin: \t"+endTime.getYear()
                +"\n mes: \t"+endTime.getMonth()
                +"\n dia mes: \t"+endTime.getDate()
                +"\n hora dia: \t"+endTime.getHours()
                +"\n minuto: \t"+ endTime.getMinutes());


        Horario horario = new Horario();
        horario.setmName("Disponible");
        horario.setmColor(Color.CYAN);
        horario.setmStartTime(startTime);
        horario.setmEndTime(endTime);
        return horario;
    }

}
