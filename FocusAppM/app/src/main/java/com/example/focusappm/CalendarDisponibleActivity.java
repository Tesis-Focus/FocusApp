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
                List<WeekViewEvent> events = getEvents(newYear,newMonth);
                Log.i("cal", "onMonthChange: tam" + events.size());
                return events;
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

    private List<WeekViewEvent> getEvents( int newYear, int newMonth) {
        ArrayList eventsHorario = new ArrayList<>();
        ArrayList<Horario> horarioArrayList = new ArrayList<>();
        myRef = database.getReference(PATH_HORARIO_DISPONIBLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot sd : dataSnapshot.getChildren()){
                    Horario horario = sd.getValue(Horario.class);
                    int anio = horario.getmStartTime().getYear()+1900;
                    int mes = horario.getmStartTime().getMonth()+1;
                    if( anio == newYear && newMonth == mes && beneficiario.getIdBeneficiario().equals(horario.getmId())){
                        WeekViewEvent event = horario.toWeekViewEvent();
                        eventsHorario.add(event);
                    }
                }
                Log.i("cal", "onDataChange 2: "+eventsHorario.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.i("cal", "getEvents: " +eventsHorario.size());
        return eventsHorario;
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
