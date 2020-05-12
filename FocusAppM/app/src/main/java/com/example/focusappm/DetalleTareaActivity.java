package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class DetalleTareaActivity extends AppCompatActivity {

    Tarea tarea;
    EditText nombre;
    EditText descripcion;
    EditText fechaEntrega;
    EditText motivacion;
    EditText complejidad;
    EditText clasificacion;
    Button btnEliminarTarea;
    Button btnEditarTarea;

    List<Usuario> beneficiarios;
    List<String> nombresBeneficiarios;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private final static String PATH_USUARIOS = "usuarios/";
    public static final String PATH_TAREAS = "tareas/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        tarea = (Tarea) getIntent().getSerializableExtra("Tarea");
        beneficiarios = new ArrayList<>();
        nombresBeneficiarios = new ArrayList<>();
        user = mAuth.getCurrentUser();

        nombre = findViewById(R.id.edtxNombreTarea);
        descripcion = findViewById(R.id.edtxtDescripcionTarea);
        fechaEntrega = findViewById(R.id.edtxFechaEntrega);
        motivacion = findViewById(R.id.edttxtMotivacion);
        complejidad = findViewById(R.id.edttxtComplejidad);
        clasificacion = findViewById(R.id.edttxtClasificacion);
        btnEliminarTarea = findViewById(R.id.btnEliminarTarea);
        btnEditarTarea = findViewById(R.id.btnEditarTarea);

        nombre.setText(tarea.getNombre());
        descripcion.setText(tarea.getDescripcion());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        tarea.getFechaEntrega().setMonth(tarea.getFechaEntrega().getMonth()-1);
        tarea.getFechaEntrega().setYear(tarea.getFechaEntrega().getYear()-1900);
        String fechEntrega = df.format(tarea.getFechaEntrega());

        fechaEntrega.setText(fechEntrega);
        if(tarea.isEstaMotivado()){
            motivacion.setText("Si");
        }else{
            motivacion.setText("No");
        }
        complejidad.setText(tarea.getComplejidad());
        clasificacion.setText(tarea.getClasificacion());


        
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


        btnEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(PATH_TAREAS+tarea.getIdTarea());
                myRef.removeValue();

                Toast.makeText(getApplicationContext(), "Tarea eliminada", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), HomeAppActivity.class);
                intent.putExtra("idBeneficiario",(String)getIntent().getSerializableExtra("idBeneficiario"));
                startActivity(intent);
            }
        });

        btnEditarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("beneficiarios", "onDataChangeDetalleLista: " +beneficiarios.toArray().toString());
                Intent intent = new Intent(getBaseContext(), AgregarTareaActivity.class);
                intent.putExtra("beneficiarios", (Serializable) beneficiarios);
                intent.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
                intent.putExtra("tarea",tarea);
                intent.putExtra("codigo",1);
                startActivity(intent);
            }
        });

    }



}
