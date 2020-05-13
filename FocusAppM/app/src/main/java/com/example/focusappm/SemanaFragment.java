package com.example.focusappm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
    public final static String PATH_ACTIVIDADES = "actividades/";
    public static int DIASVISIBLES = 2;
    public static String idBeneficiarioG="";
    IpantallaCompleta ipantallaCompletaListener;
    public SemanaFragment() {
        // Required empty public constructor
    }

    WeekView mWeekView;
    ImageButton imgbtnZoom,imgbtnNoZoom;
    Button btnPantallaCompleta;
    ArrayList<WeekViewEvent> eventos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_semana,container,false);
        mWeekView = (WeekView)rootView.findViewById(R.id.calendarHome);
        imgbtnNoZoom = rootView.findViewById(R.id.imgBtnnoZoom);
        imgbtnZoom = rootView.findViewById(R.id.imgBtnZoom);
        btnPantallaCompleta = rootView.findViewById(R.id.btnPantallaCompleta);


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

        btnPantallaCompleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipantallaCompletaListener.onPantallaCompletaClicked();
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

        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(event.getName());
                builder.setMessage("Fecha: "+event.getStartTime().get(Calendar.DATE)+"/"+event.getStartTime().get(Calendar.MONTH)+"/"+event.getStartTime().get(Calendar.YEAR)
                                    +"\nHora inicio \t"+event.getStartTime().get(Calendar.HOUR_OF_DAY)+":"+event.getStartTime().get(Calendar.MINUTE)
                                    +"\nHora fin \t"+event.getEndTime().get(Calendar.HOUR_OF_DAY)+":"+event.getEndTime().get(Calendar.MINUTE)
                                    +"\nRecuerda tomar descansos de 5 minutos cada 20 minutos de trabajo!");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        return rootView;

    }


    private void obtenerHorarios() {
        Bundle b = getArguments();
        String idBeneficiario;
        Log.i("calendario", "obtenerHorarios: " + b.getString("idBeneficiario"));

        if(b != null){
            idBeneficiario = b.getString("idBeneficiario");
            idBeneficiarioG = idBeneficiario;
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IpantallaCompleta){
            ipantallaCompletaListener = (IpantallaCompleta) context;
        }
    }
}
