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
    private final static ArrayList<Horario> horarioDispoCambio = new ArrayList<>();
    private final static ArrayList<Horario> horariosDisponiblesBenefi = new ArrayList<>();
    private static float tiempoPromedio = -1;


    public static void planeacion(String idBeneficiario){

        getHorarios(idBeneficiario);

    }


    private static void planearTareas(List<Tarea> tareas,String idBeneficiario){
        for(Tarea t : tareas){
            Date tareaEmpieza,tareaTermina;
            tareaEmpieza = t.getFechaAsignacion();
            tareaEmpieza.setYear(tareaEmpieza.getYear()-1900);
            tareaTermina = t.getFechaEntrega();
            tareaTermina.setYear(tareaTermina.getYear()-1900);
            boolean encontro = false;

            for(Horario h : horariosDisponiblesBenefi){
                encontro = false;
                if( h.getmEndTime().before(tareaTermina) && h.getmStartTime().after(tareaEmpieza)){
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
            int tiempoenMinDisponible = 0 ;
            for(ArrayList<Horario> hList : horarioxTarea){
                for( Horario h: hList){
                    tiempoenMinDisponible += (int) ((h.getmEndTime().getTime() - h.getmStartTime().getTime())/( 60*1000 ) % 60);
                }
            }
            if(tiempoenMinDisponible>tiempoRealizacion){

                planeacionDeTarea(t, tiempoRealizacion,idBeneficiario,false);

            }else{

                planeacionDeTarea(t, tiempoRealizacion,idBeneficiario,true);
            }
        }
    }

   private static void planeacionDeTarea(Tarea tarea, int tiempoTarea, String idBeneficiario,boolean sinTiempo){

        List<Horario> horariosTarea = new ArrayList<>();
        tiempoTarea = asignarHorarios(tarea,tiempoTarea,idBeneficiario,horariosTarea,sinTiempo);

        while (tiempoTarea > 0 && horariosTarea.size() > 0){
            Log.i("OTRA", "planeacionDeTarea: !!!!!!!!!!!!!" + tiempoTarea + "horarios tam "+ horariosTarea.size());
            tiempoTarea = asignarHorarios(tarea,tiempoTarea,idBeneficiario,horariosTarea,sinTiempo);
        }

       for(Horario horarioDisponible : horariosDisponiblesBenefi){
           for(Horario horarioTarea : horariosTarea){
               if( horarioDisponible.getIdHorario().equals(horarioTarea.getIdHorario())){
                   horarioDisponible.setmStartTime(horarioTarea.getmEndTime());
               }
           }
       }

        tarea.setHorarios(horariosTarea);
       Log.i("OTRA", "planeacionDeTarea: tamaño tareas en lista " + horariosTarea.size());
       Log.i("OTRA", "planeacionDeTarea: Tamaño horarios dentro de tarea " + tarea.getHorarios().size());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();
        myRef = db.getReference(PATH_TAREAS+ tarea.getId());
        myRef.setValue(tarea);

   }

   public static  int  asignarHorarios(Tarea tarea, int tiempoTarea, String idBeneficiario, List<Horario> horariosTarea, boolean sinTiempo){

        if(sinTiempo){
            Collections.reverse(horarioxTarea);
        }

       for(ArrayList<Horario> horariosDia: horarioxTarea){
           if(tiempoTarea <= 0){break;}
           //Horario franjaHorario = horariosDia.get(0); //ESTO ES PARA QUE SOLO TOME UNA FRANJA POR DIA TOCA ELIMINAR EL SIGUIENTE FORSITO
           for(Horario franjaHorario: horariosDia){
               if(tiempoTarea <= 0){break;}
               int tiempoMinutosFranja = (int) ((franjaHorario.getmEndTime().getTime() - franjaHorario.getmStartTime().getTime()) /(60 * 1000) % 60);

               if(tiempoTarea >= 75 && tiempoMinutosFranja >= 75){

                   tiempoTarea-= 75;
                   Date startTime = (Date) franjaHorario.getmStartTime().clone();
                   franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(75));
                   Date endTime = (Date) franjaHorario.getmStartTime().clone();
                   Horario horarioAsignar =  new Horario(idBeneficiario,franjaHorario.getIdHorario(),startTime
                           ,endTime,"Desarrollo "+tarea.getNombre(),tarea.getColor(),false);
                   horariosTarea.add(horarioAsignar);

               }else if( tiempoTarea >= 75 && tiempoMinutosFranja <= 75){

                   tiempoTarea-= tiempoMinutosFranja;
                   Date startTime = (Date) franjaHorario.getmStartTime().clone();
                   Date endTime = (Date) franjaHorario.getmEndTime().clone();
                   horariosDia.remove(franjaHorario);
                   Horario horarioAsignar =  new Horario(idBeneficiario,franjaHorario.getIdHorario(),startTime
                           ,endTime,"Desarrollo "+tarea.getNombre(),tarea.getColor(),false);
                   horariosTarea.add(horarioAsignar);

               }else if(tiempoTarea < 75 && tiempoMinutosFranja > 75){

                   tiempoTarea -= tiempoTarea;
                   Date startTime = (Date) franjaHorario.getmStartTime().clone();
                   franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(tiempoTarea));
                   Date endTime = (Date) franjaHorario.getmStartTime().clone();
                   Horario horarioAsignar =  new Horario(idBeneficiario,franjaHorario.getIdHorario(),startTime
                           ,endTime,"Desarrollo "+tarea.getNombre(),tarea.getColor(),false);
                   horariosTarea.add(horarioAsignar);

               }else if(tiempoTarea < 75 && tiempoMinutosFranja < 75){

                   Date startTime,endTime;
                   if( tiempoTarea < tiempoMinutosFranja ){

                       tiempoTarea -= tiempoTarea;
                       startTime = (Date) franjaHorario.getmStartTime().clone();
                       franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(tiempoTarea));
                       endTime = (Date) franjaHorario.getmStartTime().clone();

                   }else {

                       tiempoTarea -= tiempoMinutosFranja;
                       startTime = (Date) franjaHorario.getmStartTime().clone();
                       franjaHorario.getmStartTime().setTime(franjaHorario.getmStartTime().getTime() + TimeUnit.MINUTES.toMillis(tiempoMinutosFranja));
                       endTime = (Date) franjaHorario.getmStartTime().clone();
                       horariosDia.remove(franjaHorario);

                   }

                   Horario horarioAsignar =  new Horario(idBeneficiario,franjaHorario.getIdHorario(),startTime
                           ,endTime,"Desarrollo "+tarea.getNombre(),tarea.getColor(),false);
                   horariosTarea.add(horarioAsignar);

               }
           }
       }
       if(sinTiempo){
           Collections.reverse(horarioxTarea);
       }
       return tiempoTarea;
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

}
