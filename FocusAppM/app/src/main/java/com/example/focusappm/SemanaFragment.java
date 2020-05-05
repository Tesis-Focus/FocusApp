package com.example.focusappm;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class SemanaFragment extends Fragment {


    public final static String PATH_TAREAS = "tareas/";
    public SemanaFragment() {
        // Required empty public constructor
    }

    WeekView mWeekView;
    ArrayList<WeekViewEvent> eventos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_semana,container,false);
        mWeekView = (WeekView)rootView.findViewById(R.id.calendarHome);

        eventos = new ArrayList<WeekViewEvent>();
        obtenerHorarios();


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
                Log.i("PRUEBA", "onMonthChange: LLEGA");
                return nuevosEventos;
            }
        });




        return rootView;

    }

    private void obtenerHorarios() {
        Bundle b = getArguments();
        String idUsuario;
        Log.i("calendario", "obtenerHorarios: " + b.getString("idBeneficiario"));

        if(b != null){
            idUsuario = b.getString("idBeneficiario");
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myRef = db.getReference();
            myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Tarea> tareas = new ArrayList<>();
                    ArrayList<Horario> horarios = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Tarea tarea = ds.getValue(Tarea.class);
                        if(tarea.getIdBeneficiario().equals(idUsuario)){
                            for(Horario horario : tarea.getHorarios()){
                                eventos.add(horario.toWeekViewEvent());
                            }
                        }
                    }
                    mWeekView.notifyDatasetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

}
