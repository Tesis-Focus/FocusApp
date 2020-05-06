package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetalleActividadActivity extends AppCompatActivity {

    Actividad actividad;
    EditText nombre;
    EditText descripcion;
    EditText tipo;
    EditText fechaInicio;
    EditText fechaFin;
    EditText desempeño;
    EditText horarioFijo;
    Button btneEliminar;
    Button btnEditarActividad;

    List<Usuario> beneficiarios;
    List<String> nombresBeneficiarios;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private final static String PATH_USUARIOS = "usuarios/";
    public static final String PATH_ACTIVIDADES = "actividades/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        actividad = (Actividad) getIntent().getSerializableExtra("Actividad");
        beneficiarios = new ArrayList<>();
        nombresBeneficiarios = new ArrayList<>();
        user = mAuth.getCurrentUser();

        nombre = findViewById(R.id.edtxNombreActividad);
        descripcion = findViewById(R.id.edtxtDescripcion);
        tipo = findViewById(R.id.edtxTipo);
        fechaInicio = findViewById(R.id.edtxFechaInicio);
        fechaFin = findViewById(R.id.edttxtFechaFin);
        desempeño = findViewById(R.id.edttxtDesempeño);
        horarioFijo = findViewById(R.id.edttxtHorarioFijo);
        btneEliminar = findViewById(R.id.btnEliminarActividad);
        btnEditarActividad = findViewById(R.id.btnEditarActividad);

        nombre.setText(actividad.getNombre());
        descripcion.setText(actividad.getDescripcion());
        tipo.setText((actividad.getTipo()));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        actividad.getFechaInicio().setMonth(actividad.getFechaInicio().getMonth()-1);
        actividad.getFechaInicio().setYear(actividad.getFechaInicio().getYear()-1900);
        String fechInicio = df.format(actividad.getFechaInicio());
        fechaInicio.setText(fechInicio);

        actividad.getFechaFinal().setMonth(actividad.getFechaFinal().getMonth()-1);
        actividad.getFechaFinal().setYear(actividad.getFechaFinal().getYear()-1900);
        String fechFinal = df.format(actividad.getFechaFinal());
        fechaFin.setText(fechFinal);

         desempeño.setText(actividad.getDesempeño());
        if(actividad.getHorarioFijo()){
            horarioFijo.setText("Si");
        }else{
            horarioFijo.setText("No");
        }

        myRef.child(PATH_USUARIOS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    Usuario beneficiario = sn.getValue(Usuario.class);
                    if(beneficiario.getRol().equals("Beneficiario") && beneficiario.getIdUsuario().equals(user.getUid())){
                        beneficiarios.add(beneficiario);
                        nombresBeneficiarios.add(beneficiario.getNombres()+" "+beneficiario.getApellidos());
                        Log.i("beneficiarios", "onDataChangeDetalle: "+(beneficiario.getNombres()+" "+beneficiario.getApellidos()));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btneEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(PATH_ACTIVIDADES+actividad.getIdActividad());
                myRef.removeValue();

                Toast.makeText(getApplicationContext(), "Actividad eliminada", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), ActividadesActivity.class);
                intent.putExtra("idBeneficiario",(String)getIntent().getSerializableExtra("idBeneficiario"));
                startActivity(intent);
            }
        });

        btnEditarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("beneficiarios", "onDataChangeDetalleLista: " +beneficiarios.toArray().toString());
                Intent intent = new Intent(getBaseContext(), AgregarActividadActivity.class);
                intent.putExtra("beneficiarios", (Serializable) beneficiarios);
                intent.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
                intent.putExtra("actividad",actividad);
                intent.putExtra("codigo",1);
                startActivity(intent);
            }
        });

    }
}
