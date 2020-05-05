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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PerfilesActivity extends AppCompatActivity {

    ListView perfiles = null;
    Button btnAgregarPerfil;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseAuth mAuth;

    private final static String PATH_USUARIOS="usuarios/";

    private ArrayList<String> nombres;
    private ArrayList<Usuario> usuariosBeneficiarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfiles);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        perfiles = findViewById(R.id.misPerfiles);
        btnAgregarPerfil = findViewById(R.id.btnAgregarPerfil);
        nombres = new ArrayList<>();
        usuariosBeneficiarios = new ArrayList<>();
        cargarPerfilesB();

        perfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            //Toast.makeText(PerfilesActivity.this, "Has pulsado: "+ nombres.get(position), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),DetallePerfilActivity.class);
            //Log.e("Error", "onItemClick: "+ usuariosBeneficiarios.get(position));
            intent.putExtra("Beneficiario", usuariosBeneficiarios.get(position));
            startActivity(intent);
        }
        });

        btnAgregarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AgregarPerfilActivity.class);
                startActivity(i);
            }
        });
    }

    private void cargarPerfilesB(){

        myRef = database.getReference(PATH_USUARIOS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    Usuario beneficiario = sn.getValue(Usuario.class);
                    if(beneficiario.getRol().equals("Beneficiario") && beneficiario.getIdUsuario().equals(user.getUid())){
                        usuariosBeneficiarios.add(beneficiario);
                        nombres.add(beneficiario.getNombres()+" "+beneficiario.getApellidos());
                        Log.i("beneficiarios", "onDataChange: "+(beneficiario.getNombres()+" "+beneficiario.getApellidos()));
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombres);
                adapter.notifyDataSetChanged();
                perfiles.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
