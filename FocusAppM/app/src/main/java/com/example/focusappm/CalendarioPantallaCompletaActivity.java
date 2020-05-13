package com.example.focusappm;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

public class CalendarioPantallaCompletaActivity extends AppCompatActivity {

    public final static String PATH_TAREAS = "tareas/";
    public final static String PATH_ACTIVIDADES = "actividades/";
    public static int DIASVISIBLES = 3;

    WeekView mWeekView;
    ArrayList<WeekViewEvent> eventos;
    ImageButton imgbtnZoom,imgbtnNoZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_pantalla_completa);
        mWeekView = findViewById(R.id.calPantallaCom);
        imgbtnNoZoom = findViewById(R.id.imgBtnnoZoom2);
        imgbtnZoom = findViewById(R.id.imgBtnZoom2);
        eventos = new ArrayList<WeekViewEvent>();
        obtenerHorarios();

        imgbtnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIASVISIBLES = (DIASVISIBLES == 1 ? 1:DIASVISIBLES-1);
                mWeekView.setNumberOfVisibleDays(DIASVISIBLES);
            }
        });

        imgbtnNoZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIASVISIBLES = (DIASVISIBLES==7 ? 7 : DIASVISIBLES+1);
                mWeekView.setNumberOfVisibleDays(DIASVISIBLES);
            }
        });
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

                ArrayList<WeekViewEvent> nuevosEventos = new ArrayList<>();

                if(!eventos.isEmpty()){

                    for(WeekViewEvent wve : eventos){
                        if (wve.getStartTime().get(Calendar.YEAR) == newYear && wve.getStartTime().get(Calendar.MONTH)== newMonth - 1) {
                            nuevosEventos.add(wve);
                        }
                    }

                }
                Log.i("eventos", "onMonthChange: se agregaron para el mes "+ newMonth +" estos eventos "+nuevosEventos.size());
                return nuevosEventos;
            }
        });

    }
    private void obtenerHorarios() {
        Bundle b = getIntent().getExtras();
        String idBeneficiario;

        if(b != null){
            idBeneficiario = b.getString("idBeneficiario");
            ArrayList<String> nombreBeneficiarios = b.getStringArrayList("idsBeneficiarios");
            Log.i("Eventos", "obtenerHorarios: tam lista"+nombreBeneficiarios.size());
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myRef = db.getReference();
            myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Tarea tarea = ds.getValue(Tarea.class);
                        if(tarea.getIdBeneficiario().equals(idBeneficiario) || idBeneficiario.equals("Integrada") && nombreBeneficiarios.contains(tarea.getIdBeneficiario())){
                            for(Horario horario : tarea.getHorarios()){
                                WeekViewEvent e = horario.toWeekViewEvent();
                                e.setColor(tarea.getColor());
                                eventos.add(e);
                            }
                        }
                    }
                    Log.i("Eventos", "onDataChange: se encontraron eventos tareas"+eventos.size()+" para el usuario"+idBeneficiario);
                    mWeekView.notifyDatasetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            myRef.child(PATH_ACTIVIDADES).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Actividad actividad = ds.getValue(Actividad.class);
                        if(actividad.getIdUsaurio().equals(idBeneficiario)|| idBeneficiario.equals("Integrada") && nombreBeneficiarios.contains(actividad.getIdUsaurio())){
                            for(Horario h : actividad.getHorarios()){
                                WeekViewEvent e = h.toWeekViewEvent();
                                e.setColor(actividad.getColor());
                                e.setName(actividad.getNombre());
                                eventos.add(e);
                            }
                        }
                    }
                    Log.i("Eventos", "onDataChange: se encontraron eventos actividades"+eventos.size()+" para el usuario"+idBeneficiario);
                    mWeekView.notifyDatasetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }
}
