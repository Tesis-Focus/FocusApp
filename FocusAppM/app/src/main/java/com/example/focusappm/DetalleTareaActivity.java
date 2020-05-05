package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetalleTareaActivity extends AppCompatActivity {

    Tarea tarea;
    EditText nombre;
    EditText descripcion;
    EditText fechaEntrega;
    EditText motivacion;
    EditText complejidad;
    EditText clasificacion;
    EditText area;
    Button btnEliminarTarea;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_TAREAS = "tareas/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        tarea = (Tarea) getIntent().getSerializableExtra("Tarea");

        nombre = findViewById(R.id.edtxNombreTarea);
        descripcion = findViewById(R.id.edtxtDescripcionTarea);
        fechaEntrega = findViewById(R.id.edtxFechaEntrega);
        motivacion = findViewById(R.id.edttxtMotivacion);
        complejidad = findViewById(R.id.edttxtComplejidad);
        clasificacion = findViewById(R.id.edttxtClasificacion);
        area = findViewById(R.id.edttxtArea);
        btnEliminarTarea = findViewById(R.id.btnEliminarTarea);

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

        String todasAreas="";
        for (int i=0; i<tarea.getAreas().size(); i++){
           todasAreas= todasAreas+tarea.getAreas().get(i)+",";
        }
        todasAreas.subSequence(0,todasAreas.length()-1);
        area.setText(todasAreas.subSequence(0,todasAreas.length()-1));


        btnEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(PATH_TAREAS+tarea.getIdTarea());
                myRef.removeValue();

                Toast.makeText(getApplicationContext(), "Tarea eliminada", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getBaseContext(), TareasActivity.class);
                intent.putExtra("idBeneficiario",(String)getIntent().getSerializableExtra("idBeneficiario"));
                startActivity(intent);
            }
        });

    }
}
