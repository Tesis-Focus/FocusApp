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
    private final static ArrayList<Horario> horarioDispoCambio = new ArrayList<>();
    private final static ArrayList<Horario> horariosDisponiblesBenefi = new ArrayList<>();
    private static float tiempoPromedio = -1;


    public static void planeacion(String idBeneficiario){

        getHorarios(idBeneficiario);
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();

        myRef = dataBase.getReference(PATH_TAREAS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    horarioxTarea.clear();
                    tiempoPromedio = tarea.getTiempoPromedio();
                    asignarHorariosTarea( tarea, idBeneficiario);//BIEN

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private static void getHorarios(String idBeneficiario) {
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();
        myRef = dataBase.getReference(PATH_HORARIO_DISPONIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Horario horario = ds.getValue(Horario.class);
                    if(horario.getmId().equals(idBeneficiario)){
                        horariosDisponiblesBenefi.add(horario);
                        Log.i("Planeacion", "idHorarioPrimer" +horario.getIdHorario());
                    }
                }
                Log.i("Planeacion", "tamañoDisponible" +horariosDisponiblesBenefi.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private static void asignarHorariosTarea(Tarea tarea, String idBeneficiario){
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();
        ArrayList<Horario> horarios = new ArrayList<>();
        Date fechaEntrega,fechaActual;
        fechaEntrega = tarea.getFechaEntrega();
        fechaActual = new Date();
        tarea.getHorarios().clear();

       /* myRef.child(PATH_HORARIO_DISPONIBLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean encontro;

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
                horarioDispoCambio.clear();

                if(diasxAsignar <= finalDiasParaEntrega){

                    for(int i=0; i<horarioxTarea.size() && diasxAsignar>0; i++){
                        if (tarea.getTiempoPromedio() > 60 ) {

                            asignarTiempos(horarioxTarea.get(i).get(0),tarea,60);
                            diasxAsignar -= 1;
                            //asignar franja de 60 m
                            //reducir horario disponible
                            //persistir


                        }else if(tarea.getTiempoPromedio() > 0){

                            asignarTiempos(horarioxTarea.get(i).get(0),tarea, (int) tarea.getTiempoPromedio());
                            diasxAsignar -=1 ;
                            //asignar tiempo total
                            //reducir horario disponible

                        }
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = dataBase.getReference();
                    myRef = database.getReference(PATH_TAREAS+tarea.getIdTarea());
                    myRef.setValue(tarea);
                    for(Horario hor : horarioDispoCambio){
                        myRef = database.getReference(PATH_HORARIO_DISPONIBLE+hor.getIdHorario());
                        myRef.setValue(hor);
                    }

                }else{
                    Log.i("Planeacion", "La tarea "+ tarea.getNombre()+" no se puede realizar la planeacion, faltan dias D:.");
                }
                Log.i("Planeacion", "listaHorario " + tarea.getHorarios().size());

           }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        boolean encontro = false;

                for (Horario horario: horariosDisponiblesBenefi){
                    encontro = false;
                    Log.i("Planeacion", "asignar" +horario.getIdHorario());
                    if(validarFecha(horario, fechaEntrega, fechaActual)){

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

                for(ArrayList<Horario> horarioArrayList : horarioxTarea){
                    Collections.sort(horarioArrayList);
                }

                Date hoy = new Date();
                float finalDiasParaEntrega = Math.abs(tarea.getFechaEntrega().getTime()-hoy.getTime());
                finalDiasParaEntrega = TimeUnit.DAYS.convert((long) finalDiasParaEntrega,TimeUnit.MILLISECONDS);

                Log.i("Planeacion", "tarea prioridad " + tarea.getPrioridad());

                int diasxAsignar = (int)Math.ceil(tarea.getTiempoPromedio() / 60);
                horarioDispoCambio.clear();

                if(diasxAsignar <= finalDiasParaEntrega){

                    for(int i=0; i<horarioxTarea.size() && diasxAsignar>0; i++){
                        Log.i("Planeacion", "antesDeTiempos" + horarioxTarea.get(i).get(0).getIdHorario());

                        if (tarea.getTiempoPromedio() > 60 ) {

                            asignarTiempos(horarioxTarea.get(i).get(0),tarea,60);
                            diasxAsignar -= 1;
                            //asignar franja de 60 m
                            //reducir horario disponible
                            //persistir


                        }else if(tarea.getTiempoPromedio() > 0){

                            asignarTiempos(horarioxTarea.get(i).get(0),tarea, (int) tarea.getTiempoPromedio());
                            diasxAsignar -=1 ;
                            //asignar tiempo total
                            //reducir horario disponible

                        }
                    }
                    tarea.setTiempoPromedio(tiempoPromedio);
                    myRef = dataBase.getReference(PATH_TAREAS+tarea.getIdTarea());
                    myRef.setValue(tarea);

                    for(int i=0; i<horarioDispoCambio.size(); i++){
                        Horario horarioDispo= horarioDispoCambio.get(i);
                        for(int j=0; j<horariosDisponiblesBenefi.size(); j++){
                            Horario horarioGeneral = horariosDisponiblesBenefi.get(j);

                            if(horarioGeneral.getIdHorario().equals(horarioDispo.getIdHorario())){
                                horariosDisponiblesBenefi.set(j,horarioDispo);
                            }
                        }
                    }

                }else{
                    Log.i("Planeacion", "La tarea "+ tarea.getNombre()+" no se puede realizar la planeacion, faltan dias D:.");
                }
                Log.i("Planeacion", "listaHorario " + tarea.getHorarios().size());

    }


    private static void asignarTiempos(Horario horarioDispo, Tarea tarea, int tiempoEnMin)  {

        Log.i("Planeacion", "asignarTiempo" +horarioDispo.getIdHorario());

        Horario horarioTarea = null;

        try {
            horarioTarea = (Horario)horarioDispo.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        Log.i("Planeacion", "asignarTiempoDesp" +horarioTarea.getIdHorario());

        tarea.setTiempoPromedio((int)tarea.getTiempoPromedio() - tiempoEnMin); // quitarle tiempo por realizar a la tarea
        Log.i("Planeacion", "horarioEndAntes " + horarioTarea.getmEndTime());
        Log.i("Planeacion", "idHorario " + horarioTarea.getIdHorario());
        horarioTarea.setmEndTime(horarioDispo.getmStartTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(horarioTarea.getmEndTime());
        cal.add(Calendar.MINUTE, tiempoEnMin); //ajustar tiempo de finalizacion del horario de realizacion
        horarioTarea.setmEndTime(cal.getTime());
        horarioTarea.setmName("Desarrollo: "+tarea.getNombre());
        horarioTarea.setmColor(tarea.getColor());
        horarioTarea.setIdHorario(horarioDispo.getIdHorario());

        Log.i("Planeacion", "tiempo " + tiempoEnMin);
        Log.i("Planeacion", "horarioStart " + horarioTarea.getmStartTime());
        Log.i("Planeacion", "horarioEnd " + horarioTarea.getmEndTime());
        Log.i("Planeacion", "horarioTarea " + tarea.getHorarios().size());
        tarea.getHorarios().add(horarioTarea);


        horarioDispo.setmStartTime(horarioTarea.getmEndTime());

        Log.i("Planeacion", "horarioStart disponible " + horarioDispo.getmStartTime());
        Log.i("Planeacion", "horarioEnd disponible" + horarioDispo.getmEndTime());


        horarioDispoCambio.add(horarioDispo);
        //Log.i("Planeacion", "listaHorarios " + tarea.getHorarios().size());


    }

    static boolean validarFecha(Horario horario, Date fechaEntrega, Date fechaAsignacion){

        if(horario.getmEndTime().before(fechaEntrega) && horario.getmStartTime().after(fechaAsignacion)) {
            return true;
        }
        return false;

    }

}
