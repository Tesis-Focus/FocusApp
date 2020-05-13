package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
    EditText edtxAsignatura;
    EditText fechaInicio;
    EditText fechaFin;
    EditText desempeño;
    EditText diasRepite;
    Button btneEliminar;
    Button btnEditarActividad;
    TextView textAsignatura;


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
        edtxAsignatura = findViewById(R.id.edtxAsignatura);
        textAsignatura = findViewById(R.id.textAsignatura);
        fechaInicio = findViewById(R.id.edtxFechaInicio);
        fechaFin = findViewById(R.id.edttxtFechaFin);
        desempeño = findViewById(R.id.edttxtDesempeño);
        diasRepite = findViewById(R.id.edttxtDias);
        btneEliminar = findViewById(R.id.btnEliminarActividad);
        btnEditarActividad = findViewById(R.id.btnEditarActividad);


        nombre.setText(actividad.getNombre());
        descripcion.setText(actividad.getDescripcion());
        tipo.setText((actividad.getTipo()));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        actividad.getFechaInicio().setMonth(actividad.getFechaInicio().getMonth());
        actividad.getFechaInicio().setYear(actividad.getFechaInicio().getYear()-1900);
        String fechInicio = df.format(actividad.getFechaInicio());
        fechaInicio.setText(fechInicio);

        actividad.getFechaFinal().setMonth(actividad.getFechaFinal().getMonth());
        actividad.getFechaFinal().setYear(actividad.getFechaFinal().getYear()-1900);
        String fechFinal = df.format(actividad.getFechaFinal());
        fechaFin.setText(fechFinal);

        Log.i("FechasAgregar" , "INI-Cambio" + actividad.getFechaInicio().getYear());
        Log.i("FechasAgregar" , "FIN-Cambio" + actividad.getFechaFinal().getYear());

        desempeño.setText(actividad.getDesempeño());

        textAsignatura.setVisibility(View.GONE);
        edtxAsignatura.setVisibility(View.GONE);

        if(actividad.getTipo().equals("Académica")){
            textAsignatura.setVisibility(View.VISIBLE);
            edtxAsignatura.setVisibility(View.VISIBLE);
            edtxAsignatura.setText(actividad.getAsignatura());
        }
        Boolean lunes=false,martes= false,miercoles= false,jueves= false,vierenes= false,sabado= false,domingo= false;
        for(Horario horario: actividad.getHorarios()){
            if(horario.getmStartTime().getDay()==2 && !lunes){
                diasRepite.setText(diasRepite.getText() + " Lunes");
                lunes = true;
            }
            if(horario.getmStartTime().getDay()==3 && !martes){
                diasRepite.setText(diasRepite.getText() + " Martes");
                martes = true;
            }
            if(horario.getmStartTime().getDay()==4 && !miercoles){
                diasRepite.setText(diasRepite.getText() + " Miercoles");
                miercoles = true;
            }
            if(horario.getmStartTime().getDay()==5 && !jueves){
                diasRepite.setText(diasRepite.getText() + " Jueves");
                jueves = true;
            }
            if(horario.getmStartTime().getDay()==6 && !vierenes){
                diasRepite.setText(diasRepite.getText() + " Viernes");
                vierenes = true;
            }
            if(horario.getmStartTime().getDay()==7 && !sabado){
                diasRepite.setText(diasRepite.getText() + " Sabado");
                sabado = true;
            }
            if(horario.getmStartTime().getDay()==8 && !domingo){
                diasRepite.setText(diasRepite.getText() + " Domingo");
                domingo = true;
            }
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
                Log.i("FechasAgregar" , "INI-" + actividad.getFechaInicio().getYear());
                Log.i("FechasAgregar" , "FIN-" + actividad.getFechaFinal().getYear());
                intent.putExtra("codigo",1);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_actividad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked==R.id.mnuCalificarActividad){
            Intent intent = new Intent(getBaseContext(), CalificarActividadActivity.class);
            intent.putExtra("Actividad", actividad);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
