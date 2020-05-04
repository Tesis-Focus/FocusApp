package com.example.focusappm;

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

public class PlaneacionActivity extends AppCompatActivity {

    public static final String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";
    WeekView mWeekView;
    Usuario beneficiario;
    FirebaseDatabase database;
    DatabaseReference myRef;
    WeekViewEvent newEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planeacion);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mWeekView = findViewById(R.id.weekpla);
        newEvent = null;


        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Log.i("cal", "onEventClick: "+event.getName());
            }
        });

        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

                List<WeekViewEvent> eventsYM = new ArrayList<>();
                List<Horario> horarios = (ArrayList) getIntent().getSerializableExtra("eventos");
                if(newEvent != null && newEvent.getStartTime().get(Calendar.YEAR) == newYear && newEvent.getStartTime().get(Calendar.MONTH) == newMonth){
                    eventsYM.add(newEvent);
                }
                else{
                    eventsYM.clear();
                    for(Horario horario : horarios){
                        if(horario.getmStartTime().getYear() == newYear && horario.getmStartTime().getMonth() == newMonth){
                            eventsYM.add(horario.toWeekViewEvent());
                            Log.i("cal", "\n anio: \t"+horario.toWeekViewEvent().getStartTime().get(Calendar.YEAR)
                                    +"\n mes: \t"+horario.toWeekViewEvent().getStartTime().get(Calendar.MONTH)
                                    +"\n dia mes: \t"+horario.toWeekViewEvent().getStartTime().get(Calendar.DAY_OF_MONTH)
                                    +"\n hora dia: \t"+horario.toWeekViewEvent().getStartTime().get(Calendar.HOUR_OF_DAY)
                                    +"\n minuto: \t"+ horario.toWeekViewEvent().getStartTime().get(Calendar.MINUTE));
                        }
                    }
                }

                Log.i("cal", "lista tam " + eventsYM.size() + " year "+newYear+ " month "+newMonth);
                return eventsYM;
            }
        });
        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Log.i("cal", "onEmptyViewClicked: "+time.getTime().toString());
            }
        });
    }
}
