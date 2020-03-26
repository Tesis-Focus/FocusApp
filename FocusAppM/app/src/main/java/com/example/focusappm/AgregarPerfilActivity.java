package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class AgregarPerfilActivity extends AppCompatActivity {

    EditText edtxNombresPB,edtxApellidosPB,edtxFechaNacPB;
    Spinner spnCursoPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_perfil);

        edtxNombresPB = findViewById(R.id.edtxNombrePB);
        edtxApellidosPB = findViewById(R.id.edtxApellidosPB);
        edtxFechaNacPB = findViewById(R.id.edtxFechaNacPB);
        spnCursoPB = findViewById(R.id.spnCursoPB);


    }
}
