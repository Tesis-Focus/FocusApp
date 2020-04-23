package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_disponible);

        beneficiario = (Usuario) getIntent().getSerializableExtra("beneficiario");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        agregarHorarioDisponible = findViewById(R.id.agregarHorarioDisponible);
        mWeekView = findViewById(R.id.weekViewDispo);


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
                List<WeekViewEvent> eventsYM = new ArrayList<>();
                eventsYM.clear();
                for(Horario horario : horarios){
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY,horario.getmStartTime().getHours());
                    startTime.set(Calendar.MINUTE, horario.getmStartTime().getMinutes());
                    newMonth = horario.getmStartTime().getMonth();
                    startTime.set(Calendar.MONTH,newMonth);
                    newYear = horario.getmStartTime().getYear();
                    startTime.set(Calendar.YEAR,newYear);
                    startTime.set(Calendar.DAY_OF_MONTH, horario.getmStartTime().getDate());

                    Log.i("cal", "HOUR OF DAY start "+ horario.getmStartTime().getHours());
                    Log.i("cal", "MINUTE start "+ horario.getmStartTime().getMinutes());
                    Log.i("cal", "MONTH start "+ newMonth);
                    Log.i("cal", "DAY OF MONTH start "+ horario.getmStartTime().getDate());
                    Log.i("cal", "YEAR start "+ newYear);

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, horario.getmEndTime().getHours());
                    endTime.set(Calendar.MINUTE, horario.getmEndTime().getMinutes());

                    Log.i("cal", "HOUR OF DAY end "+ horario.getmEndTime().getHours());
                    Log.i("cal", "MINUTE end "+ horario.getmEndTime().getMinutes());
                    Log.i("cal", "MONTH end "+ horario.getmEndTime().getMonth());
                    Log.i("cal", "DAY OF MONTH end "+ horario.getmEndTime().getDate());
                    Log.i("cal", "YEAR end "+ horario.getmEndTime().getYear());

                    WeekViewEvent event = new WeekViewEvent(1,horario.getmName(),startTime,endTime);
                    event.setColor(Color.CYAN);
                    eventsYM.add(event);
                }

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, 3);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.MONTH, newMonth );
                startTime.set(Calendar.YEAR, newYear);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR, 1);
                endTime.set(Calendar.MONTH, newMonth );
                WeekViewEvent event = new WeekViewEvent(1, "disponible", startTime, endTime);
                event.setColor(Color.CYAN);
                eventsYM.add(event);

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

        agregarHorarioDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),EventosCalendarioActivity.class);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Horario horario = (Horario) data.getSerializableExtra("horario");
                horario.setmId(beneficiario.getIdBeneficiario());
                myRef = database.getReference(PATH_HORARIO_DISPONIBLE+myRef.push().getKey());
                myRef.setValue(horario);
            }
        }
    }

}
