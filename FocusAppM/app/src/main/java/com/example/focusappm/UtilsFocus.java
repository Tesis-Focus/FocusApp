package com.example.focusappm;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

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
    public static void calcularHorarioPorTarea(Tarea tarea){
        Date fechaEntrega = null, fechaAsignacion = null;
        long diasParaEntrega;

        try {
            fechaEntrega = new SimpleDateFormat("dd/MM/yyyy").parse(tarea.getFechaEntrega());
            fechaAsignacion = new SimpleDateFormat("dd/MM/yyyy").parse(tarea.getFechaAsignacion());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        diasParaEntrega = Math.abs(fechaEntrega.getTime() - fechaAsignacion.getTime());
        diasParaEntrega = TimeUnit.DAYS.convert(diasParaEntrega,TimeUnit.MILLISECONDS);
        Log.i("Planeacion", "Faltan "+diasParaEntrega+" dias para entregar");


    }

}
