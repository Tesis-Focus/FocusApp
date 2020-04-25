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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UtilsFocus {

    private final static String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";

    public static void calcularHorarioPorTarea(Tarea tarea, String idBeneficiario){

        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference();


        Date fechaEntrega = null, fechaAsignacion = null;
        long diasParaEntrega;

        tarea.getFechaEntrega().setMonth(tarea.getFechaEntrega().getMonth()-1);
        tarea.getFechaAsignacion().setMonth(tarea.getFechaAsignacion().getMonth()-1);

        fechaEntrega = tarea.getFechaEntrega();
        fechaAsignacion = tarea.getFechaAsignacion();

        diasParaEntrega = Math.abs(fechaEntrega.getTime() - fechaAsignacion.getTime());
        diasParaEntrega = TimeUnit.DAYS.convert(diasParaEntrega,TimeUnit.MILLISECONDS);
        Log.i("Planeacion", "Faltan "+diasParaEntrega+" dias para entregar");

        Date finalFechaEntrega = fechaEntrega;
        Date finalFechaAsignacion = fechaAsignacion;
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
                Log.i("Planeacion", "tam " + horarios.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    static boolean validarFecha(Horario horario, Date fechaEntrega, Date fechaAsignacion){
        Log.i("Planeacion", "FechaEntrega " + fechaEntrega);
        Log.i("Planeacion", "fechaAsignacion " + fechaAsignacion);
        Log.i("Planeacion", "horarioStartTime " + horario.getmStartTime());
        Log.i("Planeacion", "horarioEndTime " + horario.getmEndTime());
        if(horario.getmEndTime().before(fechaEntrega) && horario.getmStartTime().after(fechaAsignacion)) {
            return true;
        }
        return false;
    }

}
