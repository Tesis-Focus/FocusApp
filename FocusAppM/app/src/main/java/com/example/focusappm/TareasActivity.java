package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TareasActivity extends AppCompatActivity {

    ListView tareas;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String idBeneficiario;


    public static final String PATH_TAREAS = "tareas/";

    private ArrayList<String> nombresTareas;
    private ArrayList<Tarea> misTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
      /*  mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();*/


        tareas = findViewById(R.id.tareas);
        nombresTareas = new ArrayList<>();
        misTareas = new ArrayList<>();

        idBeneficiario = (String) getIntent().getSerializableExtra("idBeneficiario");
        cargarTareas();

        tareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),DetalleTareaActivity.class);
                intent.putExtra("Tarea", misTareas.get(position));
                intent.putExtra("idBeneficiario",idBeneficiario);
                startActivity(intent);
            }
        });


    }

    private void cargarTareas() {

        myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                     Tarea tarea = ds.getValue(Tarea.class);

                    if(tarea.getIdBeneficiario().equals(idBeneficiario)){
                        misTareas.add(tarea);
                        nombresTareas.add(tarea.getNombre());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombresTareas);
                adapter.notifyDataSetChanged();
                tareas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
