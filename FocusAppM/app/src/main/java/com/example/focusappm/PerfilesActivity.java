package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PerfilesActivity extends AppCompatActivity {

    ListView perfiles = null;
    Button btnAgregarPerfil;

    private ArrayList<String> nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfiles);

        perfiles = findViewById(R.id.misPerfiles);
        btnAgregarPerfil = findViewById(R.id.btnAgregarPerfil);

        btnAgregarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AgregarPerfilActivity.class);
                startActivity(i);
            }
        });

        nombres = new ArrayList<String>();
        nombres.add("Anyi");
        nombres.add("Pablo");
        nombres.add("Lina");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        perfiles.setAdapter(adapter);

        perfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Toast.makeText(PerfilesActivity.this, "Has pulsado: "+ nombres.get(position), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),DetallePerfilActivity.class);
            startActivity(intent);
        }
        });

    }
}
