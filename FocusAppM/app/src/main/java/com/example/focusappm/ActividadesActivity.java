 package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

 public class ActividadesActivity extends AppCompatActivity {

     ListView actividades;
     FirebaseDatabase database;
     DatabaseReference myRef;
     String idBeneficiario;
     Usuario beneficiario;



     public static final String PATH_ACTIVIDADES = "actividades/";

     private ArrayList<String> nombresActividades;
     private ArrayList<Actividad> misActividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        actividades = findViewById(R.id.actividades);
        nombresActividades = new ArrayList<>();
        misActividades = new ArrayList<>();

        beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");
        idBeneficiario = beneficiario.getIdBeneficiario();
        cargarTareas();

        actividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),DetalleActividadActivity.class);
                Actividad actividad = misActividades.get(position);
                Log.e("ActividadError", "" +misActividades.get(position).getNombre());
                intent.putExtra("Actividad", misActividades.get(position));
                startActivity(intent);
            }
        });

    }

     private void cargarTareas() {

         myRef.child(PATH_ACTIVIDADES).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 for (DataSnapshot ds: dataSnapshot.getChildren()){
                     Actividad actividad = ds.getValue(Actividad.class);

                     if(actividad.getIdUsaurio().equals(idBeneficiario)){
                         misActividades.add(actividad);
                         nombresActividades.add(actividad.getNombre());
                     }
                 }
                 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombresActividades);
                 adapter.notifyDataSetChanged();
                 actividades.setAdapter(adapter);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }

}
