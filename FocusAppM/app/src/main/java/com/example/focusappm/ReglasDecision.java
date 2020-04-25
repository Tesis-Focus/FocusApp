package com.example.focusappm;

import android.util.Log;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

public class ReglasDecision {

    public ReglasDecision() {
    }


    public Tarea asignarTiempos(Tarea tarea, String desempenioAct) {

        Facts facts = new Facts();
        facts.put("tarea", tarea);
        facts.put("desempeñoAct", desempenioAct);

        //Reglas de tiempo base
        MVELRule ruleProyecto = new MVELRule()
                .name("regla tiempo proyecto")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Proyecto\")")
                .then("tarea.setTiempoPromedio(248.57);");

        MVELRule ruleTaller = new MVELRule()
                .name("regla tiempo taller")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Taller\")")
                .then("tarea.setTiempoPromedio(54.6);");

        MVELRule ruleEvaluacion = new MVELRule()
                .name("regla tiempo evaluacion")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Estudio para evaluación\")")
                .then("tarea.setTiempoPromedio(222.3);");

        MVELRule ruleTarea = new MVELRule()
                .name("regla tiempo tarea")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Tarea\")")
                .then("tarea.setTiempoPromedio(43.9);");

        MVELRule ruleExposición = new MVELRule()
                .name("regla tiempo exposición")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Exposición\")")
                .then("tarea.setTiempoPromedio(134.6);");

        MVELRule ruleManual = new MVELRule()
                .name("regla tiempo manual")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Trabajo manual\")")
                .then("tarea.setTiempoPromedio(359.28);");

        Rules rules = new Rules();
        rules.register(ruleProyecto);
        rules.register(ruleTaller);
        rules.register(ruleEvaluacion);
        rules.register(ruleTarea);
        rules.register(ruleExposición);
        rules.register(ruleManual);

        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);

        Log.i("testRule", "testRule1: " + tarea.getTiempoPromedio());

        Double porcentaje = (tarea.getTiempoPromedio() * 15) / 100;

        facts.put("tiempoPorcentaje", porcentaje);

        Log.i("testRule", "testRule1: " + porcentaje);

        //Reglas de tiempo por complejidad
        MVELRule ruleComplejidadAlta = new MVELRule()
                .name("regla tiempo complejidad alta")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Alto\")")
                .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje);");

        MVELRule ruleComplejidadBaja = new MVELRule()
                .name("regla tiempo complejidad baja")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Bajo\")")
                .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje);");

        //Reglas de tiempo por desempeño
        MVELRule ruleDesempeñoAlto = new MVELRule()
                .name("regla tiempo desempeño alto")
                .priority(1)
                .when("desempeñoAct.equals(\"Alto\")")
                .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje);");

        MVELRule ruleDesempeñoBajo = new MVELRule()
                .name("regla tiempo desempeño bajo")
                .priority(1)
                .when("desempeñoAct.equals(\"Bajo\")")
                .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje);");


        Rules rulesPorcentaje = new Rules();
        rulesPorcentaje.register(ruleComplejidadAlta);
        rulesPorcentaje.register(ruleComplejidadBaja);
        rulesPorcentaje.register(ruleDesempeñoAlto);
        rulesPorcentaje.register(ruleDesempeñoBajo);
        rulesEngine.fire(rulesPorcentaje, facts);

        return tarea;

    }

    public Tarea asignarPrioridad(Tarea tarea, String desempenio){

        tarea.setPrioridad(0);
        Facts facts = new Facts();
        facts.put("tarea",tarea);
        RulesEngine rulesEngine = new DefaultRulesEngine();

        MVELRule prioridadProyecto = new MVELRule()
                .name("regla prioridad por Proyecto")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Proyecto\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+3);");
        MVELRule prioridadTaller = new MVELRule()
                .name("regla prioridad por Taller")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Taller\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+2);");
        MVELRule prioridadEstudioEval = new MVELRule()
                .name("regla prioridad por estudio para Evalaucion")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Estudio para evaluación\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+1);");
        MVELRule prioridadTarea = new MVELRule()
                .name("regla prioridad por Tarea")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Tarea\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+1);");
        MVELRule prioridadExpo = new MVELRule()
                .name("regla prioridad por Exposicion")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Exposición\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+2);");
        MVELRule prioridadTrabajoManual = new MVELRule()
                .name("regla prioridad por Trabajo Manual")
                .priority(1)
                .when("tarea.getClasificacion().equals(\"Trabajo manual\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+2);");

        Rules reglasPrioridad = new Rules();
        reglasPrioridad.register(prioridadProyecto);
        reglasPrioridad.register(prioridadTaller);
        reglasPrioridad.register(prioridadEstudioEval);
        reglasPrioridad.register(prioridadTarea);
        reglasPrioridad.register(prioridadExpo);
        reglasPrioridad.register(prioridadTrabajoManual);

        rulesEngine.fire(reglasPrioridad,facts);
        Log.i("test", "asignarPrioridad: 1-"+tarea.getPrioridad());

        facts.put("desempenio",desempenio);

        MVELRule prioridadDesempenioAlto = new MVELRule()
                .name("regla prioridad por desempenio alto")
                .priority(1)
                .when("desempenio.equals(\"Trabajo manual\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+1);");
        MVELRule prioridadDesempenioMedio = new MVELRule()
                .name("regla prioridad por desempenio medio")
                .priority(1)
                .when("desempenio.equals(\"Trabajo manual\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+2);");
        MVELRule prioridadDesempenioBajo = new MVELRule()
                .name("regla prioridad por desempenio bajo")
                .priority(1)
                .when("desempenio.equals(\"Trabajo manual\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+3);");



        reglasPrioridad.register(prioridadDesempenioAlto);
        reglasPrioridad.register(prioridadDesempenioMedio);
        reglasPrioridad.register(prioridadDesempenioBajo);

        rulesEngine.fire(reglasPrioridad,facts);
        Log.i("test", "asignarPrioridad: 2-"+tarea.getPrioridad());

        MVELRule prioridadComplejidadBajo = new MVELRule()
                .name("regla prioridad por complejidad bajo")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Bajo\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+1);");
        MVELRule prioridadComplejidadMedio = new MVELRule()
                .name("regla prioridad por complejidad medio")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Bajo\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+2);");

        MVELRule prioridadComplejidadAlto = new MVELRule()
                .name("regla prioridad por complejidad Alto")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Alto\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+3);");


        reglasPrioridad.register(prioridadComplejidadBajo);
        reglasPrioridad.register(prioridadComplejidadMedio);
        reglasPrioridad.register(prioridadComplejidadAlto);

        rulesEngine.fire(reglasPrioridad,facts);

        MVELRule prioridadMotivacionTrue = new MVELRule()
                .name("regla prioridad por motivacion verdadera")
                .priority(1)
                .when("!tarea.isEstaMotivado()")
                .then("tarea.setPrioridad(tarea.getPrioridad()+3);");
        Log.i("test", "asignarPrioridad: 3-"+tarea.getPrioridad());

        return tarea;
    }

}