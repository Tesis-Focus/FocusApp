package com.example.focusappm;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class MesFragment extends Fragment {

    public MesFragment() {
        // Required empty public constructor
    }

    MaterialCalendarView mCustomCalendar;
    public final static String PATH_TAREAS = "tareas/";
    public final static String PATH_ACTIVIDADES = "actividades/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_semana,container,false);

        mCustomCalendar = rootView.findViewById(R.id.calendarView);
       // mCustomCalendar.state();


        return inflater.inflate(R.layout.fragment_mes, container, false);
    }
    private void obtenerHorarios() {
        Bundle b = getArguments();
        String idBeneficiario;
        Log.i("calendario", "obtenerHorarios: " + b.getString("idBeneficiario"));

        if(b != null){
            idBeneficiario = b.getString("idBeneficiario");
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myRef = db.getReference();
            myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashSet<CalendarDay> horarios = new HashSet<>();
                    int color = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Tarea tarea = ds.getValue(Tarea.class);
                        if(tarea.getIdBeneficiario().equals(idBeneficiario)){
                            for(Horario horario : tarea.getHorarios()){
                                CalendarDay day = CalendarDay.from(horario.getmStartTime().getYear(),horario.getmStartTime().getMonth(),horario.getmStartTime().getDate());
                                horarios.add(day);
                                color = tarea.getColor();
                            }
                        }
                    }
                    EventDecorator eventDecorator = new EventDecorator(color,horarios);
                    mCustomCalendar.addDecorator(eventDecorator);
                    Log.i("Eventos", "onDataChange: se encontraron eventos tareas"+horarios.size());
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
                        if(actividad.getIdUsaurio().equals(idBeneficiario)){
                            for(Horario h : actividad.getHorarios()){
                                //horarios.add(h);
                            }
                        }
                    }
                    //Log.i("Eventos", "onDataChange: se encontraron eventos actividades"+horarios.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

}
