package com.example.focusappm;

import android.util.Log;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

public class ReglasTiempo {

    public ReglasTiempo() {
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


}
