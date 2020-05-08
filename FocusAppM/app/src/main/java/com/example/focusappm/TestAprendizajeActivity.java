package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestAprendizajeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_ESTILOS = "estilosAprendizaje/";
    FirebaseAuth mAuth;
    FirebaseUser user;
    Usuario beneficiario;


    ArrayList<String> listDatos;
    ArrayList<String> listResultados,listResultadosDos, listResultadosTres, listResultadosCuatro, listResultadosCinco,listResultadosSeis, listResultadosSiete,listResultadosOcho,listResultadosNueve;
    ArrayList<Integer> valoresUno, valoresDos, valoresTres, valoresCuatro,valoresCinco,
    valoresSeis,valoresSiete, valoresOcho, valoresNueve  = new ArrayList<>();

    RecyclerView recycler;
    AdapterDatos adapter;
    TextView preguntaUno;
    ImageButton siguienteUno, atrasDos;
    ImageButton siguienteDos;
    ImageButton siguienteTres, atrasTres;
    ImageButton siguienteCuatro, atrasCuatro;
    ImageButton siguienteCinco, atrasCinco;
    ImageButton siguienteSeis, atrasSeis;
    ImageButton siguienteSiete, atrasSiete;
    ImageButton siguienteOcho, atrasOcho;
    ImageButton atrasNueve;
    Button terminar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aprendizaje);

        beneficiario = (Usuario) getIntent().getSerializableExtra("beneficiario");
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        recycler = findViewById(R.id.recyclerId);
        preguntaUno = findViewById(R.id.preguntaUno);
        siguienteUno = findViewById(R.id.siguienteUno);
        siguienteDos = findViewById(R.id.siguienteDos);
        atrasDos =findViewById(R.id.atrasDos);
        siguienteTres = findViewById(R.id.siguienteTres);
        atrasTres =findViewById(R.id.atrasTres);
        siguienteCuatro = findViewById(R.id.siguienteCuatro);
        atrasCuatro =findViewById(R.id.atrasCuatro);
        siguienteCinco = findViewById(R.id.siguienteCinco);
        atrasCinco =findViewById(R.id.atrasCinco);
        siguienteSeis = findViewById(R.id.siguienteSeis);
        atrasSeis =findViewById(R.id.atrasSeis);
        siguienteSiete = findViewById(R.id.siguienteSiete);
        atrasSiete =findViewById(R.id.atrasSiete);
        siguienteOcho = findViewById(R.id.siguienteOcho);
        atrasOcho =findViewById(R.id.atrasOcho);
        atrasNueve =findViewById(R.id.atrasNueve);
        terminar = findViewById(R.id.terminar);


        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false ));
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
        recycler.addItemDecoration(itemDecorator);

        listDatos= new ArrayList<String>();
        listResultados=new ArrayList<String>();
        listResultadosDos=new ArrayList<String>();
        listResultadosTres=new ArrayList<String>();
        listResultadosCuatro=new ArrayList<String>();
        listResultadosCinco=new ArrayList<String>();
        listResultadosSeis=new ArrayList<String>();
        listResultadosSiete=new ArrayList<String>();
        listResultadosOcho=new ArrayList<String>();
        listResultadosNueve=new ArrayList<String>();


        mostrarPreguntaDos();
        listResultadosDos.addAll(listDatos);
        mostrarPreguntaTres();
        listResultadosTres.addAll(listDatos);
        mostrarPreguntaCuatro();
        listResultadosCuatro.addAll(listDatos);
        mostrarPreguntaCinco();
        listResultadosCinco.addAll(listDatos);
        mostrarPreguntaSeis();
        listResultadosSeis.addAll(listDatos);
        mostrarPreguntaSiete();
        listResultadosSiete.addAll(listDatos);
        mostrarPreguntaOcho();
        listResultadosOcho.addAll(listDatos);
        mostrarPreguntaNueve();
        listResultadosNueve.addAll(listDatos);

        mostrarPreguntaUno();
        Log.i("MyAPP", listDatos.toString());
        listResultados.addAll(listDatos);
        adapter = new AdapterDatos(listResultados);

        inicializar();
        siguienteDos.setVisibility(View.GONE);
        atrasDos.setVisibility(View.GONE);
        siguienteTres.setVisibility(View.GONE);
        atrasTres.setVisibility(View.GONE);
        siguienteCuatro.setVisibility(View.GONE);
        atrasCuatro.setVisibility(View.GONE);
        siguienteCinco.setVisibility(View.GONE);
        atrasCinco.setVisibility(View.GONE);
        siguienteSeis.setVisibility(View.GONE);
        atrasSeis.setVisibility(View.GONE);
        siguienteSiete.setVisibility(View.GONE);
        atrasSiete.setVisibility(View.GONE);
        siguienteOcho.setVisibility(View.GONE);
        atrasOcho.setVisibility(View.GONE);
        atrasNueve.setVisibility(View.GONE);
        terminar.setVisibility(View.GONE);

        Log.i("MyAPP", listDatos.toString());
        Log.i("MyAPPREsult", listResultados.toString());


        siguienteUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteUno.setVisibility(View.GONE);
                atrasDos.setVisibility(View.VISIBLE);
                siguienteDos.setVisibility(View.VISIBLE);

                valoresUno = new ArrayList<>();

                Log.i("MyAPP", listDatos.toString());
                Log.i("MyAPPResult", listResultados.toString());

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultados.size(); i++) {
                        if (listResultados.get(i).equals(elemento)){
                            valoresUno.add(i+1);
                        }
                    }
                }
                Log.i("MyAPValores", valoresUno.toString());

                mostrarPreguntaDos();
                inicializar();
            }
        });

        atrasDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteUno.setVisibility(View.VISIBLE);
                siguienteDos.setVisibility(View.GONE);
                atrasDos.setVisibility(View.GONE);

                mostrarPreguntaUno();
                inicializar();
            }
        });

        siguienteDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteTres.setVisibility(View.VISIBLE);
                atrasTres.setVisibility(View.VISIBLE);
                siguienteDos.setVisibility(View.GONE);
                atrasDos.setVisibility(View.GONE);
                valoresDos = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosDos.size(); i++) {
                        if (listResultadosDos.get(i).equals(elemento)){
                            valoresDos.add(i+1);
                        }
                    }
                }
                mostrarPreguntaTres();
                inicializar();
            }
        });

        atrasTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteDos.setVisibility(View.VISIBLE);
                atrasDos.setVisibility(View.VISIBLE);
                siguienteTres.setVisibility(View.GONE);
                atrasTres.setVisibility(View.GONE);
                siguienteUno.setVisibility(View.GONE);

                mostrarPreguntaDos();
                inicializar();
            }
        });

        siguienteTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteCuatro.setVisibility(View.VISIBLE);
                atrasCuatro.setVisibility(View.VISIBLE);
                siguienteTres.setVisibility(View.GONE);
                atrasTres.setVisibility(View.GONE);
                valoresTres = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosTres.size(); i++) {
                        if (listResultadosTres.get(i).equals(elemento)){
                            valoresTres.add(i+1);
                        }
                    }
                }
                mostrarPreguntaCuatro();
                inicializar();
            }
        });

        atrasCuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteTres.setVisibility(View.VISIBLE);
                atrasTres.setVisibility(View.VISIBLE);
                siguienteCuatro.setVisibility(View.GONE);
                atrasCuatro.setVisibility(View.GONE);

                mostrarPreguntaTres();
                inicializar();
            }
        });

        siguienteCuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteCinco.setVisibility(View.VISIBLE);
                atrasCinco.setVisibility(View.VISIBLE);
                siguienteCuatro.setVisibility(View.GONE);
                atrasCuatro.setVisibility(View.GONE);
                valoresCuatro = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosCuatro.size(); i++) {
                        if (listResultadosCuatro.get(i).equals(elemento)){
                            valoresCuatro.add(i+1);
                        }
                    }
                }
                mostrarPreguntaCinco();
                inicializar();
            }
        });


        atrasCinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteCuatro.setVisibility(View.VISIBLE);
                atrasCuatro.setVisibility(View.VISIBLE);
                siguienteCinco.setVisibility(View.GONE);
                atrasCinco.setVisibility(View.GONE);

                mostrarPreguntaCuatro();
                inicializar();
            }
        });

        siguienteCinco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteSeis.setVisibility(View.VISIBLE);
                atrasSeis.setVisibility(View.VISIBLE);
                siguienteCinco.setVisibility(View.GONE);
                atrasCinco.setVisibility(View.GONE);
                valoresCinco = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosCinco.size(); i++) {
                        if (listResultadosCinco.get(i).equals(elemento)){
                            valoresCinco.add(i+1);
                        }
                    }
                }
                mostrarPreguntaSeis();
                inicializar();
            }
        });

        atrasSeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteCinco.setVisibility(View.VISIBLE);
                atrasCinco.setVisibility(View.VISIBLE);
                siguienteSeis.setVisibility(View.GONE);
                atrasSeis.setVisibility(View.GONE);

                mostrarPreguntaCinco();
                inicializar();
            }
        });

        siguienteSeis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteSiete.setVisibility(View.VISIBLE);
                atrasSiete.setVisibility(View.VISIBLE);
                siguienteSeis.setVisibility(View.GONE);
                atrasSeis.setVisibility(View.GONE);
                valoresSeis = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosSeis.size(); i++) {
                        if (listResultadosSeis.get(i).equals(elemento)){
                            valoresSeis.add(i+1);
                        }
                    }
                }
                mostrarPreguntaSiete();
                inicializar();
            }
        });
        atrasSiete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteSeis.setVisibility(View.VISIBLE);
                atrasSeis.setVisibility(View.VISIBLE);
                siguienteSiete.setVisibility(View.GONE);
                atrasSiete.setVisibility(View.GONE);

                mostrarPreguntaSeis();
                inicializar();
            }
        });

        siguienteSiete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguienteOcho.setVisibility(View.VISIBLE);
                atrasOcho.setVisibility(View.VISIBLE);
                siguienteSiete.setVisibility(View.GONE);
                atrasSiete.setVisibility(View.GONE);
                valoresSiete = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosSiete.size(); i++) {
                        if (listResultadosSiete.get(i).equals(elemento)){
                            valoresSiete.add(i+1);
                        }
                    }
                }
                mostrarPreguntaOcho();
                inicializar();
            }
        });

        atrasOcho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteSiete.setVisibility(View.VISIBLE);
                atrasSiete.setVisibility(View.VISIBLE);
                siguienteOcho.setVisibility(View.GONE);
                atrasOcho.setVisibility(View.GONE);

                mostrarPreguntaSiete();
                inicializar();
            }
        });

        siguienteOcho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atrasNueve.setVisibility(View.VISIBLE);
                terminar.setVisibility(View.VISIBLE);
                siguienteOcho.setVisibility(View.GONE);
                atrasOcho.setVisibility(View.GONE);
                valoresOcho = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosOcho.size(); i++) {
                        if (listResultadosOcho.get(i).equals(elemento)){
                            valoresOcho.add(i+1);
                        }
                    }
                }
                mostrarPreguntaNueve();
                inicializar();
            }
        });

        atrasNueve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                siguienteOcho.setVisibility(View.VISIBLE);
                atrasOcho.setVisibility(View.VISIBLE);
                atrasNueve.setVisibility(View.GONE);
                terminar.setVisibility(View.GONE);

                mostrarPreguntaOcho();
                inicializar();
            }
        });

        terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valoresNueve = new ArrayList<>();

                for (String elemento:listDatos) {
                    for (int i=0; i< listResultadosNueve.size(); i++) {
                        if (listResultadosNueve.get(i).equals(elemento)){
                            valoresNueve.add(i+1);
                        }
                    }
                }
                Integer ec, or, ca, ea;
                EstiloAprendizaje estilo= new EstiloAprendizaje();
                estilo.setEc((valoresUno.get(0)+valoresDos.get(0)+valoresTres.get(0)+valoresCuatro.get(0)+valoresCinco.get(0)+
                        valoresSeis.get(0)+valoresSiete.get(0)+valoresOcho.get(0)+valoresNueve.get(0))*100/90);
                estilo.setOr((valoresUno.get(1)+valoresDos.get(1)+valoresTres.get(1)+valoresCuatro.get(1)+valoresCinco.get(1)+
                        valoresSeis.get(1)+valoresSiete.get(1)+valoresOcho.get(1)+valoresNueve.get(1))*100/90);
                estilo.setCa((valoresUno.get(2)+valoresDos.get(2)+valoresTres.get(2)+valoresCuatro.get(2)+valoresCinco.get(2)+
                        valoresSeis.get(2)+valoresSiete.get(2)+valoresOcho.get(2)+valoresNueve.get(2))*100/90);
                estilo.setEa((valoresUno.get(3)+valoresDos.get(3)+valoresTres.get(3)+valoresCuatro.get(3)+valoresCinco.get(3)+
                        valoresSeis.get(3)+valoresSiete.get(3)+valoresOcho.get(3)+valoresNueve.get(3))*100/90);
                estilo.setIdBeneficiario(beneficiario.getIdBeneficiario());

                TreeMap<Integer, String> mapa = new TreeMap<Integer, String>();
                mapa.put(estilo.getEc(), "EC");
                mapa.put(estilo.getOr(), "OR");
                mapa.put(estilo.getCa(), "CA");
                mapa.put(estilo.getEa(), "EA");

                int cont=0;
                String menor="", menorm="", mayor="", mayorm="";
                for (Map.Entry<Integer, String> entry : mapa.entrySet()) {
                    if(cont ==3){
                        mayorm=entry.getValue();
                    }
                    if(cont ==2){
                        mayor = entry.getValue();
                    }
                    if(cont ==1){
                        menor = entry.getValue();
                    }
                    if(cont ==0){
                        menorm = entry.getValue();
                    }
                    cont++;
                }

                if(mayorm.equals("EC") && mayor.equals("OR") || mayorm.equals("OR") && mayor.equals("EC")){
                    estilo.setDominate("Divergente");
                }
                if(mayorm.equals("EC") && mayor.equals("EA") || mayorm.equals("EA") && mayor.equals("EC")){
                    estilo.setDominate("Acomodador");
                }
                if(mayorm.equals("CA") && mayor.equals("OR") || mayorm.equals("OR") && mayor.equals("CA")){
                    estilo.setDominate("Asimilador");
                }
                if(mayorm.equals("CA") && mayor.equals("EA") || mayorm.equals("EA") && mayor.equals("CA")){
                    estilo.setDominate("Convergente");
                }

                if(menor.equals("EC") && menorm.equals("OR") || menor.equals("OR") && menorm.equals("EC")){
                    estilo.setSecundario("Divergente");
                }
                if(menor.equals("EC") && menorm.equals("EA") || menor.equals("EA") && menorm.equals("EC")){
                    estilo.setSecundario("Acomodador");
                }
                if(menor.equals("CA") && menorm.equals("OR") || menor.equals("OR") && menorm.equals("CA")){
                    estilo.setSecundario("Asimilador");
                }
                if(menor.equals("CA") && menorm.equals("EA") || menor.equals("EA") && menorm.equals("CA")){
                    estilo.setSecundario("Convergente");
                }


                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String key = myRef.push().getKey();
                myRef = database.getReference(PATH_ESTILOS+key);
                myRef.setValue(estilo);
                Toast.makeText(getApplicationContext(),"Persistencia hecha", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(),DetallePerfilActivity.class);
                intent.putExtra("beneficiario", beneficiario);
                startActivity(intent);



            }
        });
    }
    private void inicializar() {
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recycler);
        recycler.setAdapter(adapter);
    }

    private void mostrarPreguntaUno() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("1. Cuando debes resolver un problema y obtener un resultado rápido ¿Qué haces?");
        listDatos.add("Intento y pruebo" );
        listDatos.add("Me involucro" );
        listDatos.add("Pongo en práctica lo aprendido" );
        listDatos.add("Resuelvo la situación por partes");

        adapter = new AdapterDatos(listResultados);

    }
    private void mostrarPreguntaDos() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("2. ¿Cómo te enfrentas a una realidad nueva?");
        listDatos.add("Confío en mis sentimientos" );
        listDatos.add("Me esfuerzo para que todo salga bien" );
        listDatos.add("Atiendo y observo cuidadosamente su utilidad" );
        listDatos.add("Descompongo todo en sus partes ");
        adapter = new AdapterDatos(listResultadosDos);
    }
    private void mostrarPreguntaTres() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("3. Ante un suceso desconocido ¿Cómo reaccionas?");
        listDatos.add("Me involucró emocionalmente" );
        listDatos.add("Observo de forma atenta" );
        listDatos.add("Examino todo con cuidado para hacerme una idea" );
        listDatos.add("Realizo actividades ");

        adapter = new AdapterDatos(listResultadosTres);
    }

    private void mostrarPreguntaCuatro() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("4. Frente a los cambios ¿Cómo eres?");
        listDatos.add("Lo acepto bien" );
        listDatos.add("Me arriesgo" );
        listDatos.add("Soy cuidadoso" );
        listDatos.add("Me fijo en si las ideas son ciertas");
        adapter = new AdapterDatos(listResultadosCuatro);
    }

    private void mostrarPreguntaCinco() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("5. Cuando aprendes");
        listDatos.add("Le preguntas a los que saben más" );
        listDatos.add("Te gusta ver resultados" );
        listDatos.add("Intentas descubrir como funciona" );
        listDatos.add("Lo haces solo");

        adapter = new AdapterDatos(listResultadosCinco);
    }

    private void mostrarPreguntaSeis() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("6. ¿Cómo eres frente a una tarea?");
        listDatos.add("Observas y examinas los detalles" );
        listDatos.add("Te dedicas a realizar lo que toca hacer" );
        listDatos.add("Eres activo y te gusta manipular cosas" );
        listDatos.add("Eres una persona lógica, separas lo esencial de las cualidades de forma abstracta");

        adapter = new AdapterDatos(listResultadosSeis);
    }

    private void mostrarPreguntaSiete() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("7.	¿Qué piensas de lo que aprendes?");
        listDatos.add("Lo aprendido me servirá ahora" );
        listDatos.add("Considero todo detalladamente" );
        listDatos.add("Lo aprendido me servirá después" );
        listDatos.add("Busco los casos prácticos");

        adapter = new AdapterDatos(listResultadosSiete);
    }

    private void mostrarPreguntaOcho() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("8. ¿Qué es más importante para aprender?");
        listDatos.add("Observar" );
        listDatos.add("Experimentar" );
        listDatos.add("Saber la teoría" );
        listDatos.add("Vivir situaciones");

        adapter = new AdapterDatos(listResultadosOcho);
    }

    private void mostrarPreguntaNueve() {
        listDatos= new ArrayList<String>();
        preguntaUno.setText("9. ¿Cómo eres en el colegio?");
        listDatos.add("Soy reservado" );
        listDatos.add("Aporto nuevas ideas cuando puedo" );
        listDatos.add("Intuitivo, estimulado por mis sentimientos" );
        listDatos.add("Busco siempre que es verdadero y que es falso");

        adapter = new AdapterDatos(listResultadosNueve);
    }
}
