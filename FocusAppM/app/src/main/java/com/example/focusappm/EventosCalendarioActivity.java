package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
    ImageButton btnFechainicio;
    ImageButton btnFechaFin;
    Calendar calendario;
    EditText etHoraInicio;
    EditText etHoraFin;
    EditText etFechaInicio,edtxFechaFin;
    Button guardar;
    CheckBox chbxLun,chbxMar,chbxMie,chbxJue,chbxVie,chbxSab,chbxDom;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

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
        btnFechainicio = findViewById(R.id.btnFecha);
        btnFechaFin = findViewById(R.id.btnFechaFin);
        etHoraInicio = findViewById(R.id.horaInicio);
        etHoraFin = findViewById(R.id.horaFin);
        etFechaInicio = findViewById(R.id.fecha);
        edtxFechaFin = findViewById(R.id.edtxFechaFin);
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
                        etHoraFin.setText(hourOfDay +":"+ minutoForFin + " " + AM_PM);
                        minutoFin = minute;
                    }
                }, horaFin, minutoFin,false);
                timePickerDialog.show();

            }
        });

        btnFechainicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(etFechaInicio);
            }
        });

        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha(edtxFechaFin);
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                etFechaInicio.setText(date);
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
                Date fechaini = new Date(),fechafin = new Date();
                try {
                    fechaini = new SimpleDateFormat("dd/MM/yyyy").parse(etFechaInicio.getText().toString());
                    fechaini.setYear(fechaini.getYear()+1900);
                    fechafin = new SimpleDateFormat("dd/MM/yyyy").parse(edtxFechaFin.getText().toString());
                    fechafin.setYear(fechafin.getYear()+1900);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList<Horario> horarios;
                horarios = obtenerHorarios();
                Intent i = new Intent();
                i.putExtra("horarios",horarios);
                i.putExtra("fechaini", fechaini);
                i.putExtra("fechafin",fechafin);
                setResult(RESULT_OK, i);
                finish();

            }
        });
    }

    private ArrayList<Horario> obtenerHorarios(){
        ArrayList<Horario> horarios = new ArrayList<>();
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(etFechaInicio.getText().toString());
            fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(edtxFechaFin.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int semanas = Math.abs(fechaFin.getMonth()-fechaInicio.getMonth());
        int anios = fechaFin.getYear()-fechaInicio.getYear();
        int total_semanas = semanas+(anios*12);
        int numdias = 0;

        Date fechaciclo = (Date) fechaInicio.clone();

        while(fechaciclo.before(fechaFin)){

            Calendar fechaCal  = getInstance();
            fechaCal.setTime(fechaInicio);
            switch (fechaCal.get(Calendar.DAY_OF_WEEK)){
                case MONDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(0+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(1+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(2+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(3+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(4+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(5+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(6+numdias));}
                    break;
                case TUESDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(6+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(0+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(1+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(2+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(3+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(4+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(5+numdias));}
                    break;
                case WEDNESDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(5+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(6+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(0+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(1+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(2+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(3+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(4+numdias));}
                    break;
                case THURSDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(4+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(5+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(6+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(0+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(1+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(2+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(3+numdias));}
                    break;
                case FRIDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(3+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(4+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(5+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(6+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(0+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(1+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(2+numdias));}
                    break;
                case SATURDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(2+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(3+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(4+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(5+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(6+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(0+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(1+numdias));}
                    break;
                case SUNDAY:
                    if(chbxLun.isChecked()){horarios.add(crearHorario(1+numdias));}
                    if(chbxMar.isChecked()){horarios.add(crearHorario(2+numdias));}
                    if(chbxMie.isChecked()){horarios.add(crearHorario(3+numdias));}
                    if(chbxJue.isChecked()){horarios.add(crearHorario(4+numdias));}
                    if(chbxVie.isChecked()){horarios.add(crearHorario(5+numdias));}
                    if(chbxSab.isChecked()){horarios.add(crearHorario(6+numdias));}
                    if(chbxDom.isChecked()){horarios.add(crearHorario(0+numdias));}
                    break;
                }
                numdias+=7;
                fechaciclo.setDate(fechaciclo.getDate()+7);
            }
        return horarios;
    }

    private Horario crearHorario(int dias){
        Date startTime = null;
        Date endTime = null;

        try {
            startTime = new SimpleDateFormat("dd/MM/yyyy").parse(etFechaInicio.getText().toString());
            endTime = new SimpleDateFormat("dd/MM/yyyy").parse(etFechaInicio.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startTime.setYear(startTime.getYear()+1900);
        startTime.setMonth(startTime.getMonth());
        startTime.setDate(startTime.getDate()+dias);
        startTime.setHours(horaInicio);
        startTime.setMinutes(minutoInicio);

        endTime.setYear(endTime.getYear()+1900);
        endTime.setMonth(endTime.getMonth());
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

    private void obtenerFecha(EditText fechaTexto) {

        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);

                fechaTexto.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();

    }
}
