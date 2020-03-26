package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgregarTareaActivity extends AppCompatActivity {

    EditText txtFechaEntrega;
    ImageButton btnFechaEntrega;
    EditText txtNombTarea;
    EditText txtDescripTarea;
    EditText txtTemaTarea;
    Spinner sprComplejidad;
    Spinner sprClasificacion;
    Spinner sprArea;
    Spinner sprActividad;
    Button btnGuardarTarea;
    Calendar calendario;
    DatePickerDialog datePickerDialog;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    public static final String PATH_TAREAS = "tareas/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        txtFechaEntrega = findViewById(R.id.txtFechaEntrega);
        btnFechaEntrega = findViewById(R.id.btnFechaEntrega);
        txtNombTarea = findViewById(R.id.txtNombTarea);
        txtDescripTarea = findViewById(R.id.txtDescripTarea);
        txtTemaTarea = findViewById(R.id.txtTemaTarea);
        sprComplejidad = findViewById(R.id.sprComplejidad);
        sprClasificacion = findViewById(R.id.sprClasificacion);
        sprArea = findViewById(R.id.sprArea);
        sprActividad = findViewById(R.id.sprActividad);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);


        btnFechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AgregarTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        txtFechaEntrega.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, anio, mes, dia);
                datePickerDialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapterComplej = ArrayAdapter.createFromResource(this, R.array.Complejidad, android.R.layout.simple_spinner_item);
        sprComplejidad.setAdapter(adapterComplej);

        ArrayAdapter<CharSequence> adapterClasif = ArrayAdapter.createFromResource(this, R.array.Clasificacion, android.R.layout.simple_spinner_item);
        sprClasificacion.setAdapter(adapterClasif);

        spinnerActiv();

        ArrayAdapter<CharSequence> adapterArea = ArrayAdapter.createFromResource(this, R.array.Area, android.R.layout.simple_spinner_item);
        sprArea.setAdapter(adapterArea);

        btnGuardarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tarea tarea = new Tarea();
                tarea.setNombre(txtNombTarea.getText().toString());
                tarea.setDescripcion(txtDescripTarea.getText().toString());
                tarea.setTema(txtTemaTarea.getText().toString());
                tarea.setComplejidad(sprComplejidad.getSelectedItem().toString());
                tarea.setClasificacion(sprClasificacion.getSelectedItem().toString());
                tarea.setArea(sprArea.getSelectedItem().toString());



                myRef.child("actividades").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Actividad act = ds.getValue(Actividad.class);
                            if(act.getIdUsaurio().equalsIgnoreCase(user.getUid()) && act.getNombre().equalsIgnoreCase(txtNombTarea.getText().toString())){

                                String idAct = act.getIdActividad();
                                Log.i("TAG", "IDACT: " + idAct);
                                tarea.setIdActividad(idAct);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                tarea.setFechaInicio("00/00/0000");
                Calendar fechAsig = Calendar.getInstance();
                int dia = fechAsig.get(Calendar.DAY_OF_MONTH);
                int mes = fechAsig.get(Calendar.MONTH);
                int anio = fechAsig.get(Calendar.YEAR);

                tarea.setFechaAsignacion(dia + "/" + (mes + 1) + "/" + anio);  //El dia que ingresa la tarea
                tarea.setFechaEntrega(txtFechaEntrega.getText().toString());
                tarea.setFechaFinalizacion("00/00/0000");

                Toast.makeText(getApplicationContext(), txtNombTarea.getText().toString(), Toast.LENGTH_LONG).show();
                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String key = myRef.push().getKey();
                myRef = database.getReference(PATH_TAREAS + key);
                tarea.setIdTarea(myRef.getKey());
                myRef.setValue(tarea);

            }
        });

    }

    public void spinnerActiv(){

        List<String> actividades = new ArrayList<>();
        myRef.child("actividades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Actividad actvActual = ds.getValue(Actividad.class);
                    //Log.i("TAG", "USUARIO ACTUAL " + actvActual.getIdUsaurio());
                    //Log.i("TAG", "USUARIO ACTUAL USER " + user.getUid());

                    if(actvActual.getIdUsaurio().equalsIgnoreCase(user.getUid())){

                        //Log.i("TAG", "EXACTO");
                        String nombre = ds.child("nombre").getValue().toString();
                        actividades.add(nombre);
                    }

                    ArrayAdapter<String> adapterAct = new ArrayAdapter<>(AgregarTareaActivity.this, android.R.layout.simple_dropdown_item_1line, actividades);
                    sprActividad.setAdapter(adapterAct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


