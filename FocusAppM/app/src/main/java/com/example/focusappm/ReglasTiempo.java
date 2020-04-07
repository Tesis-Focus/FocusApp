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

    public void asignarTiempos(Tarea tarea) {

            Facts facts = new Facts();
            facts.put("tarea", tarea);

            MVELRule rule = new MVELRule()
                    .name("regla de tiempo")
                    .priority(1)
                    .when("user.getClasificacion().equals(\"Tarea\")")
                    .then("user.setTiempoPromedio(\"43.9\");");

            Rules rules = new Rules();
            rules.register(rule);
            RulesEngine rulesEngine = new DefaultRulesEngine();
            rulesEngine.fire(rules, facts);
            Log.i("testRule", "testRule: " + tarea.getTiempoPromedio());

        }


}
