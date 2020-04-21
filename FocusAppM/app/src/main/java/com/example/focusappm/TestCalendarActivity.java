package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestCalendarActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    WeekView mWeekView;
    List <WeekViewEvent> eventosNuevos = new ArrayList<WeekViewEvent>();
    ImageButton agregarEvento;

    int nuevoAnio;
    int nuevoMes;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_HORARIOS = "horarioDisponible/";
    FirebaseAuth mAuth;
    FirebaseUser user;
    Usuario beneficiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_calendar_activity);

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        beneficiario = (Usuario) getIntent().getSerializableExtra("beneficiario");
        Log.i("cal", "onCreate: usuario" + beneficiario.getNombres());

        agregarEvento = findViewById(R.id.agregarEvento);
        mWeekView = findViewById(R.id.weekView3);



        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Log.i("calendar", "onEventClick: ");
            }
        });

        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> eventos = new ArrayList<WeekViewEvent>();
                nuevoAnio=newYear;
                nuevoMes=newMonth;
                cargarEventos();

                /////////////////////////////////////
                Log.i("calendar", "onMonthChange: ");
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth - 1);
                startTime.set(Calendar.YEAR, newYear);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 1);
                endTime.set(Calendar.MONTH, newMonth - 1);
                WeekViewEvent event = new WeekViewEvent(1, "Primer evento", startTime, endTime);
                event.setColor(R.color.colorPrimary);
                eventos.add(event);

                for (WeekViewEvent evento: eventosNuevos) {
                    Log.i("calendarNombre", evento.getName());
                    eventos.add(evento);
                }

                Log.i("calendarEventos", String.valueOf(eventos.size()));
                return eventos;
            }
        });



        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
                Log.i("calendar", "onEventLongPress: ");
            }
        });

        agregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),EventosCalendarioActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void cargarEventos() {
        myRef.child(PATH_HORARIOS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Horario actHorario = ds.getValue(Horario.class);
                    Log.i("Calendar", "cargaInial");

                  //  if(idBeneficiario.equalsIgnoreCase(actActual.getIdUsaurio())) {

                        //Log.i("TAG", "Iguales: " + idBeneficiario + actActual.getIdUsaurio());

                    String nombre = actHorario.getNombre();
                    int diaMes = actHorario.getDiaMes();
                    int horaInicio =  actHorario.getHoraInicio();
                    int minutoInicio = actHorario.getMinutoInicio();
                    int horaFin = actHorario.getHoraFin();
                    int minutoFin = actHorario.getMinutoFin();
                    int AM_PM_Inicio = actHorario.getAM_PM_Inicio();
                    int AM_PM_Fin = actHorario.getAM_PM_Fin();

                    Calendar startTime = Calendar.getInstance();

                    startTime.set(Calendar.HOUR, horaInicio);
                    startTime.set(Calendar.AM_PM, AM_PM_Inicio);
                    startTime.set(Calendar.MINUTE, minutoInicio);
                    startTime.set(Calendar.MONTH, nuevoMes );
                    startTime.set(Calendar.YEAR, nuevoAnio);
                    startTime.set(Calendar.DAY_OF_MONTH, diaMes);
                    // Calendar endTime = (Calendar) startTime.clone();
                    // endTime.add(Calendar.HOUR, 1);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(Calendar.MONTH, nuevoMes );
                    endTime.set(Calendar.HOUR, horaFin);
                    endTime.set(Calendar.AM_PM, AM_PM_Fin);
                    endTime.set(Calendar.MINUTE, minutoFin);
                    endTime.set(Calendar.YEAR, nuevoAnio);
                    endTime.set(Calendar.DAY_OF_MONTH, diaMes);

                    WeekViewEvent event = new WeekViewEvent(2, nombre, startTime, endTime);
                    eventosNuevos.add(event);
                    Log.i("calendar", String.valueOf(eventosNuevos.size()));

                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){

                nuevoMes = nuevoMes - 2;

                String nombreEvento = data.getStringExtra("nombre");
                int diaMes = data.getIntExtra("diaMes",0);
                int horaInicio =  data.getIntExtra("horaInicio",0);
                int minutoInicio = data.getIntExtra("minutoInicio",0);
                int horaFin = data.getIntExtra("horaFin",0);
                int minutoFin = data.getIntExtra("minutoFin",0);
                int AM_PM_Inicio = data.getIntExtra("AM_PM_Inicio",0);
                int AM_PM_Fin = data.getIntExtra("AM_PM_Fin",0);

                Log.i("calendarHora", String.valueOf(horaInicio));
                Log.i("calendarMinuto", String.valueOf(minutoInicio));
                Log.i("calendarAMPM", String.valueOf(AM_PM_Inicio));

                Log.i("calendar", "onMonthChangeAdd: ");
                Calendar startTime = Calendar.getInstance();

                startTime.set(Calendar.HOUR, horaInicio);
                startTime.set(Calendar.AM_PM, AM_PM_Inicio);
                startTime.set(Calendar.MINUTE, minutoInicio);
                startTime.set(Calendar.MONTH, nuevoMes );
                startTime.set(Calendar.YEAR, nuevoAnio);
                startTime.set(Calendar.DAY_OF_MONTH, diaMes);
                // Calendar endTime = (Calendar) startTime.clone();
                // endTime.add(Calendar.HOUR, 1);
                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.MONTH, nuevoMes );
                endTime.set(Calendar.HOUR, horaFin);
                endTime.set(Calendar.AM_PM, AM_PM_Fin);
                endTime.set(Calendar.MINUTE, minutoFin);
                endTime.set(Calendar.YEAR, nuevoAnio);
                endTime.set(Calendar.DAY_OF_MONTH, diaMes);
                WeekViewEvent event = new WeekViewEvent(2, nombreEvento, startTime, endTime);
                event.setColor(R.color.colorAccent);

                Horario horario = new Horario();
                horario.setNombre(nombreEvento);
                horario.setDiaMes(diaMes);
                horario.setHoraInicio(horaInicio);
                horario.setMinutoInicio(minutoInicio);
                horario.setAM_PM_Inicio(AM_PM_Inicio);
                horario.setHoraFin(horaFin);
                horario.setMinutoFin(minutoFin);
                horario.setAM_PM_Fin(AM_PM_Fin);

                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String key = myRef.push().getKey();
                //Log.i("MyAPP", myRef.getKey());
                myRef = database.getReference(PATH_HORARIOS+key);
                myRef.setValue(horario);
                Toast.makeText(getApplicationContext(),"Persistencia hecha", Toast.LENGTH_LONG).show();
                Log.i("calendar", "persistencia");

                eventosNuevos.add(event);
                mWeekView.notifyDatasetChanged();

            }else if (resultCode == RESULT_CANCELED){

            }
        }
    }

}
