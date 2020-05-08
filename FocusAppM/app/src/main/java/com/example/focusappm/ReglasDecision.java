package com.example.focusappm;

import android.util.Log;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

import java.util.List;

public class ReglasDecision {

    public ReglasDecision() {
    }


    public Tarea asignarTiempos(Tarea tarea, String desempenioAct, List<String> areasActividad, String dominante, String secundario) {

        for (int i=0; i<areasActividad.size(); i++){
            Log.i("test", areasActividad.get(i));
        }
        Log.i("test",dominante);
        Log.i("test", secundario);

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

        float porcentaje = (tarea.getTiempoPromedio() * 15) / 100;
        float porcentajeSec = (tarea.getTiempoPromedio() * 10) / 100;

        facts.put("tiempoPorcentaje", porcentaje);
        facts.put("tiempoPorcentajeSec", porcentajeSec);

        Log.i("testRule", "Procentaje: " + porcentaje);
        Log.i("testRule", "ProcentajeSecunda: " + porcentajeSec);


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
        Log.i("test", "tiempo "+tarea.getTiempoPromedio());


        //Reglas de estilo aprendizaje
        facts.put("dominante",dominante);
        facts.put("secundario",secundario);

        for(int i=0; i<areasActividad.size(); i++){
            Log.i("test", "--------area" + areasActividad.get(i));
            facts.put("i",i);
            facts.put("area",areasActividad.get(i));

            //---------------------------------Reglas para el area Lectura
            /////Reglas para Estilo Convergente
            MVELRule ruleLectConvergenteDivergente = new MVELRule()
                    .name("regla tiempo lectura convergente divergente")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleLectConvergenteAsimilador = new MVELRule()
                    .name("regla tiempo lectura convergente asimilador")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleLectConvergenteAcomodador = new MVELRule()
                    .name("regla tiempo lectura y convergente acomodador")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            /////Reglas para Estilo Divergente
            MVELRule ruleLectDivergenteConvergente = new MVELRule()
                    .name("regla tiempo lectura divergente convergente")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleLectDivergenteAsimilador = new MVELRule()
                    .name("regla tiempo lectura divergente Asimilador")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleLectDivergenteAcomodador = new MVELRule()
                    .name("regla tiempo lectura divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje+tiempoPorcentajeSec);");

            /////Reglas para Estilo Asimilador
            MVELRule ruleLectAsimiladorConvergente = new MVELRule()
                    .name("regla tiempo lectura asimilador convergente")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleLectAsimiladorDivergente = new MVELRule()
                    .name("regla tiempo lectura asimilador Divergente")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleLectAsimiladorAcomodador = new MVELRule()
                    .name("regla tiempo lectura divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            /////Reglas para Estilo Acomodador
            MVELRule ruleLectAcomodadorConvergente = new MVELRule()
                    .name("regla tiempo lectura acomodador convergente")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleLectAcomodadorDivergente = new MVELRule()
                    .name("regla tiempo lectura acomodador Divergente")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleLectAcomodadorAsimilador = new MVELRule()
                    .name("regla tiempo lectura acomodador asimilador")
                    .priority(1)
                    .when("area.equals(\"Lectura\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            Rules rulesEstilosLectura = new Rules();
            rulesEstilosLectura.register(ruleLectConvergenteDivergente);
            rulesEstilosLectura.register(ruleLectConvergenteAsimilador);
            rulesEstilosLectura.register(ruleLectConvergenteAcomodador);

            rulesEstilosLectura.register(ruleLectDivergenteConvergente);
            rulesEstilosLectura.register(ruleLectDivergenteAsimilador);
            rulesEstilosLectura.register(ruleLectDivergenteAcomodador);

            rulesEstilosLectura.register(ruleLectAsimiladorConvergente);
            rulesEstilosLectura.register(ruleLectAsimiladorDivergente);
            rulesEstilosLectura.register(ruleLectAsimiladorAcomodador);

            rulesEstilosLectura.register(ruleLectAcomodadorConvergente);
            rulesEstilosLectura.register(ruleLectAcomodadorDivergente);
            rulesEstilosLectura.register(ruleLectAcomodadorAsimilador);
            rulesEngine.fire(rulesEstilosLectura, facts);

            Log.i("test", "tiempoDespReglasLectura "+tarea.getTiempoPromedio());


            //---------------------------------Reglas para el area Escritura-------------------------------------------------
            /////Reglas para Estilo Convergente
            MVELRule ruleEscriConvergenteDivergente = new MVELRule()
                    .name("regla tiempo Escritura convergente divergente")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleEscriConvergenteAsimilador = new MVELRule()
                    .name("regla tiempo Escritura convergente asimilador")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleEscriConvergenteAcomodador = new MVELRule()
                    .name("regla tiempo Escritura y convergente acomodador")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Divergente
            MVELRule ruleEscriDivergenteConvergente = new MVELRule()
                    .name("regla tiempo Escritura divergente convergente")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleEscriDivergenteAsimilador = new MVELRule()
                    .name("regla tiempo Escritura divergente Asimilador")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleEscriDivergenteAcomodador = new MVELRule()
                    .name("regla tiempo Escritura divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Asimilador
            MVELRule ruleEscriAsimiladorConvergente = new MVELRule()
                    .name("regla tiempo Escritura asimilador convergente")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleEscriAsimiladorDivergente = new MVELRule()
                    .name("regla tiempo Escritura asimilador Divergente")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleEscriAsimiladorAcomodador = new MVELRule()
                    .name("regla tiempo Escritura divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Acomodador
            MVELRule ruleEscriAcomodadorConvergente = new MVELRule()
                    .name("regla tiempo Escritura acomodador convergente")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleEscriAcomodadorDivergente = new MVELRule()
                    .name("regla tiempo Escritura acomodador Divergente")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleEscriAcomodadorAsimilador = new MVELRule()
                    .name("regla tiempo Escritura acomodador asimilador")
                    .priority(1)
                    .when("area.equals(\"Escritura\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            Rules rulesEstilosEscritura = new Rules();
            rulesEstilosEscritura.register(ruleEscriConvergenteDivergente);
            rulesEstilosEscritura.register(ruleEscriConvergenteAsimilador);
            rulesEstilosEscritura.register(ruleEscriConvergenteAcomodador);

            rulesEstilosEscritura.register(ruleEscriDivergenteConvergente);
            rulesEstilosEscritura.register(ruleEscriDivergenteAsimilador);
            rulesEstilosEscritura.register(ruleEscriDivergenteAcomodador);

            rulesEstilosEscritura.register(ruleEscriAsimiladorConvergente);
            rulesEstilosEscritura.register(ruleEscriAsimiladorDivergente);
            rulesEstilosEscritura.register(ruleEscriAsimiladorAcomodador);

            rulesEstilosEscritura.register(ruleEscriAcomodadorConvergente);
            rulesEstilosEscritura.register(ruleEscriAcomodadorDivergente);
            rulesEstilosEscritura.register(ruleEscriAcomodadorAsimilador);
            rulesEngine.fire(rulesEstilosEscritura, facts);

            Log.i("test", "tiempoDespReglasEscritura "+tarea.getTiempoPromedio());

            //---------------------------------Reglas para el area Razonamiento-------------------------------------------------
            /////Reglas para Estilo Convergente
            MVELRule ruleRazoConvergenteDivergente = new MVELRule()
                    .name("regla tiempo Razonamiento convergente divergente")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleRazoConvergenteAsimilador = new MVELRule()
                    .name("regla tiempo Razonamiento convergente asimilador")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleRazoConvergenteAcomodador = new MVELRule()
                    .name("regla tiempo Razonamiento y convergente acomodador")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            /////Reglas para Estilo Divergente
            MVELRule ruleRazoDivergenteConvergente = new MVELRule()
                    .name("regla tiempo Razonamiento divergente convergente")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleRazoDivergenteAsimilador = new MVELRule()
                    .name("regla tiempo Razonamiento divergente Asimilador")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleRazoDivergenteAcomodador = new MVELRule()
                    .name("regla tiempo Razonamiento divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje+tiempoPorcentajeSec);");

            /////Reglas para Estilo Asimilador
            MVELRule ruleRazoAsimiladorConvergente = new MVELRule()
                    .name("regla tiempo Razonamiento asimilador convergente")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleRazoAsimiladorDivergente = new MVELRule()
                    .name("regla tiempo Razonamiento asimilador Divergente")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleRazoAsimiladorAcomodador = new MVELRule()
                    .name("regla tiempo Razonamiento divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            /////Reglas para Estilo Acomodador
            MVELRule ruleRazoAcomodadorConvergente = new MVELRule()
                    .name("regla tiempo Razonamiento acomodador convergente")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleRazoAcomodadorDivergente = new MVELRule()
                    .name("regla tiempo Razonamiento acomodador Divergente")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleRazoAcomodadorAsimilador = new MVELRule()
                    .name("regla tiempo Razonamiento acomodador asimilador")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            Rules rulesEstilosRazonamiento = new Rules();
            rulesEstilosRazonamiento.register(ruleRazoConvergenteDivergente);
            rulesEstilosRazonamiento.register(ruleRazoConvergenteAsimilador);
            rulesEstilosRazonamiento.register(ruleRazoConvergenteAcomodador);

            rulesEstilosRazonamiento.register(ruleRazoDivergenteConvergente);
            rulesEstilosRazonamiento.register(ruleRazoDivergenteAsimilador);
            rulesEstilosRazonamiento.register(ruleRazoDivergenteAcomodador);

            rulesEstilosRazonamiento.register(ruleRazoAsimiladorConvergente);
            rulesEstilosRazonamiento.register(ruleRazoAsimiladorDivergente);
            rulesEstilosRazonamiento.register(ruleRazoAsimiladorAcomodador);

            rulesEstilosRazonamiento.register(ruleRazoAcomodadorConvergente);
            rulesEstilosRazonamiento.register(ruleRazoAcomodadorDivergente);
            rulesEstilosRazonamiento.register(ruleRazoAcomodadorAsimilador);
            rulesEngine.fire(rulesEstilosRazonamiento, facts);

            Log.i("test", "tiempoDespReglasRazonamiento "+tarea.getTiempoPromedio());

            //---------------------------------Reglas para el area Ingles-------------------------------------------------
            /////Reglas para Estilo Convergente
            MVELRule ruleInglConvergenteDivergente = new MVELRule()
                    .name("regla tiempo Ingles convergente divergente")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleInglConvergenteAsimilador = new MVELRule()
                    .name("regla tiempo Ingles convergente asimilador")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleInglConvergenteAcomodador = new MVELRule()
                    .name("regla tiempo Ingles y convergente acomodador")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Divergente
            MVELRule ruleInglDivergenteConvergente = new MVELRule()
                    .name("regla tiempo Ingles divergente convergente")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleInglDivergenteAsimilador = new MVELRule()
                    .name("regla tiempo Ingles divergente Asimilador")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleInglDivergenteAcomodador = new MVELRule()
                    .name("regla tiempo Ingles divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Asimilador
            MVELRule ruleInglAsimiladorConvergente = new MVELRule()
                    .name("regla tiempo Ingles asimilador convergente")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleInglAsimiladorDivergente = new MVELRule()
                    .name("regla tiempo Ingles asimilador Divergente")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleInglAsimiladorAcomodador = new MVELRule()
                    .name("regla tiempo Ingles divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Acomodador
            MVELRule ruleInglAcomodadorConvergente = new MVELRule()
                    .name("regla tiempo Ingles acomodador convergente")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleInglAcomodadorDivergente = new MVELRule()
                    .name("regla tiempo Ingles acomodador Divergente")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleInglAcomodadorAsimilador = new MVELRule()
                    .name("regla tiempo Ingles acomodador asimilador")
                    .priority(1)
                    .when("area.equals(\"Ingles\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            Rules rulesEstilosIngles = new Rules();
            rulesEstilosIngles.register(ruleInglConvergenteDivergente);
            rulesEstilosIngles.register(ruleInglConvergenteAsimilador);
            rulesEstilosIngles.register(ruleInglConvergenteAcomodador);

            rulesEstilosIngles.register(ruleInglDivergenteConvergente);
            rulesEstilosIngles.register(ruleInglDivergenteAsimilador);
            rulesEstilosIngles.register(ruleInglDivergenteAcomodador);

            rulesEstilosIngles.register(ruleInglAsimiladorConvergente);
            rulesEstilosIngles.register(ruleInglAsimiladorDivergente);
            rulesEstilosIngles.register(ruleInglAsimiladorAcomodador);

            rulesEstilosIngles.register(ruleInglAcomodadorConvergente);
            rulesEstilosIngles.register(ruleInglAcomodadorDivergente);
            rulesEstilosIngles.register(ruleInglAcomodadorAsimilador);
            rulesEngine.fire(rulesEstilosIngles, facts);

            Log.i("test", "tiempoDespReglasIngles "+tarea.getTiempoPromedio());

            //---------------------------------Reglas para el area Competencias-------------------------------------------------
            /////Reglas para Estilo Convergente
            MVELRule ruleCompConvergenteDivergente = new MVELRule()
                    .name("regla tiempo Competencias convergente divergente")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleCompConvergenteAsimilador = new MVELRule()
                    .name("regla tiempo Competencias convergente asimilador")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleCompConvergenteAcomodador = new MVELRule()
                    .name("regla tiempo Competencias y convergente acomodador")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Convergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()+tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Divergente
            MVELRule ruleCompDivergenteConvergente = new MVELRule()
                    .name("regla tiempo Competencias divergente convergente")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleCompDivergenteAsimilador = new MVELRule()
                    .name("regla tiempo Competencias divergente Asimilador")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleCompDivergenteAcomodador = new MVELRule()
                    .name("regla tiempo Competencias divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Divergente\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Asimilador
            MVELRule ruleCompAsimiladorConvergente = new MVELRule()
                    .name("regla tiempo Competencias asimilador convergente")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleCompAsimiladorDivergente = new MVELRule()
                    .name("regla tiempo Competencias asimilador Divergente")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleCompAsimiladorAcomodador = new MVELRule()
                    .name("regla tiempo Competencias divergente Acomodador")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Asimilador\")&&secundario.equals(\"Acomodador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            /////Reglas para Estilo Acomodador
            MVELRule ruleCompAcomodadorConvergente = new MVELRule()
                    .name("regla tiempo Competencias acomodador convergente")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Convergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje+tiempoPorcentajeSec);");

            MVELRule ruleCompAcomodadorDivergente = new MVELRule()
                    .name("regla tiempo Competencias acomodador Divergente")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Divergente\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            MVELRule ruleCompAcomodadorAsimilador = new MVELRule()
                    .name("regla tiempo Competencias acomodador asimilador")
                    .priority(1)
                    .when("area.equals(\"Competencias\")&&dominante.equals(\"Acomodador\")&&secundario.equals(\"Asimilador\")")
                    .then("tarea.setTiempoPromedio(tarea.getTiempoPromedio()-tiempoPorcentaje-tiempoPorcentajeSec);");

            Rules rulesEstilosCompetencias = new Rules();
            rulesEstilosCompetencias.register(ruleCompConvergenteDivergente);
            rulesEstilosCompetencias.register(ruleCompConvergenteAsimilador);
            rulesEstilosCompetencias.register(ruleCompConvergenteAcomodador);

            rulesEstilosCompetencias.register(ruleCompDivergenteConvergente);
            rulesEstilosCompetencias.register(ruleCompDivergenteAsimilador);
            rulesEstilosCompetencias.register(ruleCompDivergenteAcomodador);

            rulesEstilosCompetencias.register(ruleCompAsimiladorConvergente);
            rulesEstilosCompetencias.register(ruleCompAsimiladorDivergente);
            rulesEstilosCompetencias.register(ruleCompAsimiladorAcomodador);

            rulesEstilosCompetencias.register(ruleCompAcomodadorConvergente);
            rulesEstilosCompetencias.register(ruleCompAcomodadorDivergente);
            rulesEstilosCompetencias.register(ruleCompAcomodadorAsimilador);
            rulesEngine.fire(rulesEstilosCompetencias, facts);

            Log.i("test", "tiempoDespReglasCompetencias "+tarea.getTiempoPromedio());

            facts.remove("area");
            facts.remove("i");


        }
        Log.i("test", "tiempoDespuesCiclo "+tarea.getTiempoPromedio());




        return tarea;
    }

    public Tarea asignarPrioridad(Tarea tarea, String desempenio, List<String> areasActividad){

        for (int i=0; i<areasActividad.size(); i++){
            Log.i("test", areasActividad.get(i));
        }

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
        Log.i("test", "asignarPrioridad Clasificación 1: -"+tarea.getPrioridad());

        facts.put("desempenio",desempenio);

        MVELRule prioridadDesempenioAlto = new MVELRule()
                .name("regla prioridad por desempenio alto")
                .priority(1)
                .when("desempenio.equals(\"Altol\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+1);");
        MVELRule prioridadDesempenioMedio = new MVELRule()
                .name("regla prioridad por desempenio medio")
                .priority(1)
                .when("desempenio.equals(\"Medio\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+2);");
        MVELRule prioridadDesempenioBajo = new MVELRule()
                .name("regla prioridad por desempenio bajo")
                .priority(1)
                .when("desempenio.equals(\"Bajo\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+3);");


        reglasPrioridad.register(prioridadDesempenioAlto);
        reglasPrioridad.register(prioridadDesempenioMedio);
        reglasPrioridad.register(prioridadDesempenioBajo);
        rulesEngine.fire(reglasPrioridad,facts);

        Log.i("test", "asignarPrioridad Desempenio: 2-"+tarea.getPrioridad());


        MVELRule prioridadComplejidadBajo = new MVELRule()
                .name("regla prioridad por complejidad bajo")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Bajo\")")
                .then("tarea.setPrioridad(tarea.getPrioridad()+1);");

        MVELRule prioridadComplejidadMedio = new MVELRule()
                .name("regla prioridad por complejidad medio")
                .priority(1)
                .when("tarea.getComplejidad().equals(\"Medio\")")
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

        Log.i("test", "asignarPrioridad Complejidad: 3-"+tarea.getPrioridad());

        MVELRule prioridadMotivacionTrue = new MVELRule()
                .name("regla prioridad por motivacion verdadera")
                .priority(1)
                .when("tarea.isEstaMotivado()")
                .then("tarea.setPrioridad(tarea.estaMotivado()+3);");

        reglasPrioridad.register(prioridadMotivacionTrue);
        rulesEngine.fire(reglasPrioridad,facts);
        Log.i("test", "asignarPrioridad Motivacion: 4-"+tarea.getPrioridad());

      //Reglas de las areas
        for(int i=0; i<areasActividad.size(); i++){

            facts.put("i",i);
            facts.put("area",areasActividad.get(i));
            Log.i("test", "asignarPrioridadArea: " + areasActividad.get(i));

            MVELRule prioridadLectCrtic = new MVELRule()
                    .name("Regla prioridad area Lectura Critica")
                    .priority(1)
                    .when("area.equals(\"Lectura\")")
                    .then("tarea.setPrioridad(tarea.getPrioridad()+5);");

            MVELRule prioridadRazonCuanti = new MVELRule()
                    .name("Regla prioridad area Razonamiento cuantitativo")
                    .priority(1)
                    .when("area.equals(\"Razonamiento\")")
                    .then("tarea.setPrioridad(tarea.getPrioridad()+4);");

            MVELRule prioridadComuniEscrita = new MVELRule()
                    .name("Regla prioridad area Comunicacion escrita")
                    .priority(1)
                    .when("area.equals(\"Escritura\")")
                    .then("tarea.setPrioridad(tarea.getPrioridad()+3);");

            MVELRule prioridadComptCiudadanas= new MVELRule()
                    .name("Regla prioridad area Competencias ciudadanas")
                    .priority(1)
                    .when("area.equals(\"Competencias\")")
                    .then("tarea.setPrioridad(tarea.getPrioridad()+2);");

            MVELRule prioridadIngles= new MVELRule()
                    .name("Regla prioridad area Ingles")
                    .priority(1)
                    .when("area.equals(\"Ingles\")")
                    .then("tarea.setPrioridad(tarea.getPrioridad()+1);");



            Rules reglasAreas = new Rules();

            reglasAreas.register(prioridadLectCrtic);
            reglasAreas.register(prioridadRazonCuanti);
            reglasAreas.register(prioridadComuniEscrita);
            reglasAreas.register(prioridadComptCiudadanas);
            reglasAreas.register(prioridadIngles);
            rulesEngine.fire(reglasAreas,facts);

            facts.remove("area");
            facts.remove("i");
            reglasPrioridad.clear();
            Log.i("test", "asignarPrioridadArea: 5-" + tarea.getPrioridad());

        }
        Log.i("test", "asignarPrioridadArea: 5-" + tarea.getPrioridad());


        //Reglas estilo de aprendizaje
        return tarea;
    }

}
