package com.example.focusappm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDisponibleActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";
    ImageButton agregarHorarioDisponible;
    WeekView mWeekView;
    Usuario beneficiario;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<WeekViewEvent> newEvents,eventsYM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_disponible);

        beneficiario = (Usuario) getIntent().getSerializableExtra("beneficiario");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        agregarHorarioDisponible = findViewById(R.id.agregarHorarioDisponible);
        mWeekView = findViewById(R.id.weekViewCal);
        newEvents = new ArrayList<>();
        eventsYM = new ArrayList<>();



        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Log.i("cal", "onEventClick: "+event.getName());
            }
        });

        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

                List<Horario> horarios = (ArrayList) getIntent().getSerializableExtra("eventos");
                if(!newEvents.isEmpty()){
                    for(WeekViewEvent newEvent : newEvents){
                        if(newEvent != null && newEvent.getStartTime().get(Calendar.YEAR) == newYear && newEvent.getStartTime().get(Calendar.MONTH) == newMonth-1){
                            eventsYM.add(newEvent);
                        }
                    }
                    newEvents.clear();
                } else{
                    eventsYM.clear();
                    for(Horario horario : horarios) {
                        if (horario.getmStartTime().getYear() == newYear && horario.getmStartTime().getMonth() == newMonth - 1) {
                            eventsYM.add(horario.toWeekViewEvent());
                            Log.i("cal", "\n anio: \t" + horario.toWeekViewEvent().getStartTime().get(Calendar.YEAR)
                                    + "\n mes: \t" + horario.toWeekViewEvent().getStartTime().get(Calendar.MONTH)
                                    + "\n dia mes: \t" + horario.toWeekViewEvent().getStartTime().get(Calendar.DAY_OF_MONTH)
                                    + "\n hora dia: \t" + horario.toWeekViewEvent().getStartTime().get(Calendar.HOUR_OF_DAY)
                                    + "\n minuto: \t" + horario.toWeekViewEvent().getStartTime().get(Calendar.MINUTE));
                        }
                    }
                }

                Log.i("cal", "lista tam " + eventsYM.size() + " year "+newYear+ " month "+newMonth);
                return (List<? extends WeekViewEvent>) eventsYM.clone();
            }
        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Log.i("cal", "onEmptyViewClicked: "+time.getTime().toString());
            }
        });

        agregarHorarioDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),EventosCalendarioActivity.class);
                i.putExtra("codigo",0);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                ArrayList<Horario> horarios = (ArrayList<Horario>) data.getSerializableExtra("horarios");
                for(Horario horario : horarios){
                    horario.setIdBeneficiario(beneficiario.getIdBeneficiario());
                    String idHorario = myRef.push().getKey();
                    horario.setIdHorario(idHorario);
                    myRef = database.getReference(PATH_HORARIO_DISPONIBLE+idHorario);
                    myRef.setValue(horario);
                    newEvents.add(horario.toWeekViewEvent());
                }
                mWeekView.notifyDatasetChanged();
            }
        }
    }

}
