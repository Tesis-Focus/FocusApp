package com.example.focusappm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class UtilsFocus {

    private final static String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";
    private final static String PATH_TAREAS = "tareas/";

    public static Tarea calcularHorarioPorTarea(Tarea tarea, String idBeneficiario){

        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();

        Date fechaEntrega = null, fechaAsignacion = null;
        float diasParaEntrega;

        fechaEntrega = tarea.getFechaEntrega();
        fechaAsignacion = tarea.getFechaAsignacion();

        diasParaEntrega = Math.abs(fechaEntrega.getTime() - fechaAsignacion.getTime());
        diasParaEntrega = TimeUnit.DAYS.convert((long) diasParaEntrega,TimeUnit.MILLISECONDS);
        Log.i("Planeacion", "Faltan "+diasParaEntrega+" dias para entregar");

        Date finalFechaEntrega = fechaEntrega;
        Date finalFechaAsignacion = fechaAsignacion;
        float finalDiasParaEntrega = diasParaEntrega;

        myRef.child(PATH_HORARIO_DISPONIBLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Horario> horarios = new ArrayList<Horario>();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Horario horario = ds.getValue(Horario.class);
                    if(horario.getmId().equals(idBeneficiario) && validarFecha(horario, finalFechaEntrega, finalFechaAsignacion)){
                        horarios.add(horario);
                    }

                }
                Log.i("Planeacion", "Lista de horarios disponibles" + horarios.size());

                Collections.sort(horarios);
                for(Horario h : horarios)
                    Log.i("Planeacion", "horario start " +h.getmStartTime().toString());
                planeacion(idBeneficiario, finalDiasParaEntrega, horarios);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return tarea;

    }

    static void planeacion(String idBeneficiario, float finalDiasParaEntrega, List<Horario> horariosDisponibles){
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();

        myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Tarea> tareas = new ArrayList<Tarea>();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Tarea tarea=ds.getValue(Tarea.class);
                    if(tarea.getIdBeneficiario().equals(idBeneficiario)){
                        tareas.add(tarea);
                    }
                }

                Collections.sort(tareas);
                Log.i("Planeacion", "Tamano tareas"+tareas.size());
                for(Tarea tarea: tareas){
                    Log.i("Planeacion", "tareasProrizadas " + tarea.getPrioridad());

                    int diasFaltantes = (int)Math.ceil(tarea.getTiempoPromedio() / 60);

                    if(diasFaltantes <= finalDiasParaEntrega){

                        for(int i=0; i<horariosDisponibles.size() && diasFaltantes>0; i++){
                            if (tarea.getTiempoPromedio() > 60 ) {

                                tarea = asignarTiempos(horariosDisponibles,tarea,i,60);
                                diasFaltantes -= 1;
                                //asignar franja de 60 m
                                //reducir horario disponible
                                //persistir


                            }else{
                                diasFaltantes -=1 ;
                                //asignar tiempo total
                                //reducir horario disponible

                                tarea = asignarTiempos(horariosDisponibles,tarea,i, (int) tarea.getTiempoPromedio());
                            }
                        }

                    }else{
                        Log.i("Planeacion", "La tarea "+ tarea.getNombre()+" no se puede realizar.");
                    }
                    Log.i("Planeacion", "listaHorario " + tarea.getHorarios().size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    static Tarea asignarTiempos(List<Horario> horariosDisponibles, Tarea tarea, int i, int tiempo){
        Horario horario = horariosDisponibles.get(i);
        horario.getmEndTime().setMinutes(horario.getmStartTime().getMinutes() + tiempo);
        tarea.setTiempoPromedio(tarea.getTiempoPromedio() - tiempo);
        horariosDisponibles.get(i).getmStartTime().setMinutes(horariosDisponibles.get(i).getmStartTime().getMinutes()+tiempo);
        tarea.getHorarios().add(horario);
        //Log.i("Planeacion", "listaHorarios " + tarea.getHorarios().size());

        return tarea;
    }

    static boolean validarFecha(Horario horario, Date fechaEntrega, Date fechaAsignacion){
        Log.i("Planeacion", "FechaEntrega " + fechaEntrega);
        Log.i("Planeacion", "fechaAsignacion " + fechaAsignacion);

        horario.getmStartTime().setMonth(horario.getmStartTime().getMonth()-1);
        horario.getmStartTime().setYear(horario.getmStartTime().getYear()-1900);
        horario.getmEndTime().setMonth(horario.getmEndTime().getMonth()-1);
        horario.getmEndTime().setYear(horario.getmEndTime().getYear()-1900);

        Log.i("Planeacion", "horarioStartTime " + horario.getmStartTime());
        Log.i("Planeacion", "horarioEndTime " + horario.getmEndTime());
        if(horario.getmEndTime().before(fechaEntrega) && horario.getmStartTime().after(fechaAsignacion)) {
            return true;
        }
        return false;
    }

}
