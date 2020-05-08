package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DetallePerfilActivity extends AppCompatActivity {

    EditText edtxNombreDetallePB,edtxApellidosDetallePB,edtxGradoDetallePB,edtxFechaNacDetallePB;
    Button btnAgregarHorDis;
    Button btnAgregarEstilo;
    Button btnEditarPerfil;
    Button btnEliminarPerfil;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Usuario beneficiario;
    public static final String PATH_HORARIO_DISPONIBLE = "horarioDisponible/";
    public static final String PATH_USUARIOS = "/usuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_perfil);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        btnAgregarHorDis = findViewById(R.id.btnAgregarHorDis);
        btnAgregarEstilo = findViewById(R.id.btnAgregaEstiloApren);
        edtxNombreDetallePB = findViewById(R.id.edtxNombreDetallePB);
        edtxApellidosDetallePB = findViewById(R.id.edtxApellidosDetallePB);
        edtxGradoDetallePB = findViewById(R.id.edtxGradoDetallePB);
        edtxFechaNacDetallePB = findViewById(R.id.edtxFechaNacDetallePB);
        beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");
        edtxNombreDetallePB.setText(beneficiario.getNombres());
        edtxApellidosDetallePB.setText(beneficiario.getApellidos());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String fechaNac = df.format(beneficiario.getFechaNacimiento());
        edtxFechaNacDetallePB.setText(fechaNac);
        edtxGradoDetallePB.setText(beneficiario.getCurso());

        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnEliminarPerfil = findViewById(R.id.btnEliminarPerfil);

        btnAgregarHorDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passEvents();
            }
        });

        btnAgregarEstilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),TestAprendizajeActivity.class);
                intent.putExtra("beneficiario", beneficiario);
                startActivity(intent);
            }
        });

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), AgregarPerfilActivity.class);
                intent.putExtra("Usuario", beneficiario);
                intent.putExtra("codigo", 1);
                startActivity(intent);
            }
        });

        btnEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef = database.getReference(PATH_USUARIOS + beneficiario.getIdUsuario());
                myRef.removeValue();

                Toast.makeText(getApplicationContext(), "Usuario eliminado", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), PerfilesActivity.class);
                intent.putExtra("idBeneficiario",(String)getIntent().getSerializableExtra("idBeneficiario"));
                startActivity(intent);
            }
        });

    }

    private void passEvents() {
        Usuario beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");
        ArrayList<Horario> eventsHorario = new ArrayList<>();
        myRef = database.getReference(PATH_HORARIO_DISPONIBLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot sd : dataSnapshot.getChildren()){
                    Horario horario = sd.getValue(Horario.class);
                    if( beneficiario.getIdBeneficiario().equals(horario.getmId())){
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
