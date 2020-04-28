package com.example.focusappm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseUser;
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
    private final static ArrayList<ArrayList<Horario>> horarioxTarea = new ArrayList<>();
    private final static ArrayList<Horario> horariosCambia = new ArrayList<>();

    public static void planeacion(String idBeneficiario){

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
                Log.i("Planeacion", "Tamano tareas "+tareas.size());//BIEN

                for(Tarea tarea: tareas){


                    getHorariosDisponibles( tarea, idBeneficiario);//BIEN

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private static void asignarTiempos(Horario horarioDispo, Tarea tarea, int tiempoEnMin)  {
        Horario horarioTarea = null;
        try {
            horarioTarea = (Horario)horarioDispo.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        horarioTarea.setmName(tarea.getName());
        horarioDispo.getmStartTime().setMinutes(horarioDispo.getmStartTime().getMinutes()+tiempoEnMin); //quitar tiempo a horario dispo
        tarea.setTiempoPromedio(tarea.getTiempoPromedio() - tiempoEnMin); // quitarle tiempo por realizar a la tarea
        horarioTarea.getmEndTime().setMinutes(horarioTarea.getmStartTime().getMinutes()+tiempoEnMin);//ajustar tiempo de finalizacion del horario de realizacion
        tarea.getHorarios().add(horarioTarea);
        //Log.i("Planeacion", "listaHorarios " + tarea.getHorarios().size());
    }

    private static void getHorariosDisponibles(Tarea tarea, String idBeneficiario){
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();
        Date fechaEntrega,fechaActual;
        fechaEntrega = tarea.getFechaEntrega();
        fechaActual = new Date();


        myRef.child(PATH_HORARIO_DISPONIBLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean encontro;
                horarioxTarea.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    encontro = false;
                    Horario horario = ds.getValue(Horario.class);
                    if(horario.getmId().equals(idBeneficiario) && validarFecha(horario, fechaEntrega, fechaActual)){
                        for(ArrayList<Horario> horarioArrayList : horarioxTarea){
                            if(horarioArrayList.get(0).getmStartTime().getDate() == horario.getmStartTime().getDate()){
                                horarioArrayList.add(horario);
                                encontro = true;
                                break;
                            }
                        }
                        if(!encontro){
                            ArrayList<Horario> listaNueva = new ArrayList<>();
                            listaNueva.add(horario);
                            horarioxTarea.add(listaNueva);
                        }
                    }
                }
                Log.i("Planeacion", "Lista de horarios disponibles" + horarioxTarea.size());

                for(ArrayList<Horario> horarioArrayList : horarioxTarea)
                    Collections.sort(horarioArrayList);

                Date hoy = new Date();
                float finalDiasParaEntrega = Math.abs(tarea.getFechaEntrega().getTime()-hoy.getTime());
                finalDiasParaEntrega = TimeUnit.DAYS.convert((long) finalDiasParaEntrega,TimeUnit.MILLISECONDS);

                Log.i("Planeacion", "tarea prioridad " + tarea.getPrioridad());

                int diasxAsignar = (int)Math.ceil(tarea.getTiempoPromedio() / 60);

                if(diasxAsignar <= finalDiasParaEntrega){

                    horariosCambia.clear();

                    for(int i=0; i<horarioxTarea.size() && diasxAsignar>0; i++){
                        if (tarea.getTiempoPromedio() > 60 ) {

                            asignarTiempos(horarioxTarea.get(i).get(0),tarea,60);
                            diasxAsignar -= 1;
                            horariosCambia.add(horarioxTarea.get(i).get(0));

                        }else{
                            diasxAsignar -=1 ;
                            asignarTiempos(horarioxTarea.get(i).get(0),tarea, (int) tarea.getTiempoPromedio());
                            horariosCambia.add(horarioxTarea.get(i).get(0));
                        }
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = dataBase.getReference();
                    myRef = database.getReference(PATH_TAREAS+tarea.getIdTarea());
                    myRef.setValue(tarea);


                    for(Horario horario : horariosCambia){
                        myRef = database.getReference(PATH_HORARIO_DISPONIBLE+horario.getIdHorario());
                        myRef.setValue(horario);
                    }


                }else{
                    Log.i("Planeacion", "La tarea "+ tarea.getNombre()+" no se puede realizar la planeacion, faltan dias D:.");
                }
                Log.i("Planeacion", "listaHorario " + tarea.getHorarios().size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    static boolean validarFecha(Horario horario, Date fechaEntrega, Date fechaAsignacion){

        horario.getmStartTime().setMonth(horario.getmStartTime().getMonth()-1);
        horario.getmStartTime().setYear(horario.getmStartTime().getYear()-1900);
        horario.getmEndTime().setMonth(horario.getmEndTime().getMonth()-1);
        horario.getmEndTime().setYear(horario.getmEndTime().getYear()-1900);

        if(horario.getmEndTime().before(fechaEntrega) && horario.getmStartTime().after(fechaAsignacion)) {
            return true;
        }
        return false;

    }

}
