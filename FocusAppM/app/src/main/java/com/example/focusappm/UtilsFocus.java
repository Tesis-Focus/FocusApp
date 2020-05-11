package com.example.focusappm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UtilsFocus {

    private final static String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";
    private final static String PATH_TAREAS = "tareas/";
    private final static ArrayList<ArrayList<Horario>> horarioxTarea = new ArrayList<>();
    private final static ArrayList<Horario> horariosDisponiblesBenefi = new ArrayList<>();


    public static void planeacion(String idBeneficiario){
        horarioxTarea.clear();
        horariosDisponiblesBenefi.clear();
        getHorarios(idBeneficiario);

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
                    if(horario.getIdBeneficiario().equals(idBeneficiario)){
                        horario.getmStartTime().setYear(horario.getmStartTime().getYear()-1900);
                        horario.getmEndTime().setYear(horario.getmEndTime().getYear()-1900);
                        horariosDisponiblesBenefi.add(horario);
                        Log.i("Planeacion", "idHorarioPrimer" +horario.getIdHorario());
                    }
                }
                Log.i("OTRA", "tamañoDisponible" +horariosDisponiblesBenefi.size());
                Collections.sort(horariosDisponiblesBenefi);
                continuacionDePlaneacion(idBeneficiario);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void continuacionDePlaneacion(String idBeneficiario){
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

                planearTareas(tareas,idBeneficiario);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private static void planearTareas(List<Tarea> tareas,String idBeneficiario){
        for(Tarea t : tareas){
            horarioxTarea.clear();
            Date tareaEmpieza,tareaTermina;
            tareaEmpieza = t.getFechaAsignacion();
            tareaEmpieza.setYear(tareaEmpieza.getYear()-1900);
            tareaTermina = t.getFechaEntrega();
            tareaTermina.setYear(tareaTermina.getYear()-1900);
            boolean encontro = false;

            for(Horario h : horariosDisponiblesBenefi){
                encontro = false;
                if( h.getmEndTime().before(tareaTermina) && h.getmStartTime().after(tareaEmpieza)){
                    Log.i("JUANP", "planearTareas: start "+h.getmStartTime()+" end "+h.getmEndTime());
                    for(ArrayList<Horario> horarioArray : horarioxTarea){
                        if(horarioArray.get(0).getmStartTime().getDate() == h.getmStartTime().getDate() && horarioArray.get(0).getmStartTime().getMonth() == h.getmStartTime().getMonth()){
                            horarioArray.add(h);
                            encontro = true;
                            break;
                        }
                    }
                    if(!encontro){
                        ArrayList<Horario> nuevaList = new ArrayList<>();
                        nuevaList.add(h);
                        horarioxTarea.add(nuevaList);
                    }
                }
            }

            int tiempoRealizacion = (int) ( t.getTiempoPromedio() + (t.getTiempoPromedio()%20) *5);
            int tiempoDisponible = 0 ;
            for(ArrayList<Horario> hList : horarioxTarea){
                for( Horario h: hList){
                    //Horario h = hList.get(0);
                    tiempoDisponible += (int) ((h.getmEndTime().getTime() - h.getmStartTime().getTime())/( 60*1000 ));
                    Log.i("JUANP", "planearTareas: "+tiempoDisponible);
                }
            }
            Log.i("JUANP", "planearTareas: tiempomindisponible "+tiempoDisponible);
            if(tiempoDisponible>tiempoRealizacion){

                planeacionDeTarea(t, tiempoRealizacion,idBeneficiario,false);

            }else{

                planeacionDeTarea(t, tiempoRealizacion,idBeneficiario,true);
            }
        }
    }

   private static void planeacionDeTarea(Tarea tarea, int tiempoTarea, String idBeneficiario,boolean sinTiempo){

        List<Horario> horariosTarea = new ArrayList<>();
        tiempoTarea = asignarHorarios(tarea,tiempoTarea,idBeneficiario,horariosTarea,sinTiempo);

        if (tiempoTarea > 0 && horariosTarea.size() > 0){
            tiempoTarea = asignarHorarios(tarea,tiempoTarea,idBeneficiario,horariosTarea,sinTiempo);
        }

        ArrayList<Horario> horariosEliminados = new ArrayList<>();
       for(Horario horarioDisponible : horariosDisponiblesBenefi){
           for(Horario horarioTarea : horariosTarea){
               if( horarioDisponible.getIdHorario().equals(horarioTarea.getIdHorario())){
                   Date endTime = (Date) horarioTarea.getmEndTime().clone();
                   endTime.setYear(endTime.getYear()-1900);
                   if(endTime.getTime()==horarioDisponible.getmEndTime().getTime()){
                       horariosEliminados.add(horarioDisponible);
                       Log.i("JUANP", "planearTareas: tiempomindisponible entra");
                   }else{
                       horarioDisponible.setmStartTime(endTime);
                   }
               }
           }
       }
       for(Horario h : horariosEliminados)
           horariosDisponiblesBenefi.remove(h);

       tarea.setHorarios(horariosTarea);
       tarea.getFechaEntrega().setYear(tarea.getFechaEntrega().getYear()+1900);
       tarea.getFechaAsignacion().setYear(tarea.getFechaAsignacion().getYear()+1900);
       FirebaseDatabase db = FirebaseDatabase.getInstance();
       DatabaseReference myRef = db.getReference(PATH_TAREAS+ tarea.getIdTarea());
       myRef.setValue(tarea);

   }

   public static  int  asignarHorarios(Tarea tarea, int tiempoTarea, String idBeneficiario, List<Horario> horariosTarea, boolean sinTiempo){
        if(sinTiempo){
            Collections.reverse(horarioxTarea);
        }

       for(ArrayList<Horario> horariosDia: horarioxTarea){
           if(tiempoTarea <= 0){break;}
          // Log.i("JUANP", "horario por asignar Start: "+ horariosDia.get(0).getmStartTime() +" end "+horariosDia.get(0).getmEndTime());
           //Horario franjaHorario = horariosDia.get(0);
            for(Horario franjaHorario : horariosDia){
                int tiempoMinutosFranja = (int) ((franjaHorario.getmEndTime().getTime() - franjaHorario.getmStartTime().getTime()) /(60 * 1000));
                if(tiempoMinutosFranja==0){continue;}
                Log.i("OTRA", "asignarHorarios: minFranja "+tiempoMinutosFranja+" mintiempoTarea "+tiempoTarea);
                Date startTime = null,endTime = null;
                if(tiempoTarea >= 75 && tiempoMinutosFranja >= 75){

                    tiempoTarea-= 75;
                    startTime = (Date) franjaHorario.getmStartTime().clone();
                    franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(75));
                    endTime = (Date) franjaHorario.getmStartTime().clone();

                }else if( tiempoTarea >= 75 && tiempoMinutosFranja <= 75){

                    tiempoTarea-= tiempoMinutosFranja;
                    startTime = (Date) franjaHorario.getmStartTime().clone();
                    endTime = (Date) franjaHorario.getmEndTime().clone();
                    horariosDia.remove(franjaHorario);


                }else if(tiempoTarea <= 75 && tiempoMinutosFranja >= 75){

                    int tiempoTarea2 = tiempoTarea;
                    tiempoTarea -= tiempoTarea;
                    startTime = (Date) franjaHorario.getmStartTime().clone();
                    franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(tiempoTarea2));
                    endTime = (Date) franjaHorario.getmStartTime().clone();


                }else if(tiempoTarea <= 75 && tiempoMinutosFranja <= 75){


                    if( tiempoTarea < tiempoMinutosFranja ){

                        int tiempoTarea2 = tiempoTarea;
                        tiempoTarea -= tiempoTarea;
                        startTime = (Date) franjaHorario.getmStartTime().clone();
                        franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(tiempoTarea2));
                        endTime = (Date) franjaHorario.getmStartTime().clone();

                    }else {

                        tiempoTarea -= tiempoMinutosFranja;
                        startTime = (Date) franjaHorario.getmStartTime().clone();
                        franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(tiempoMinutosFranja));
                        endTime = (Date) franjaHorario.getmStartTime().clone();
                        horariosDia.remove(franjaHorario);

                    }

                }

                startTime.setYear(startTime.getYear()+1900);
                endTime.setYear(endTime.getYear()+1900);
                Horario horarioAsignar =  new Horario(idBeneficiario,franjaHorario.getIdHorario(),startTime
                        ,endTime,"Desarrollo "+tarea.getNombre(),tarea.getColor(),false);
                horariosTarea.add(horarioAsignar);

            }
       }

       return tiempoTarea;
   }





}
