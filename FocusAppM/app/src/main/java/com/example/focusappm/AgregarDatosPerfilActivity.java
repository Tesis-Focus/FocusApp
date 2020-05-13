package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AgregarDatosPerfilActivity extends AppCompatActivity {

    Button btnHorario;
    Button btnEstilo;
    Button btnTerminar;
    Usuario beneficiario;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";
    public static final String PATH_ESTILOS = "estilosAprendizaje/";

    boolean hayEstilo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_datos_perfil);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        btnHorario = findViewById(R.id.btnHorario);
        btnEstilo = findViewById(R.id.btnEstilo);
        btnTerminar = findViewById(R.id.btnListoDatosPB);
        beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");

        btnHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passEvents();
            }
        });

        btnEstilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),InstruccionesTest.class);
                intent.putExtra("beneficiario", beneficiario);
                intent.putExtra("codigo",0);
                startActivity(intent);
            }
        });

        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hayEstilo = false;
                FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = dataBase.getReference();
                myRef = dataBase.getReference(PATH_ESTILOS);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            EstiloAprendizaje estilo = ds.getValue(EstiloAprendizaje.class);
                            if(estilo.getIdBeneficiario().equals(beneficiario.getIdBeneficiario())){
                                hayEstilo=true;
                            }
                        }

                        if(!hayEstilo){
                            btnEstilo.setError("Requerido");
                        }
                        if(hayEstilo){
                            Intent i = new Intent(getBaseContext(), PerfilesActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void passEvents() {
    //    Usuario beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");
        ArrayList<Horario> eventsHorario = new ArrayList<>();
        myRef = database.getReference(PATH_HORARIO_DISPONIBLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot sd : dataSnapshot.getChildren()){
                    Horario horario = sd.getValue(Horario.class);
                    if( beneficiario.getIdBeneficiario().equals(horario.getIdBeneficiario())){
                        eventsHorario.add(horario);
                    }
                }
                Log.i("cal", "tamanio de eventos llega "+eventsHorario.size());
                Intent i = new Intent(getBaseContext(),CalendarDisponibleActivity.class);
                i.putExtra("beneficiario",beneficiario);
                i.putExtra("eventos",eventsHorario);
                startActivity(i);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
